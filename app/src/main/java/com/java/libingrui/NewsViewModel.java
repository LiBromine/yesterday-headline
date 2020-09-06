package com.java.libingrui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private NewsRepository mRepository;
    private LiveData<NewsList> mGetNewsList;
    private LiveData<News> mGetSelectedNews;

    private LiveData<List<String>> mCountryList;
    private LiveData<List<String>> mSelectedProvince;
    private LiveData<List<String>> mSelectedCounty;
    private LiveData<List<EpidemicInfo>> mSelectedEpidemicInfo;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);
        mGetNewsList = mRepository.getNewsList();
        mGetSelectedNews = mRepository.getSelectedNews();
        mCountryList = mRepository.getCountryList();
        mSelectedProvince = mRepository.getSelectedProvince();
        mSelectedCounty = mRepository.getSelectedCounty();
        mSelectedEpidemicInfo = mRepository.getmSelectedEpidemicInfo();
    }

    LiveData<NewsList> Var_getNewsList(String str) {
        return mGetNewsList;
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
