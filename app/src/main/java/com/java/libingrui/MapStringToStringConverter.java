package com.java.libingrui;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class MapStringToStringConverter {
    @TypeConverter
    public String ObjectToString(Map<String,String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @TypeConverter
    public Map<String,String> StringToObject(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,String>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
