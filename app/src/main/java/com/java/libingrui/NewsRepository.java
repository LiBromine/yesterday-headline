package com.java.libingrui;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import java.util.Map;

import kotlinx.coroutines.DefaultExecutor;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<News> mGetNewsById;
    private LiveData<NewsList> mGetNewsList;
    private NewsList cached_mGetNewsList;

    private final int NEWS_PER_PAGE = 5;

    private int loaded_news_page_count = 0;

    private NewsRoomDatabase db;

    NewsRepository(Application application) {
        db = NewsRoomDatabase.getDatabase(application);
        mNewsDao = db.newsDao();
        mGetNewsById = null;
        mGetNewsList = mNewsDao.getNewsList();
    }

    void updateEpidemicData() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Map<RegionName, DaysEpidemicData> data = new RemoteServiceManager().getEpidemicData();
                synchronized (db) {
                    //@TODO here
                }
            }
        });
    }

    void flushNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    cached_mGetNewsList = mGetNewsList.getValue();
                    if(cached_mGetNewsList == null) {
                        cached_mGetNewsList = new NewsList();
                    }
                    Log.v("debug", "before networkservice flush, we have " + cached_mGetNewsList.list.size());
                    loaded_news_page_count = 0;
                    List<News> current_news = new ArrayList<News>();
                    while(true) {
                        List<News> append_news = new RemoteServiceManager(NEWS_PER_PAGE, ++loaded_news_page_count).flushNews("news");
                        if(!isAchieveCachedFirst(append_news)) {
                            for (int i = 0; i < append_news.size(); ++i) {
                                News news = append_news.get(i);
                                news.pageNumber = loaded_news_page_count;
                                mNewsDao.insert(news);
                            }
                            current_news.addAll(append_news);
                        }
                        else {
                            for (int i = 0; i < append_news.size(); ++i) {
                                News news = append_news.get(i);
                                news.pageNumber = loaded_news_page_count;
                                if(cached_mGetNewsList.list.size() > 0 && news._id.equals(cached_mGetNewsList.list.get(0)._id)) {
                                    Log.v("debug", "break at " + i);
                                    break;
                                }
                                mNewsDao.insert(news);
                                current_news.add(news);
                            }
                            current_news.addAll(cached_mGetNewsList.list);
                            break;
                        }
                    }

 /*                   int limit = Math.min(current_news.size(), NEWS_PER_PAGE);
                    //cached_mGetNewsList.list.addAll(current_news.subList(0,limit));

                    for (int i = 0; i < limit; ++i) {
                        cached_mGetNewsList.insert(current_news.get(i));
                    }*/
                    cached_mGetNewsList.list = current_news;
                    mNewsDao.deleteAllNewsList();
                    mNewsDao.insert(cached_mGetNewsList);
                 //   loaded_news_page_count = 1;
                }
            }
        });
    }

    boolean isAchieveCachedFirst(List<News> list) {
        if(cached_mGetNewsList.list.size() == 0) return true;
        for(News news : list) {
            if(news._id.equals(cached_mGetNewsList.list.get(0)._id))
                return true;
        }
        return false;
    }

    boolean isAchieveCachedLast(List<News> list) {
        for(News news : list)
            if(news._id.equals(cached_mGetNewsList.list.get(cached_mGetNewsList.list.size()-1)._id)) {
                return true;
            }
        return false;
    }

    void getMoreNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    cached_mGetNewsList = mGetNewsList.getValue();

                    int cnt = (cached_mGetNewsList.list.size() + NEWS_PER_PAGE - 1) / NEWS_PER_PAGE;
                    Log.v("debug", "before get new, we have " + cached_mGetNewsList.list.size() + " in total, and page number is " + cnt);

                    List<News> current_news = new RemoteServiceManager(NEWS_PER_PAGE, cnt).flushNews("news");

                    while(!isAchieveCachedLast(current_news)) {
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews("news");
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
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews("news");
                        begin_pos = 0;
                    }
                    for(int i = begin_pos; i < current_news.size(); ++i) {
                        mNewsDao.insert(current_news.get(i));
                    }
                    cached_mGetNewsList.append(current_news.subList(begin_pos,current_news.size()));
                    mNewsDao.deleteAllNewsList();
                    mNewsDao.insert(cached_mGetNewsList);
                }
            }
        });
    }

    LiveData<NewsList> getNewsList() {
        return mGetNewsList;
    }

    LiveData<News> getNewsById(String id) {
        return mGetNewsById = mNewsDao.getNewsById(id);
    }
}