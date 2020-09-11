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

    private LiveData<NewsList> mEventList;

    private LiveData<PersonList> mAllPersonList;
    private LiveData<PersonList> mPassedAwayPersonList;

    private LiveData<News> mGetSelectedNews;

    private LiveData<Person> mGetSelectedPerson;

    private LiveData<EntityData> mGetSelectedEntityData;

    private LiveData<EntityDataList> mGetSearchEntityDataList;

    private LiveData<StringList> mGetCountriesList;
    private LiveData<StringList> mGetProvincesList;
    private LiveData<StringList> mGetCountiesList;

    private LiveData<StringList> mGetKeywordsList;

    private LiveData<BitmapData> mSelectedBitmapData;

    private LiveData<List<EpidemicInfo>> mSelectedEpidemicInfo;

    public NewsViewModel(Application application) {
        super(application);
        mRepository = new NewsRepository(application);

        mNewsList = mRepository.getNewsList();
        mPapersList = mRepository.getPaperList();

        mSearchList = mRepository.getSearchList();

        mWatchedList = mRepository.getWatchedList();

        mEventList = mRepository.getEventList();

        mAllPersonList = mRepository.getAllPersonList();
        mPassedAwayPersonList = mRepository.getPassedAwayPersonList();

        mGetSelectedNews = mRepository.getSelectedNews();

        mGetSelectedPerson = mRepository.getSelectedPerson();

        mGetSelectedEntityData = mRepository.getSelectedEntityData();

        mGetCountriesList = mRepository.getCountriesList();
        mGetProvincesList = mRepository.getProvincesList();
        mGetCountiesList = mRepository.getCountiesList();

        mGetKeywordsList = mRepository.getKeywordsList();

        mSelectedEpidemicInfo = mRepository.getSelectedEpidemicInfo();

        mGetSearchEntityDataList = mRepository.getSearchEntityDataList();

        mSelectedBitmapData = mRepository.getSelectedBitmapData();
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
        if(type.equals("event")) {
            return mEventList;
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
        if(type.equals("keywords")) {
            return  mGetKeywordsList;
        }
        return null;
    }

    LiveData<BitmapData> getSelectedBitmapData() { return mSelectedBitmapData;}

    LiveData<EntityDataList> getSearchEntityDataList() {
        return mGetSearchEntityDataList;
    }

    LiveData<List<EpidemicInfo>> getSelectedEpidemicInfo() {
        return mSelectedEpidemicInfo;
    }

    LiveData<PersonList> getAllPersonList() {
        return mAllPersonList;
    }

    LiveData<PersonList> getPassedAwayPersonList() {
        return mPassedAwayPersonList;
    }

    void getBitmapDataByUrl(String url) throws MyException{
        mRepository.getBitmapDataByUrl(url);
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

    void updateEpidemicData() throws MyException{
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

    void getPersonById(String target_id) { mRepository.getPersonById(target_id);}

    void getEntityDataByUrl(String url) {
        mRepository.getEntityDataByUrl(url);
    }

    void getWatchedNews() {
        mRepository.getWatchedNews();
    }

    void flushPerson() {
        mRepository.flushPerson();;
    }

    void flushPassedAwayPerson() {
        mRepository.getPassedAwayPerson();
    }

    void flushEventNews() {
        mRepository.flushEventNews();
    }

    void getEventListByType(int type) {
        mRepository.getEventListByType(type);
    }

    void searchNewsByKeyword(final String keyword) {
        mRepository.searchNewsByKeyword(keyword);
    }

    LiveData<News> Var_getSelectedNews() {
        return mGetSelectedNews;
    }

    LiveData<Person> getSelectedPerson() {
        return mGetSelectedPerson;
    }

    LiveData<EntityData> getSelectedEntityData() {
        return mGetSelectedEntityData;
    }

    boolean isEpidemicDataReady() {
        return mRepository.getIsEpidemicDataReady();
    }

    void searchEntityDataByKeyword(final String keyword) {
        mRepository.searchEntityDataListByKeyword(keyword);
    }
}
