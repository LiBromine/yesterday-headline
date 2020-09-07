package com.java.libingrui;

import android.graphics.Bitmap;

import androidx.room.Embedded;
import androidx.room.Entity;

@Entity
public class EntityData {
    @Embedded
    public EntityDetails entityDetails;
    public Bitmap bitmap;
}
