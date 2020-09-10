package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

@Entity
@TypeConverters({
        ListOfStringConverter.class
})
public class StringList {
    @PrimaryKey
    @NonNull
    public String type;

    public List<String> nameList;

    public StringList(@NonNull String type) {
        this.nameList = new ArrayList<>();
        this.type = type;
    }
}

