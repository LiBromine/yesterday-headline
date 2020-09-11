package com.java.libingrui;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfDoubleConverter {
    @TypeConverter
    public String ObjectToString(List<Double> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Double> StringToObject(String str) {
        Gson gson = new Gson();
        List<Double> result;
        Double[] tmp = gson.fromJson(str, Double[].class);
        result = new ArrayList<Double>();
        if(tmp != null) Collections.addAll(result, tmp);
        return result;
    }
}