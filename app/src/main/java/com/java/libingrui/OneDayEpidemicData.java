package com.java.libingrui;

public class OneDayEpidemicData {
    public int CONFIRMED;
    public int SUSPECTED;
    public int CURED;
    public int DEAD;
    public int SEVERE;
    public int RISK;
    public int inc24;

    public OneDayEpidemicData() {}

    public OneDayEpidemicData(int[] data) {
        CONFIRMED = data[0];
        SUSPECTED = data[1];
        CURED = data[2];
        DEAD = data[3];
        SEVERE = data[4];
        RISK = data[5];
        inc24 = data[6];
    }
}
