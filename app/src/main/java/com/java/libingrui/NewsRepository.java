package com.java.libingrui;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import android.util.Log;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<News> mSelectedNews;

    private LiveData<NewsList> mGetNewsList;
    private LiveData<NewsList> mGetPaperList;

    private LiveData<NewsList> mGetSearchList;

    private LiveData<NewsList> mGetWatchedList;

    private LiveData<List<String>> mCountryList;
    private LiveData<List<String>> mSelectedProvinceList;
    private LiveData<List<String>> mSelectedCountyList;
    private LiveData<List<EpidemicInfo>> mSelectedEpidemicInfoList;

    private final int NEWS_PER_PAGE = 5;

    private NewsRoomDatabase db;

    NewsRepository(Application application) {
        db = NewsRoomDatabase.getDatabase(application);

        mNewsDao = db.newsDao();

        mSelectedNews = mNewsDao.getSelectedNews();

        mGetNewsList = mNewsDao.getNewsList();
        mGetPaperList = mNewsDao.getPaperList();

        mGetSearchList = mNewsDao.getSearchList();

        mGetWatchedList = mNewsDao.getWatchedList();

        mCountryList = mNewsDao.getCountryList();
        mSelectedProvinceList = mNewsDao.getSelectedProvince();
        mSelectedCountyList = mNewsDao.getSelectedCounty();
        mSelectedEpidemicInfoList = mNewsDao.getSelectedEpidemicInfo();
    }

    LiveData<List<String>> getCountryList() {
        return mCountryList;
    }

    LiveData<List<String>> getSelectedProvince() {
        return mSelectedProvinceList;
    }

    LiveData<List<String>> getSelectedCounty() {
        return mSelectedCountyList;
    }

    LiveData<List<EpidemicInfo>> getSelectedEpidemicInfo() {
        return mSelectedEpidemicInfoList;
    }

    LiveData<NewsList> getNewsList() {
        return mGetNewsList;
    }

    LiveData<NewsList> getPaperList() {
        return mGetPaperList;
    }

    LiveData<NewsList> getSearchList() {
        return mGetSearchList;
    }

    LiveData<NewsList> getWatchedList() {
        return mGetWatchedList;
    }

    LiveData<News> getSelectedNews() {
        return mSelectedNews;
    }

    void updateEpidemicData() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Map<RegionName, DaysEpidemicData> data = new RemoteServiceManager().getEpidemicData();
                Set<Entry<RegionName, DaysEpidemicData> > entries = data.entrySet();

                List<EpidemicInfo> epidemicInfos = new ArrayList<EpidemicInfo>();

                for(Entry<RegionName, DaysEpidemicData> entry : entries) {
                    int day_length = entry.getValue().data.size();
                    String begin_date = entry.getValue().begin;
                    List<Day> days = genPeriod(begin_date, day_length);
                    List<OneDayEpidemicData> datas = entry.getValue().data;
                    for(int i = 0; i < day_length; ++i) {
                        EpidemicInfo info = new EpidemicInfo();
                        info.region = entry.getKey();
                        info.day = days.get(i);
                        info.data = datas.get(i);
                        info.selected = 0;
                        epidemicInfos.add(info);
                    }
                }

                synchronized (db) {
                    for(EpidemicInfo info : epidemicInfos) {
                        mNewsDao.insert(info);
                    }
                }
            }
        });
    }

    private List<Day> genPeriod(String begin_date, int length) {
        List<Day> result = new ArrayList<Day>();
        if(length == 0) return result;

        Day begin_day = new Day(begin_date);
        for(int i = 0; i < length; ++i) {
            result.add(begin_day);
            begin_day.addOne();
        }
        return result;
    }

    void flushNews(final String type) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    NewsList cached_mGetNewsList = null;
                    if(type.equals("news")) {
                        cached_mGetNewsList = mGetNewsList.getValue();
                    }
                    if(type.equals("paper")) {
                        cached_mGetNewsList = mGetPaperList.getValue();
                    }
                    if(cached_mGetNewsList == null) {
                        cached_mGetNewsList = new NewsList(type);
                    }

                    int loaded_news_page_count = 0;
                    List<News> current_news = new ArrayList<News>();
                    while(true) {
                        List<News> append_news = new RemoteServiceManager(NEWS_PER_PAGE, ++loaded_news_page_count).flushNews(type);

                        if(!isAchieveCachedFirst(cached_mGetNewsList, append_news)) {
                            for (int i = 0; i < append_news.size(); ++i) {
                                News news = append_news.get(i);
                                mNewsDao.insert(news);
                            }
                            current_news.addAll(append_news);
                        }
                        else {
                            for (int i = 0; i < append_news.size(); ++i) {
                                News news = append_news.get(i);
                                if(cached_mGetNewsList.list.size() > 0 && news._id.equals(cached_mGetNewsList.list.get(0)._id)) {
                                    break;
                                }
                                mNewsDao.insert(news);
                                current_news.add(news);
                            }
                            current_news.addAll(cached_mGetNewsList.list);
                            break;
                        }
                    }

                    cached_mGetNewsList.list = current_news;
                    NewsList list = mNewsDao.getNewsListByType(type);
                    if(list != null) {
                        list = cached_mGetNewsList;
                        mNewsDao.updateNewsList(list);
                    }
                    else {
                        mNewsDao.insert(cached_mGetNewsList);
                    }
                }
            }
        });
    }

    boolean isAchieveCachedFirst(NewsList cached_mGetNewsList, List<News> list) {
        if(cached_mGetNewsList.list.size() == 0) return true;
        for(News news : list) {
            if(news._id.equals(cached_mGetNewsList.list.get(0)._id))
                return true;
        }
        return false;
    }

    boolean isAchieveCachedLast(NewsList cached_mGetNewsList, List<News> list) {
        for(News news : list)
            if(news._id.equals(cached_mGetNewsList.list.get(cached_mGetNewsList.list.size()-1)._id)) {
                return true;
            }
        return false;
    }

    void getMoreNews(final String type) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    NewsList cached_mGetNewsList = null;
                    if(type.equals("news")) {
                        cached_mGetNewsList = mGetNewsList.getValue();
                    }
                    if(type.equals("paper")) {
                        cached_mGetNewsList = mGetPaperList.getValue();
                    }
                    if(cached_mGetNewsList == null) {
                        cached_mGetNewsList = new NewsList(type);
                    }

                    int cnt = (cached_mGetNewsList.list.size() + NEWS_PER_PAGE - 1) / NEWS_PER_PAGE;

                    List<News> current_news = new RemoteServiceManager(NEWS_PER_PAGE, cnt).flushNews(type);

                    while(!isAchieveCachedLast(cached_mGetNewsList, current_news)) {
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews(type);
                    }

                    int begin_pos = -1;
                    for(int i = 0; i < current_news.size(); ++i) {
                        if(current_news.get(i)._id.equals(cached_mGetNewsList.list.get(cached_mGetNewsList.list.size()-1)._id)) {
                            begin_pos = i + 1;
                            break;
                        }
                    }

                    if(begin_pos < 0) {
                        Log.v("debug", "ERROR when getMoreNews");
                    }

                    if(begin_pos == current_news.size()) {
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews(type);
                        begin_pos = 0;
                    }
                    for(int i = begin_pos; i < current_news.size(); ++i) {
                        mNewsDao.insert(current_news.get(i));
                    }
                    cached_mGetNewsList.append(current_news.subList(begin_pos,current_news.size()));

                    NewsList list = mNewsDao.getNewsListByType(type);
                    if(list != null) {
                        list = cached_mGetNewsList;
                        mNewsDao.updateNewsList(list);
                    }
                    else {
                        mNewsDao.insert(cached_mGetNewsList);
                    }
                }
            }
        });
    }

    void getNewsById(final String id) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<News> list = mNewsDao.getNormalSelectedNews();
                    for(News item : list) {
                        item.selected = 0;
                        mNewsDao.updateNews(item);
                    }
                    News current_news = mNewsDao.getNewsById(id);
                    current_news.selected = 1;
                    mNewsDao.updateNews(current_news);
                }
            }
        });
    }

    void getWatchedNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<News> list = mNewsDao.getNormalWatchedNews();
                    for(int i = 0; i < list.size(); ++i) {
                        list.get(i).myDate = DateManager.NewsStringToDate(list.get(i).date);
                    }
                    Collections.sort(list, new NewsValueComparatorByMyDate());
                    NewsList newslist = mNewsDao.getNewsListByType("watched");
                    if(newslist != null) {
                        newslist.list = list;
                        mNewsDao.updateNewsList(newslist);
                    }
                    else {
                        NewsList cur = new NewsList("watched");
                        cur.list = list;
                        mNewsDao.insert(cur);
                    }
                }
            }
        });
    }

    static class NewsValueComparatorByMyDate implements Comparator<News> {
        @Override
        public int compare(News e1, News e2) {
            return -1 * e1.myDate.compareTo(e2.myDate);
        }
    }

    void getProvinceByCountry(final String country) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for(EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getProvinceOfCountryList(country);
                    for(EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void getCountyByProvince(final String country, final String province) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for(EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getCountyOfProvinceList(country, province);
                    for(EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void getEpidemicInfoByRegionName(final String country, final String province, final String county) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for(EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getEpidemicInfoByRegionName(country,province,county);
                    for(EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void getEpidemicInfoByRegionNameWithTimeLimit(final String country, final String province, final String county, final int begin_time, final int end_time) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for(EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getEpidemicInfoByRegionNameWithTimeLimit(country,province,county,begin_time,end_time);
                    for(EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void getEpidemicInfoOfCountries() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for(EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getEpidemicInfoOfCountries();
                    for(EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void getEpidemicInfoByCountryOfProvinces(final String country) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for (EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getEpidemicInfoByCountryOfProvinces(country);
                    for (EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void getEpidemicInfoByProvinceOfCounties(final String country, final String province) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EpidemicInfo> list = mNewsDao.getNormalSelectedEpidemicInfo();
                    for (EpidemicInfo item : list) {
                        item.selected = 0;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                    List<EpidemicInfo> current_info = mNewsDao.getEpidemicInfoByProvinceOfCounties(country,province);
                    for (EpidemicInfo item : current_info) {
                        item.selected = 1;
                        mNewsDao.updateEpidemicInfo(item);
                    }
                }
            }
        });
    }

    void searchNewsByKeyword(final String keyword) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                RemoteServiceManager remoteServiceManager = new RemoteServiceManager();
                List<EntityDetails> entityDetailsList = remoteServiceManager.getEntitiesByKeyWord(keyword);
                List<News> relatedNews = new ArrayList<News>();
                Set<String> event_ids = new HashSet<String>();
                for(EntityDetails entity : entityDetailsList) {
                    for(String event_id : entity.related_events) {
                        event_ids.add(event_id);
                    }
                }
                for(String event_id : event_ids) {
                    News current_news = remoteServiceManager.getNewsById(event_id);
                    if(current_news != null) {
                        relatedNews.add(current_news);
                    }
                }

                synchronized (db) {
                    NewsList list = mNewsDao.getNewsListByType("search");
                    if(list != null) {
                        list.list = relatedNews;
                        mNewsDao.updateNewsList(list);
                    }
                    else {
                        list = new NewsList("search");
                        list.list = relatedNews;
                        mNewsDao.updateNewsList(list);
                    }
                }
            }
        });
    }
}