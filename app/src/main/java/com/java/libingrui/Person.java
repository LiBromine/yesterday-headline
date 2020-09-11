package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.List;

@Entity
@TypeConverters({
        MapStringToStringConverter.class,
        ListOfStringConverter.class,
        ListOfDoubleConverter.class,
        ListOfEmail_uConverter.class
})
public class Person {
    public Map<String,String> aff;
    public String avatar;
    public boolean bind;

    @PrimaryKey
    @NonNull
    public String id;

    public Map<String,String> indices;
    public String name;
    public String name_zh;
    public int num_followed;
    public int num_viewed;

    @Embedded
    public Profile profile;

    public int score;
    public String sourcetype;
    public List<String> tags;
    public List<Double> tags_score;
    public int index;
    public int tab;
    public boolean is_passedaway;

    public int selected;
}

