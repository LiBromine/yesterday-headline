package com.java.libingrui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mRepository;
    private LiveData<NewsList> mGetNewsList;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);
        mGetNewsList = mRepository.getNewsList();
    }

    LiveData<NewsList> getNewsList() {
        return mGetNewsList;
    }

    void flushNews() {
        mRepository.flushNews();
    }

    void getMoreNews() {
        mRepository.getMoreNews();
    }
}
