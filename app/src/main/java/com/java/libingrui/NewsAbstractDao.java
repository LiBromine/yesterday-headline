package com.java.libingrui;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsAbstractDao {
    @Query("SELECT * FROM NewsAbstract")
    List<NewsAbstract> getAll();

    @Query("SELECT id FROM NewsAbstract")
    List<String> getAllId();

    @Query("SELECT * FROM NewsAbstract WHERE id Like :target_id")
    NewsAbstract findById(String target_id);

    @Insert
    void insertAll(NewsAbstract... newsAbstracts);
}
