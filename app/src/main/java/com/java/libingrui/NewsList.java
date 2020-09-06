package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

@Entity
@TypeConverters(ListOfNewsConverter.class)
public class NewsList {
    @NonNull
    @PrimaryKey
    public String type;

    public List<News> list;

    NewsList(@NonNull  String type){
        this.type = type;
        list = new ArrayList<News>();
    }

    public void append(List<News> news) {
        list.addAll(news);
    }

    public void insert(News news) {
        list.add(news);
    }

    public void clear() {
        list.clear();
    }
}

class ListOfNewsConverter {
    @TypeConverter
    public String ObjectToString(List<News> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<News> StringToObject(String json) {
        Gson gson = new Gson();
        List<News> result;
        News[] tmp = gson.fromJson(json, News[].class);
        result = new ArrayList<News>();
        Collections.addAll(result, tmp);
        return result;
    }
}
