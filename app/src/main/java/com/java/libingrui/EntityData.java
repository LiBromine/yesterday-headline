package com.java.libingrui;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"url"})
public class EntityData {
    @Embedded
    @NonNull
    public EntityDetails entityDetails;
    public byte[] bitmap;
}
