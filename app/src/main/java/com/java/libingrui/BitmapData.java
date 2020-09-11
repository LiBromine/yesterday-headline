package com.java.libingrui;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BitmapData {
    @PrimaryKey
    @NonNull
    public String url;

    public byte[] bitmap;
    public int height;
    public int width;
    public String name;

    public int selected;
}
