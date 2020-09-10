package com.java.libingrui;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfEmail_uConverter {
    @TypeConverter
    public String ObjectToString(List<Email_u> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<com.java.libingrui.Email_u> StringToObject(String str) {
        Gson gson = new Gson();
        List<Email_u> result;
        Email_u[] tmp = gson.fromJson(str, Email_u[].class);
        result = new ArrayList<Email_u>();
        if(tmp != null) Collections.addAll(result, tmp);
        return result;
    }
}
