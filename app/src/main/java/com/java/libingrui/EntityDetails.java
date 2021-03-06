package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity
@TypeConverters(ListOfStringConverter.class)
public class EntityDetails {
    @Embedded
    public EntityAbstractInfo abstractInfo;
    public String hot;
    public String img;
    public String label;
    public List<String> related_events;
    public String source;
    @NonNull
    public String url;
}
