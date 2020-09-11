package com.java.libingrui;

public class NewsClusterManager {

    private int type_cnt;

    NewsClusterManager() {
        //TODO get real type count here
        type_cnt = 5;
    }

    public int getTypeByPos(int pos) {
        //TODO get real type[pos] here
        return pos % 5;
    }

    public String getKeywordsByType(int type) {
        //TODO get real keywords[type] here
        return "This is news cluster type " + type;
    }

    public int getType_cnt() { return type_cnt;}
}
