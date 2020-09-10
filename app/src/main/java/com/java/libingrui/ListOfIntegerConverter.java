package com.java.libingrui;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfIntegerConverter {
    @TypeConverter
    public String ObjectToString(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Integer> StringToObject(String str) {
        Gson gson = new Gson();
        List<Integer> result;
        Integer[] tmp = gson.fromJson(str, Integer[].class);
        result = new ArrayList<Integer>();
        if(tmp != null) Collections.addAll(result, tmp);
        return result;
    }
}
