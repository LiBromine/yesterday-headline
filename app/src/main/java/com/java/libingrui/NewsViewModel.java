package com.java.libingrui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mRepository;

    private LiveData<NewsList> mNewsList;
    private LiveData<NewsList> mPapersList;

    private LiveData<NewsList> mSearchList;

    private LiveData<News> mGetSelectedNews;

    private LiveData<List<String>> mCountryList;
    private LiveData<List<String>> mSelectedProvince;
    private LiveData<List<String>> mSelectedCounty;
    private LiveData<List<EpidemicInfo>> mSelectedEpidemicInfo;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);

        mNewsList = mRepository.getNewsList();
        mPapersList = mRepository.getPaperList();

        mSearchList = mRepository.getSearchList();

        mGetSelectedNews = mRepository.getSelectedNews();

        mCountryList = mRepository.getCountryList();
        mSelectedProvince = mRepository.getSelectedProvince();
        mSelectedCounty = mRepository.getSelectedCounty();
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
        //@TODO throw an Exception rather than return a null, because return a null may cause system crash
        return null;
    }

    LiveData<List<String>> getCountryList() {
        return mCountryList;
    }

    LiveData<List<String>> getProvinceList() {
        return mSelectedProvince;
    }

    LiveData<List<String>> getCountyList() {
        return mSelectedCounty;
    }

    LiveData<List<EpidemicInfo>> getSelectedEpidemicInfo() {
        return mSelectedEpidemicInfo;
    }

    void getProvinceByCountry(String country) {
        mRepository.getProvinceByCountry(country);
    }

    void getCountyByProvince(String country, String province) {
        mRepository.getCountyByProvince(country, province);
    }

    void getEpidemicInfoByRegionName(String country, String province, String county) {
        mRepository.getEpidemicInfoByRegionName(country, province, county);
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

    void searchNewsByKeyword(final String keyword) {
        mRepository.searchNewsByKeyword(keyword);
    }

    LiveData<News> Var_getSelectedNews() {
        return mGetSelectedNews;
    }
}
