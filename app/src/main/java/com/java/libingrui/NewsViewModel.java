package com.java.libingrui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mRepository;

    private LiveData<NewsList> mNewsList;
    private LiveData<NewsList> mPapersList;

    private LiveData<NewsList> mSearchList;

    private LiveData<NewsList> mWatchedList;

    private LiveData<News> mGetSelectedNews;

    private LiveData<EntityData> mGetSelectedEntityData;

    private LiveData<StringList> mGetCountriesList;
    private LiveData<StringList> mGetProvincesList;
    private LiveData<StringList> mGetCountiesList;

    private LiveData<List<EpidemicInfo>> mSelectedEpidemicInfo;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);

        mNewsList = mRepository.getNewsList();
        mPapersList = mRepository.getPaperList();

        mSearchList = mRepository.getSearchList();

        mWatchedList = mRepository.getWatchedList();

        mGetSelectedNews = mRepository.getSelectedNews();

        mGetSelectedEntityData = mRepository.getSelectedEntityData();

        mGetCountriesList = mRepository.getCountriesList();
        mGetProvincesList = mRepository.getProvincesList();
        mGetCountiesList = mRepository.getCountiesList();

        mSelectedEpidemicInfo = mRepository.getSelectedEpidemicInfo();
    }

    LiveData<NewsList> getListByType(String type) {
        if(type.equals("news")) {
            return mNewsList;
        }
        if(type.equals("paper")) {
            return mPapersList;
        }
        if(type.equals("search")) {
            return mSearchList;
        }
        if(type.equals("watched")) {
            return mWatchedList;
        }
        //@TODO throw an Exception rather than return a null, because return a null may cause system crash
        return null;
    }

    LiveData<StringList> getStringListByType(String type) {
        if(type.equals("countries")) {
            return mGetCountriesList;
        }
        if(type.equals("provinces")) {
            return mGetProvincesList;
        }
        if(type.equals("counties")) {
            return mGetCountiesList;
        }
        return null;
    }

    LiveData<List<EpidemicInfo>> getSelectedEpidemicInfo() {
        return mSelectedEpidemicInfo;
    }

    void getProvincesOfCountry(String country) {
        mRepository.getProvincesOfCountry(country);
    }

    void getCountiesOfProvince(String country, String province) {
        mRepository.getCountiesOfProvince(country,province);
    }

    void getEpidemicInfoByRegionName(String country, String province, String county) {
        mRepository.getEpidemicInfoByRegionName(country, province, county);
    }

    void getEpidemicInfoOfCountries() {
        mRepository.getEpidemicInfoOfCountries();
    }

    void getEpidemicInfoByCountryOfProvinces(String country) {
        mRepository.getEpidemicInfoByCountryOfProvinces(country);
    }

    void updateEpidemicData() {
        mRepository.updateEpidemicData();
    }

    void flushNews(final String type) {
        mRepository.flushNews(type);
    }

    void getMoreNews(final String type) {
        mRepository.getMoreNews(type);
    }

    void getNewsById(String target_id) {
        mRepository.getNewsById(target_id);
    }

    void getEntityDataByUrl(String url) {
        mRepository.getEntityDataByUrl(url);
    }

    void getWatchedNews() {
        mRepository.getWatchedNews();
    }

    void searchNewsByKeyword(final String keyword) {
        mRepository.searchNewsByKeyword(keyword);
    }

    LiveData<News> Var_getSelectedNews() {
        return mGetSelectedNews;
    }

    LiveData<EntityData> getSelectedEntityData() {
        return mGetSelectedEntityData;
    }

    boolean isEpidemicDataReady() {
        return mRepository.getIsEpidemicDataReady();
    }
}
