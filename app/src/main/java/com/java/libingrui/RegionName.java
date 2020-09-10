package com.java.libingrui;

import androidx.room.ColumnInfo;

public class RegionName {
    @ColumnInfo(name = "country")
    public String country;
    public String province;
    public String county;
}
