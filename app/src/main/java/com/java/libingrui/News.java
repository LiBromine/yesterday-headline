package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@TypeConverters(ListOfLiBrEntityConverter.class)
public class News {
    @NonNull
    @PrimaryKey
    public String _id;

    public String aminer_id;
    @Ignore
    public List<Author> authors;
    public String category;
    public String content;
    public String date;
    public String doi;

    public List<com.java.libingrui.Entity> entities;
    @Ignore
    public List<GeoInfo> geoInfo;
    public String id;
    public String influence;
    public String lang;
    public String pdf;
    @Ignore
    public List<String> regionIDs;
    @Ignore
    public List<RelatedEvent> related_events;
    public String seg_text;
    public String source;
    public String tflag;
    public String time;
    public String title;
    public String type;
    @Ignore
    public List<String> urls;
    public String year;

    public int selected;

}

class ListOfLiBrEntityConverter {
    @TypeConverter
    public String ObjectToString(List<com.java.libingrui.Entity> list) {
        //@TODO
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<com.java.libingrui.Entity> StringToObject(String str) {
        Gson gson = new Gson();
        List<com.java.libingrui.Entity> result;
        com.java.libingrui.Entity[] tmp = gson.fromJson(json, com.java.libingrui.Entity[].class);
        result = new ArrayList<com.java.libingrui.Entity>();
        Collections.addAll(result, tmp);
        return result;
    }
}