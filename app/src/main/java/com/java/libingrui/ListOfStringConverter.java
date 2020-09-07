package com.java.libingrui;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if(tmp != null) Collections.addAll(result, tmp);
        return result;
    }
}
