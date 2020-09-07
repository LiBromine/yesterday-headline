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
import java.util.Date;

@Entity
@TypeConverters({
        ListOfLiBrEntityConverter.class,
        DateConverter.class,
        ListOfGeoInfoConverter.class,
        ListOfAuthorConverter.class,
        ListOfStringConverter.class,
        ListOfRelatedEventConverter.class
})
public class News {
    @NonNull
    @PrimaryKey
    public String _id;

    public String aminer_id;
    public List<Author> authors;
    public String category;
    public String content;
    public String date;
    public String doi;
    public List<com.java.libingrui.Entity> entities;
    public List<GeoInfo> geoInfo;
    public String id;
    public String influence;
    public String lang;
    public String pdf;
    public List<String> regionIDs;
    public List<RelatedEvent> related_events;
    public String seg_text;
    public String source;
    public String tflag;
    public String time;
    public String title;
    public String type;
    public List<String> urls;
    public String year;

    public int selected;
    public int is_watched;
    public Date myDate;
}

class ListOfLiBrEntityConverter {
    @TypeConverter
    public String ObjectToString(List<com.java.libingrui.Entity> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<com.java.libingrui.Entity> StringToObject(String str) {
        Gson gson = new Gson();
        List<com.java.libingrui.Entity> result;
        com.java.libingrui.Entity[] tmp = gson.fromJson(str, com.java.libingrui.Entity[].class);
        result = new ArrayList<com.java.libingrui.Entity>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class ListOfGeoInfoConverter {
    @TypeConverter
    public String ObjectToString(List<GeoInfo> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<GeoInfo> StringToObject(String str) {
        Gson gson = new Gson();
        List<GeoInfo> result;
        GeoInfo[] tmp = gson.fromJson(str, GeoInfo[].class);
        result = new ArrayList<GeoInfo>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class ListOfAuthorConverter {
    @TypeConverter
    public String ObjectToString(List<Author> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Author> StringToObject(String str) {
        Gson gson = new Gson();
        List<Author> result;
        Author[] tmp = gson.fromJson(str, Author[].class);
        result = new ArrayList<Author>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class ListOfStringConverter {
    @TypeConverter
    public String ObjectToString(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<String> StringToObject(String str) {
        Gson gson = new Gson();
        List<String> result;
        String[] tmp = gson.fromJson(str, String[].class);
        result = new ArrayList<String>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class ListOfRelatedEventConverter {
    @TypeConverter
    public String ObjectToString(List<RelatedEvent> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<RelatedEvent> StringToObject(String str) {
        Gson gson = new Gson();
        List<RelatedEvent> result;
        RelatedEvent[] tmp = gson.fromJson(str, RelatedEvent[].class);
        result = new ArrayList<RelatedEvent>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class DateConverter {
    @TypeConverter
    public String ObjectToString(Date myDate) {
        Gson gson = new Gson();
        return gson.toJson(myDate);
    }

    @TypeConverter
    public Date StringToObject(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str, Date.class);
    }
}