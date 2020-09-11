package com.java.libingrui;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<News> mSelectedNews;

    private LiveData<Person> mSelectedPerson;

    private LiveData<EntityData> mSelectedEntityData;

    private LiveData<NewsList> mGetNewsList;
    private LiveData<NewsList> mGetPaperList;

    private LiveData<NewsList> mGetSearchList;

    private LiveData<NewsList> mGetWatchedList;

    private LiveData<NewsList> mGetEventList;

    private LiveData<PersonList> mGetAllPersonList;

    private LiveData<EntityDataList> mGetSearchEntityDataList;

    private LiveData<PersonList> mGetPassedAwayPersonList;

    private LiveData<StringList> mGetCountriesList;
    private LiveData<StringList> mGetProvincesList;
    private LiveData<StringList> mGetCountiesList;

    private LiveData<List<EpidemicInfo>> mSelectedEpidemicInfoList;

    private LiveData<BitmapData> mSelectedBitmapData;

    private LiveData<StringList> mGetKeywordsList;

    private final int NEWS_PER_PAGE = 5;

    private NewsRoomDatabase db;

    private boolean isEpidemicDataReady;

    NewsRepository(Application application) {
        db = NewsRoomDatabase.getDatabase(application);

        mNewsDao = db.newsDao();

        mSelectedNews = mNewsDao.getSelectedNews();

        mSelectedPerson = mNewsDao.getSelectedPerson();

        mSelectedEntityData = mNewsDao.getSelectedEntityData();

        mGetNewsList = mNewsDao.getNewsList();
        mGetPaperList = mNewsDao.getPaperList();

        mGetSearchList = mNewsDao.getSearchList();

        mGetWatchedList = mNewsDao.getWatchedList();

        mGetEventList = mNewsDao.getEventList();

        mGetAllPersonList = mNewsDao.getAllPersonList();
        mGetPassedAwayPersonList = mNewsDao.getPassedAwayPersonList();

        mGetSearchEntityDataList = mNewsDao.getSearchEntityDataList();

        mGetCountriesList = mNewsDao.getCountriesStringList();
        mGetProvincesList = mNewsDao.getProvincesStringList();
        mGetCountiesList = mNewsDao.getCountiesStringList();

        mGetKeywordsList = mNewsDao.getKeywordsStringList();

        mSelectedEpidemicInfoList = mNewsDao.getSelectedEpidemicInfo();

        mSelectedBitmapData = mNewsDao.getSelectedBitmapData();

        isEpidemicDataReady = false;
    }

    LiveData<StringList> getCountriesList() { return mGetCountriesList;}

    LiveData<StringList> getProvincesList() { return mGetProvincesList;}

    LiveData<StringList> getCountiesList() { return mGetCountiesList;}

    LiveData<StringList> getKeywordsList() { return mGetKeywordsList;}

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

    LiveData<NewsList> getEventList() {
        return mGetEventList;
    }

    LiveData<PersonList> getAllPersonList() { return mGetAllPersonList;}

    LiveData<PersonList> getPassedAwayPersonList() { return mGetPassedAwayPersonList;}

    LiveData<EntityDataList> getSearchEntityDataList() {
        return mGetSearchEntityDataList;
    }

    LiveData<News> getSelectedNews() {
        return mSelectedNews;
    }

    LiveData<Person> getSelectedPerson() { return mSelectedPerson;}

    LiveData<EntityData> getSelectedEntityData() {
        return mSelectedEntityData;
    }

    LiveData<BitmapData> getSelectedBitmapData() { return mSelectedBitmapData;}

    void getEventListByType(final int type) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<News> list = mNewsDao.getEventListByType(type);
                    NewsList current_list = new NewsList("event");
                    current_list.list = list;
                    mNewsDao.insert(current_list);
                }
            }
        });
    }

    void flushEventNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<News> list = new RemoteServiceManager(700,1).flushNews("event");
                    NewsClusterManager manager = new NewsClusterManager();
                    int type_cnt = manager.getType_cnt();
                    StringList typeList = new StringList("keywords");
                    for(int i = 0; i < type_cnt; ++i) {
                        typeList.nameList.add(manager.getKeywordsByType(i));
                    }
                    for(int i = 0; i < list.size(); ++i) {
                        list.get(i).cluster_type = manager.getTypeByPos(i);
                    }
                    synchronized (db) {
                        for(News news : list) {
                            mNewsDao.insert(news);
                        }
                        mNewsDao.insert(typeList);
                    }
                }
                catch (MyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getBitmapDataByUrl(final String url) throws MyException {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<BitmapData> old_list = mNewsDao.getNormalSelectedBitmapData();
                    if(old_list != null) {
                        for(BitmapData data : old_list) {
                            data.selected = 0;
                            mNewsDao.insert(data);
                        }
                    }
                    BitmapData cur_data = mNewsDao.getBitmapDataByUrl(url);
                    if(cur_data != null) {
                        cur_data.selected = 1;
                        mNewsDao.insert(cur_data);
                    }
                    else {
                        if(url == null || url.length() == 0) {
                            return;
                        }
                        cur_data = BitmapByteArrayConverter.BitmapToByteArray(new RemoteServiceManager().getBitmapByUrl(url));
                        cur_data.url = url;
                        cur_data.selected = 1;
                        Log.v("debug", "url="+url);
                        Log.v("debug", "length="+cur_data.bitmap.length);
                        mNewsDao.insert(cur_data);
                    }
                }
            }
        });
    }

    void updateEpidemicData() throws MyException{
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("debug", "upd EpiData net service begin");
                Map<RegionName, DaysEpidemicData> data = null;
                try {
                    data = new RemoteServiceManager().getEpidemicData();
                }
                catch( MyException e) {
                    e.printStackTrace();
                }
                Set<Entry<RegionName, DaysEpidemicData> > entries = data.entrySet();

                Log.v("debug", "upd EpiData net service end");

                List<EpidemicInfo> epidemicInfos = new ArrayList<EpidemicInfo>();

                for(Entry<RegionName, DaysEpidemicData> entry : entries) {
                    int day_length = entry.getValue().data.size();
                    String begin_date = entry.getValue().begin;
                    List<Day> days = genPeriod(begin_date, day_length);
                    List<OneDayEpidemicData> datas = entry.getValue().data;
                    EpidemicInfo info = new EpidemicInfo();
                    info.region = entry.getKey();
                    info.day = days;
                    info.data = datas;
                    info.selected = 0;
                    epidemicInfos.add(info);
                    if(info.region.country.equals("China") && info.region.province.equals("total")) {
                        Day tmp = new Day(begin_date);
                        Log.v("debug", "begin_date="+begin_date+" "+tmp.year+" "+tmp.month+" "+tmp.date);
                        Log.v("debug", "begin=" + begin_date + " length=" + day_length);
                        for(Day day : days) {
                            Log.v("debug", "day = " + day.year + "/" + day.month + "/" + day.date);
                        }
                    }
                }

                Log.v("debug", "upd EpiData map end");

                synchronized (db) {
                    for(EpidemicInfo info : epidemicInfos) {
                        mNewsDao.insert(info);
                    }

                    StringList countryList = new StringList("countries");
                    countryList.nameList = new ArrayList<>();
                    List<CountryName> countryNames = mNewsDao.getNormalCountryList();
                    for(CountryName name : countryNames) {
                        countryList.nameList.add(name.country);
                    }
                    Collections.sort(countryList.nameList);
                    mNewsDao.insert(countryList);
                }
                Log.v("debug", "build EpidemicData finish");
                isEpidemicDataReady = true;
            }
        });
    }

    boolean getIsEpidemicDataReady() {
        return isEpidemicDataReady;
    }

    private List<Day> genPeriod(String begin_date, int length) {
        List<Day> result = new ArrayList<Day>();
        if(length == 0) return result;

        Day begin_day = new Day(begin_date);
        for(int i = 0; i < length; ++i) {
            Day current_day = new Day();
            current_day.year = begin_day.year;
            current_day.month = begin_day.month;
            current_day.date  = begin_day.date;
            current_day.calcTimeValue();
            result.add(current_day);
            begin_day.addOne();
        }
        return result;
    }

    void flushPerson() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<Person> personList = new ArrayList<>();
                    List<Person> append_person = null;
                    try {
                        append_person = new RemoteServiceManager().flushPerson();
                    }
                    catch( MyException e) {
                        e.printStackTrace();
                    }
                    if(append_person != null) {
                        personList.addAll(append_person);
                    }
                    PersonList myList = new PersonList("all");
                    for(Person person : personList) {
                        myList.list.add(person);
                        mNewsDao.insert(person);
                    }
                    mNewsDao.insert(myList);
                }
            }
        });
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
                        List<News> append_news = null;
                        try {
                            append_news = new RemoteServiceManager(NEWS_PER_PAGE, ++loaded_news_page_count).flushNews(type);
                        }
                        catch (MyException e) {
                            e.printStackTrace();
                            return;
                        }

                        if(!isAchieveCachedFirst(cached_mGetNewsList, append_news)) {
                            for (int i = 0; i < append_news.size(); ++i) {
                                News news = append_news.get(i);
                                savedInsertNews(news);
                            }
                            current_news.addAll(append_news);
                        }
                        else {
                            for (int i = 0; i < append_news.size(); ++i) {
                                News news = append_news.get(i);
                                if(cached_mGetNewsList.list.size() > 0 && news._id.equals(cached_mGetNewsList.list.get(0)._id)) {
                                    break;
                                }
                                savedInsertNews(news);
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

                    List<News> current_news = null;
                    try {
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, cnt).flushNews(type);
                    }
                    catch (MyException e) {
                        e.printStackTrace();
                        return;
                    }
                    while(!isAchieveCachedLast(cached_mGetNewsList, current_news)) {
                        try {
                            current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews(type);
                        } catch (MyException e) {
                            e.printStackTrace();
                            return;
                        }
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
                        try {
                            current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews(type);
                        }
                        catch (MyException e){
                            e.printStackTrace();
                            return;
                        }
                        begin_pos = 0;
                    }
                    for(int i = begin_pos; i < current_news.size(); ++i) {
                        savedInsertNews(current_news.get(i));
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
        Log.v("debug", "getNewsById="+id);
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
                    if(current_news == null) {
                        return;
                    }
                    current_news.selected = 1;
                    current_news.is_watched = 1;
                    mNewsDao.updateNews(current_news);
                    String type = current_news.type;
                    NewsList fatherList = mNewsDao.getNewsListByType(type);
                    for(int i = 0; i < fatherList.list.size(); ++i) {
                        if(fatherList.list.get(i)._id.equals(current_news._id)) {
                            fatherList.list.get(i).is_watched = 1;
                            break;
                        }
                    }
                    mNewsDao.insert(fatherList);
                }
            }
        });
    }

    void getPersonById(final String id) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<Person> list = mNewsDao.getNormalSelectedPerson();
                    for(Person item : list) {
                        item.selected = 0;
                        mNewsDao.insert(item);
                    }
                    Person current_person = mNewsDao.getPersonById(id);
                    current_person.selected = 1;
                    mNewsDao.insert(current_person);
                }
            }
        });
    }

    void getPassedAwayPerson() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<Person> list = mNewsDao.getNormalPassedAwayPerson();
                    PersonList myList = new PersonList("passedaway");
                    for(Person person : list) {
                        myList.list.add(person);
                    }
                    mNewsDao.insert(myList);
                }
            }
        });
    }

    void getEntityDataByUrl(final String url) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<EntityData> list = mNewsDao.getNormalSelectedEntityData();
                    for(EntityData item : list) {
                        item.selected = 0;
                        mNewsDao.insert(item);
                    }

                    EntityData current_data = mNewsDao.getEntityDataByUrl(url);
                    if(current_data == null) {
                        Log.v("debug", "read from remote");
                        RemoteServiceManager mangager = new RemoteServiceManager();
                        current_data = new EntityData();
                        try {
                            current_data.entityDetails = mangager.getEntityByUrl(url);
                        }
                        catch (MyException e) {
                            e.printStackTrace();
                        }
                    }
                    current_data.selected = 1;
                    mNewsDao.insert(current_data);
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

    void getProvincesOfCountry(final String country) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<ProvinceName> list = mNewsDao.getNormalProvinceList(country);
                    StringList provinceList = new StringList("provinces");
                    provinceList.nameList = new ArrayList<>();
                    for(ProvinceName name : list) {
                        provinceList.nameList.add(name.province);
                    }
                    Collections.sort(provinceList.nameList);
                    mNewsDao.insert(provinceList);
                }
            }
        });
    }

    void getCountiesOfProvince(final String country, final String province) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    List<CountyName> list = mNewsDao.getNormalCountyList(country, province);
                    StringList countyList = new StringList("counties");
                    countyList.nameList = new ArrayList<>();
                    for (CountyName name : list) {
                        countyList.nameList.add(name.county);
                    }
                    Collections.sort(countyList.nameList);
                    mNewsDao.insert(countyList);
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

    void searchEntityDataListByKeyword(final String keyword) {
        Log.v("debug", "achieve here");
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    RemoteServiceManager remoteServiceManager = new RemoteServiceManager();
                    List<EntityDetails> entityDetailsList = null;
                    try {
                        entityDetailsList = remoteServiceManager.getEntitiesByKeyWord(keyword);
                        Log.v("debug", "search size=" + entityDetailsList.size());
                    } catch (MyException e) {
                        e.printStackTrace();
                    }
                    if(entityDetailsList == null) {
                        entityDetailsList = new ArrayList<>();
                    }
                    List<EntityData> result = new ArrayList<EntityData>();
                    for (EntityDetails item : entityDetailsList) {
                        EntityData current = new EntityData();
                        current.entityDetails = item;
                        current.selected = 0;
                        result.add(current);
                        mNewsDao.insert(current);
                    }

                    EntityDataList list = mNewsDao.getEntityDataListByType("search");
                    if (list == null) {
                        Log.v("debug", "new search list");
                        list = new EntityDataList("search");
                    }
                    list.list = result;
                    mNewsDao.insert(list);
                }
            }
        });
    }

    void searchNewsByKeyword(final String keyword) {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                RemoteServiceManager remoteServiceManager = new RemoteServiceManager();
                List<EntityDetails> entityDetailsList = null;
                try {
                    entityDetailsList = remoteServiceManager.getEntitiesByKeyWord(keyword);
                }
                catch (MyException e) {
                    e.printStackTrace();
                }
                List<News> relatedNews = new ArrayList<News>();
                Set<String> event_ids = new HashSet<String>();
                for(EntityDetails entity : entityDetailsList) {
                    if(entity.related_events != null) {
                        for (String event_id : entity.related_events) {
                            event_ids.add(event_id);
                        }
                    }
                }
/*
                for(String event_id : event_ids) {
                    News current_news = remoteServiceManager.getNewsById(event_id);
                    if(current_news != null) {
                        relatedNews.add(current_news);
                    }
                }
*/
                synchronized (db) {
                    List<String> entity_urls = new ArrayList<String>();
                    for(EntityDetails item : entityDetailsList) {
                        entity_urls.add(item.url);
                    }
                    entity_urls.add(keyword);

                    List<String> ref_news_id_list = mNewsDao.getNewsIdByEntity(entity_urls);
                    for(String id : ref_news_id_list) {
                        event_ids.add(id);
                    }

                    for(String event_id : event_ids) {
                        News news = mNewsDao.getNewsById(event_id);
                        if(news == null) {
                            try {
                                news = remoteServiceManager.getNewsById(event_id);
                            }
                            catch (MyException e) {
                                e.printStackTrace();
                            }
                        }
                        if(news != null) {
                            relatedNews.add(news);
                        }
                    }

                    for(News news : relatedNews) {
                        savedInsertNews(news);
                    }
                    NewsList list = mNewsDao.getNewsListByType("search");
                    if(list != null) {
                        list.list = relatedNews;
                        mNewsDao.updateNewsList(list);
                    }
                    else {
                        list = new NewsList("search");
                        list.list = relatedNews;
                        mNewsDao.insert(list);
                    }
                }
            }
        });
    }

    void savedInsertNews(News news) {
        for(Entity entity : news.entities) {
            NewsEntityCrossRef ref = new NewsEntityCrossRef();
            ref.news_id = news._id;
            ref.url = entity.url;
            mNewsDao.insert(ref);

            NewsEntityCrossRef ref2 = new NewsEntityCrossRef();
            ref2.url = entity.label;
            ref2.news_id = news._id;
            mNewsDao.insert(ref2);

   //         Log.v("debug", "add " + ref.url + ref.news_id);
   //         Log.v("debug", "add " + ref2.url + ref2.news_id);
        }
        mNewsDao.insert(news);
    }
}