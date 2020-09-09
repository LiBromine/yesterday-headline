package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@TypeConverters(ListOfEntityDataConverter.class)
public class EntityDataList {
    @PrimaryKey
    @NonNull
    public String type;
    public List<EntityData> list;

    public EntityDataList(@NonNull final String type) {
        this.type = type;
        list = new ArrayList<EntityData>();
    }

    public void add(EntityData data) {
        list.add(data);
    }
}

class ListOfEntityDataConverter {
    @TypeConverter
    public String ObjectToString(List<EntityData> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<EntityData> StringToObject(String str) {
        Gson gson = new Gson();
        List<EntityData> result;
        EntityData[] tmp = gson.fromJson(str, EntityData[].class);
        result = new ArrayList<EntityData>();
        if(tmp != null) Collections.addAll(result, tmp);
        return result;
    }
}
