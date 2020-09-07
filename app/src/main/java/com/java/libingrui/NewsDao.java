package com.java.libingrui;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao {
    //----------------News----------------
    @Insert
    void insert(News news);

    @Query("SELECT * from News where News._id LIKE :target_id")
    News getNewsById(String target_id);

    @Query("DELETE from News where News.type='news'")
    void deleteAllNews();

    @Query("SELECT * from News where News.selected=1")
    LiveData<News> getSelectedNews();

    @Query("SELECT * from News where News.selected=1")
    List<News> getNormalSelectedNews();

    @Query("SELECT * from News where News.is_watched=1")
    List<News> getNormalWatchedNews();

    @Update
    void updateNews(News news);

    //------------NewsList---------------
    @Insert
    void insert(NewsList newslist);

    @Query("DELETE from NewsList")
    void deleteAllNewsList();

    @Query("SELECT * from NewsList where type like :target_type")
    NewsList getNewsListByType(String target_type);

    @Update
    void updateNewsList(NewsList newslist);

    @Query("SELECT * from NewsList where type='news'")
    LiveData<NewsList> getNewsList();

    @Query("SELECT * from NewsList where type='paper'")
    LiveData<NewsList> getPaperList();

    @Query("SELECT * from NewsList where type='search'")
    LiveData<NewsList> getSearchList();

    @Query("SELECT * from NewsList where type='watched'")
    LiveData<NewsList> getWatchedList();

    //------------EpidemicInfo------------
    @Insert
    void insert(EpidemicInfo info);

    @Query("DELETE from EpidemicInfo")
    void deleteAllEpidemicInfo();

    @Query("SELECT * from EpidemicInfo where country like :target_country and province like :target_province and county like :target_county")
    List<EpidemicInfo> getEpidemicInfoByRegionName(String target_country, String target_province, String target_county);

    @Query("SELECT country from EpidemicInfo")
    LiveData<List<String>> getCountryList();

    @Query("SELECT * from EpidemicInfo where country like :target_country")
    List<EpidemicInfo> getProvinceOfCountryList(String target_country);

    @Query("SELECT * from EpidemicInfo where country like :target_country and province like :target_province")
    List<EpidemicInfo> getCountyOfProvinceList(String target_country, String target_province);

    @Query("SELECT * from EpidemicInfo where country like :target_country and province like :target_province" +
            " and county like :target_county and timeValue >= :beginTime and timeValue < :endTime")
    List<EpidemicInfo> getEpidemicInfoByRegionNameWithTimeLimit(String target_country, String target_province, String target_county,
                                                                int beginTime, int endTime);

    @Query("SELECT * from EpidemicInfo where selected = 1")
    LiveData<List<EpidemicInfo>> getSelectedEpidemicInfo();

    @Query("SELECT province from EpidemicInfo where selected = 1")
    LiveData<List<String>> getSelectedProvince();

    @Query("SELECT county from EpidemicInfo where selected = 1")
    LiveData<List<String>> getSelectedCounty();

    @Query("SELECT * from EpidemicInfo where selected = 1")
    List<EpidemicInfo> getNormalSelectedEpidemicInfo();

    @Update
    void updateEpidemicInfo(EpidemicInfo info);
}
