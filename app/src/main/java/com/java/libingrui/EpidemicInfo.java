package com.java.libingrui;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EpidemicInfo {
    @PrimaryKey(autoGenerate = true)
    public int _id;

    @Embedded
    public RegionName region;

    @Embedded
    public OneDayEpidemicData data;

    @Embedded
    public Day day;

    public int selected;
}
