package com.java.libingrui;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface NewsDao {
    @Insert
    void insert(News news);

    @Query("SELECT * from News where News._id LIKE :target_id")
    LiveData<News> getNewsById(String target_id);

    @Query("SELECT * from News where News.type='news' AND News.pageNumber Like :target_page")
    List<News> getNewsByPageNumber(int target_page);

    @Query("DELETE from News where News.type='news'")
    void deleteAllNews();

    @Query("SELECT * from NewsList")
    LiveData<NewsList> getNewsList();

    @Insert
    void insert(NewsList newslist);

    @Query("DELETE from NewsList")
    void deleteAllNewsList();
}
