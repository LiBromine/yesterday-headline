package com.java.libingrui;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@TypeConverters({
        ListOfOneDayEpidemicDataConverter.class,
        ListOfDayConverter.class
})
public class EpidemicInfo {
    @PrimaryKey(autoGenerate = true)
    public int _id;

    @Embedded
    public RegionName region;

    public List<OneDayEpidemicData> data;

    public List<Day> day;

    public int selected;
}

class ListOfOneDayEpidemicDataConverter {
    @TypeConverter
    public String ObjectToString(List<OneDayEpidemicData> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<OneDayEpidemicData> StringToObject(String str) {
        Gson gson = new Gson();
        List<OneDayEpidemicData> result;
        OneDayEpidemicData[] tmp = gson.fromJson(str, OneDayEpidemicData[].class);
        result = new ArrayList<OneDayEpidemicData>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class ListOfDayConverter {
    @TypeConverter
    public String ObjectToString(List<Day> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Day> StringToObject(String str) {
        Gson gson = new Gson();
        List<Day> result;
        Day[] tmp = gson.fromJson(str, Day[].class);
        result = new ArrayList<Day>();
        Collections.addAll(result, tmp);
        return result;
    }
}

