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
@TypeConverters({
        ListOfPersonConverter.class
})
public class PersonList {
    @PrimaryKey
    @NonNull
    public String type;

    public List<Person> list;

    PersonList(@NonNull String type) {
        this.type = type;
        list = new ArrayList<>();
    }

}

class ListOfPersonConverter {
    @TypeConverter
    public String ObjectToString(List<Person> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Person> StringToObject(String json) {
        Gson gson = new Gson();
        List<Person> result;
        Person[] tmp = gson.fromJson(json, Person[].class);
        result = new ArrayList<Person>();
        Collections.addAll(result, tmp);
        return result;
    }
}
