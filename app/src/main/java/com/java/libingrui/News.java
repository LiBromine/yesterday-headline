package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.util.List;

@Entity
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
    @Ignore
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
