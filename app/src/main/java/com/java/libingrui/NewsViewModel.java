package com.java.libingrui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mRepository;
    private LiveData<NewsList> mGetNewsList;
    private LiveData<News> mGetSelectedNews;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);
        mGetNewsList = mRepository.getNewsList();
        mGetSelectedNews = mRepository.getSelectedNews();
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
        mRepository.getNewsById(target_id);
    }

    LiveData<News> Var_getSelectedNews() {
        return mGetSelectedNews;
    }
}
