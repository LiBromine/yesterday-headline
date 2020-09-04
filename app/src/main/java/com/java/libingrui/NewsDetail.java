package com.java.libingrui;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsDetail {
    @PrimaryKey
    public String id;

    public String category;
    public String content;
    public String date;
}
