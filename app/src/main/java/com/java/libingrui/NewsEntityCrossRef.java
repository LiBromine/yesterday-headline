package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsEntityCrossRef {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    @NonNull
    public String url;
    @NonNull
    public String news_id;
}
