package com.java.libingrui;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

import android.util.Log;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<News> mGetNewsById;
    private LiveData<NewsList> mGetNewsList;
    private NewsList cached_mGetNewsList;

    private final int NEWS_PER_PAGE = 10;

    private int loaded_news_page_count = 0;

    NewsRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        mNewsDao = db.newsDao();
        mGetNewsById = null;
        mGetNewsList = mNewsDao.getNewsList();
    }

    void flushNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<News> current_news = new RemoteServiceManager(NEWS_PER_PAGE).flushNews("news");
                mNewsDao.deleteAllNews();
                for(int i = 0; i < current_news.size(); ++i) {
                    News news = current_news.get(i);
                    news.pageNumber = (i + NEWS_PER_PAGE - 1) / NEWS_PER_PAGE;
                    mNewsDao.insert(news);
                }

                cached_mGetNewsList = new NewsList();
                int limit = Math.min(current_news.size(), NEWS_PER_PAGE);
                //cached_mGetNewsList.list.addAll(current_news.subList(0,limit));

                for(int i = 0; i < limit; ++i) {
                    cached_mGetNewsList.insert(current_news.get(i));
                }

                mNewsDao.deleteAllNewsList();
                mNewsDao.insert(cached_mGetNewsList);
                loaded_news_page_count = 1;
            }
        });
    }

    void getMoreNews() {
        NewsRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ++loaded_news_page_count;
                List<News> current_news = mNewsDao.getNewsByPageNumber(loaded_news_page_count);
                cached_mGetNewsList.append(current_news);
                mNewsDao.deleteAllNewsList();
                mNewsDao.insert(cached_mGetNewsList);
            }
        });
    }

    LiveData<NewsList> getNewsList() {
        return mGetNewsList;
    }
}
