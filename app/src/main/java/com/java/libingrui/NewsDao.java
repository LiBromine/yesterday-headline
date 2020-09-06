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
    @Insert
    void insert(News news);

    @Query("SELECT * from News where News._id LIKE :target_id")
    News getNewsById(String target_id);

    @Query("DELETE from News where News.type='news'")
    void deleteAllNews();

    @Query("SELECT * from NewsList")
    LiveData<NewsList> getNewsList();

    @Query("SELECT * from News where News.selected=1")
    LiveData<News> getSelectedNews();

    @Query("SELECT * from News where News.selected=1")
    List<News> getNormalSelectedNews();

    @Update
    void updateNews(News news);

    @Insert
    void insert(NewsList newslist);

    @Query("DELETE from NewsList")
    void deleteAllNewsList();
}
