package com.java.libingrui;

import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Entity
@TypeConverters({
        MapOfPropertiesConverter.class,
        ListOfEntityRelationConverter.class
})
public class EntityCovidInfo {
    Map<String,String> properties;
    List<EntityRelation> relations;
}

class ListOfEntityRelationConverter {
    @TypeConverter
    public String ObjectToString(List<EntityRelation> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<EntityRelation> StringToObject(String str) {
        Gson gson = new Gson();
        List<EntityRelation> result;
        EntityRelation[] tmp = gson.fromJson(str, EntityRelation[].class);
        result = new ArrayList<EntityRelation>();
        Collections.addAll(result, tmp);
        return result;
    }
}

class MapOfPropertiesConverter {
    @TypeConverter
    public String ObjectToString(Map<String,String> properties) {
        Gson gson = new Gson();
        return gson.toJson(properties);
    }

    @TypeConverter
    public Map<String,String> StringToObject(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,String>>(){}.getType();
        return gson.fromJson(json, type);
    }
}