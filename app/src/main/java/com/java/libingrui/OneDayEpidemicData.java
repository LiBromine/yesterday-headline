package com.java.libingrui;

import java.util.List;

public class OneDayEpidemicData {
    public int CONFIRMED;
    public int SUSPECTED;
    public int CURED;
    public int DEAD;
    public int SEVERE;
    public int RISK;
    public int inc24;

    public OneDayEpidemicData() {}

    public OneDayEpidemicData(List<String> data) {
        if( data.get(0)!= null) CONFIRMED = Integer.parseInt(data.get(0));
        else CONFIRMED = 0;
        if( data.get(1)!= null) SUSPECTED = Integer.parseInt(data.get(1));
        else SUSPECTED = 0;
        if( data.get(2)!= null) CURED = Integer.parseInt(data.get(2));
        else CURED = 0;
        if( data.get(3)!= null) DEAD = Integer.parseInt(data.get(3));
        else DEAD = 0;
        if( data.get(4)!= null) SEVERE = Integer.parseInt(data.get(4));
        else SEVERE = 0;
        if( data.get(5)!= null) RISK = Integer.parseInt(data.get(5));
        else RISK = 0;
        if( data.get(6)!= null) inc24 = Integer.parseInt(data.get(6));
        else inc24 = 0;
    }
}
