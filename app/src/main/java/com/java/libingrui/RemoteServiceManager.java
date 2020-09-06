package com.java.libingrui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import android.util.Log;

public class RemoteServiceManager {
    private final int NEWS_PER_PAGE;
    private final int PAGE_NUMBER;

    RemoteServiceManager() {
        NEWS_PER_PAGE = 10;
        PAGE_NUMBER = 1;
    }

    RemoteServiceManager(final int NEWS_PER_PAGE, final int PAGE_NUMBER) {
        this.NEWS_PER_PAGE = NEWS_PER_PAGE;
        this.PAGE_NUMBER = PAGE_NUMBER;
    }

    public List<News> flushNews(String type) {
        String base_url = "https://covid-dashboard.aminer.cn/api/events/list";
        List<News> result = new ArrayList<News>();
        if( type.equals("news") || type.equals("paper")) {
            String url = base_url + "?type=" + type + "&page=" + PAGE_NUMBER + "&size=" + NEWS_PER_PAGE;

            String json = remoteGET(url);
            if( json != null) {
                Gson gson = new Gson();
                API_EVENTS_LIST api_events_list = gson.fromJson(json, API_EVENTS_LIST.class);
                for(News news : api_events_list.data) {
                    news.selected = 0;
                }

                result.addAll(api_events_list.data);
            }
        }
        Log.v("debug", "from url get size=" + result.size());
        return result;
    }

    public Map<RegionName,DaysEpidemicData> getEpidemicData() {
        String url = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
        String json = remoteGET(url);
        Map<RegionName, DaysEpidemicData> result = new HashMap<RegionName, DaysEpidemicData>();
        if( json != null) {
            Gson gson = new Gson();
            API_EPIDEMIC api_epidemic = new API_EPIDEMIC();
            api_epidemic.item = gson.fromJson(json, api_epidemic.item.getClass());
            for(Entry<String, API_DAYSEPIDEMICDATA> entry : api_epidemic.item.entrySet() ) {
                RegionName regionName = String2RegionName(entry.getKey());
                DaysEpidemicData data = API2DaysEpidemicData(entry.getValue());
                result.put(regionName, data);
            }
        }
        else {
            Log.v("debug", "network error when get Epidemic data");
            //@TODO change log file to throw Exception
        }
        return result;
    }

    private RegionName String2RegionName(String str) {
        int len = str.length();
        List<String> strings = new ArrayList<String>();
        int last_pos = -1;
        for(int i = 0; i < len; ++i) {
            if(str.indexOf(i) == '|') {
                strings.add(str.substring(last_pos + 1, i));
                last_pos = i;
            }
        }
        RegionName result = new RegionName();
        if(strings.size() >= 1) result.country = strings.get(0);
        else result.country = "total";
        if(strings.size() >= 2) result.province = strings.get(1);
        else result.province = "total";
        if(strings.size() >= 3) result.county = strings.get(2);
        else result.county = "total";
        return result;
    }

    private DaysEpidemicData API2DaysEpidemicData(API_DAYSEPIDEMICDATA data) {
        DaysEpidemicData result = new DaysEpidemicData();
        result.begin = data.begin;
        result.data = new ArrayList<OneDayEpidemicData>();
        for(int i = 0; i < data.data.size(); ++i) {
            result.data.add(new OneDayEpidemicData(data.data.get(i)));
        }
        return result;
    }

    private String remoteGET(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            StringBuilder buffer = new StringBuilder();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));
                String inputLine = null;
                while ((inputLine = bin.readLine()) != null) {
                    buffer.append(inputLine);
                }
                bin.close();
                in.close();
            }
            conn.disconnect();
            return buffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

class API_EVENTS_LIST {
    public List<News> data;
    public Pagination pagination;
    public boolean status;
}

class Pagination {
    public int page;
    public int size;
    public int total;
}

class API_EPIDEMIC {
    Map<String, API_DAYSEPIDEMICDATA> item;
}

class API_DAYSEPIDEMICDATA {
    public String begin;
    public List<int[]> data;
}