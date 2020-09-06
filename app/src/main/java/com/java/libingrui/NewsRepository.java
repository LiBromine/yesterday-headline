package com.java.libingrui;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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
        cached_mGetNewsList = new NewsList();
    }

    void flushNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (db) {
                    Log.v("debug", "before networkservice flush");
                    mNewsDao.deleteAllNews();
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
                            for(News news : cached_mGetNewsList.list) {
                                mNewsDao.insert(news);
                            }
                            break;
                        }
                    }

                    cached_mGetNewsList = new NewsList();
                    cached_mGetNewsList.list = current_news;
 /*                   int limit = Math.min(current_news.size(), NEWS_PER_PAGE);
                    //cached_mGetNewsList.list.addAll(current_news.subList(0,limit));

                    for (int i = 0; i < limit; ++i) {
                        cached_mGetNewsList.insert(current_news.get(i));
                    }*/

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
                    int cnt = (cached_mGetNewsList.list.size() + NEWS_PER_PAGE - 1) / NEWS_PER_PAGE;

                    Log.v("debug", "pos="+cnt);
                    List<News> current_news = new RemoteServiceManager(NEWS_PER_PAGE, cnt).flushNews("news");

                    while(!isAchieveCachedLast(current_news)) {
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews("news");
                        Log.v("debug", "pos="+cnt);
                    }
                    int begin_pos = -1;
                    for(int i = 0; i < current_news.size(); ++i) {
                        if(current_news.get(i)._id.equals(cached_mGetNewsList.list.get(cached_mGetNewsList.list.size()-1)._id)) {
                            begin_pos = i + 1;
                            break;
                        }
                    }
                    Log.v("debug","begin_pos="+begin_pos+" size="+current_news.size());
                    if(begin_pos < 0) {
                        Log.v("debug", "ERROR when getMoreNews");
                    }
                    if(begin_pos == current_news.size()) {
                        current_news = new RemoteServiceManager(NEWS_PER_PAGE, ++cnt).flushNews("news");
                        begin_pos = 0;
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
