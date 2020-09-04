package com.java.libingrui;

import com.google.gson.Gson;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * Using this class to achieve TA's data
 * Actually, it is an URL reader
 */
public class RemoteServiceManager {

    /**
     * Get all news abstract, and return a list of NewsAbstract
     * USE API_2 : https://covid-dashboard.aminer.cn/api/dist/events.json
     * @return the list of NewsAbstract
     */
    public List<NewsAbstract> getAllNewsAbstract() {
        String url = "https://covid-dashboard.aminer.cn/api/dist/events.json";
        String json = loadJson(url);
        Gson gson = new Gson();
        GSON_AllNewsAbstract allNewsAbstract = gson.fromJson(json, GSON_AllNewsAbstract.class);

        List<NewsAbstract> newsAbstracts = new ArrayList<>();

        for(GSON_NewsAbstract gson_newsAbstract : allNewsAbstract.datas) {
            newsAbstracts.add(gson_newsAbstract.toNewsAbstract());
        }

        return newsAbstracts;
    }

    /**
     * load remote json by url
     * @param url
     * @return a string format json
     */
    private String loadJson(String url) {
        StringBuilder buffer = new StringBuilder();

        try{
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while( (inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * for GSON parser
     */
    class GSON_AllNewsAbstract {
        public String tflag;
        public List<GSON_NewsAbstract> datas;
    }

    /**
     * for GSON parser
     */
    class GSON_NewsAbstract {
        public String _id;
        public String type;
        public String title;
        public String category;
        public String time;
        public String lang;
        public List<GSON_geoInfo> geoInfo;
        public String influence;

        public NewsAbstract toNewsAbstract() {
            NewsAbstract newsAbstract = new NewsAbstract();
            newsAbstract.id = this._id;
            newsAbstract.type = this.type;
            newsAbstract.title = this.title;
            newsAbstract.category = this.category;
            newsAbstract.time = this.time;
            newsAbstract.lang = this.lang;
            newsAbstract.influence = this.influence;
            if(geoInfo.size() > 0) {
                newsAbstract.geoInfo = this.geoInfo.get(0).toGeoInfo();
            }
            else {
                newsAbstract.geoInfo = null;
            }
        }
    }

    /**
     * for GSON parser
     */
    class GSON_geoInfo {
        public String originText;
        public String geoName;
        public String latitude;
        public String longitude;

        public GeoInfo toGeoInfo() {
            GeoInfo geoInfo = new GeoInfo();
            geoInfo.originText = this.originText;
            geoInfo.geoName = this.geoName;
            geoInfo.latitude = this.latitude;
            geoInfo.longitude = this.longitude;
            return geoInfo;
        }
    }
}
