package com.java.libingrui;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

@Entity
public class NewsAbstract {
    @PrimaryKey
    public String id;

    public String type;
    public String title;
    public String category;
    public String time;
    public String lang;
    public GeoInfo geoInfo;
    public String influence;

    /**
     * Gereta json format info
     * @return json
     */
    public String toJson() {
        return new Gson().toJson(this);
    }

}
