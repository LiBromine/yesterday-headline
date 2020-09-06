package com.java.libingrui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mRepository;
    private LiveData<NewsList> mGetNewsList;
    private LiveData<News> mGetNewsById;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);
        mGetNewsList = mRepository.getNewsList();
        mGetNewsById = null;
    }

    LiveData<NewsList> Var_getNewsList(String str) {
        return mGetNewsList;
    }

    void flushNews() {
        mRepository.flushNews();
    }

    void getMoreNews() {
        mRepository.getMoreNews();
    }

    void getNewsById(String target_id) {
        mGetNewsById = mRepository.getNewsById(target_id);
    }

    LiveData<News> Var_getGetNewsById() {
        return mGetNewsById;
    }
}
