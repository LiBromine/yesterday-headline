package com.java.libingrui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

import com.google.gson.Gson;

import android.util.Log;

public class RemoteServiceManager {
    private final int NEWS_PER_PAGE;

    RemoteServiceManager(final int NEWS_PER_PAGE) {
        this.NEWS_PER_PAGE = NEWS_PER_PAGE;
    }

    public List<News> flushNews(String type) {
        String base_url = "https://covid-dashboard.aminer.cn/api/events/list";
        List<News> result = new ArrayList<News>();
        if( type.equals("news")) {
            String url = base_url + "?type=" + type + "&page=1" + "&size=" + NEWS_PER_PAGE;

            String json = remoteGET(url);
            if( json != null) {
                Gson gson = new Gson();
                API_EVENTS_LIST api_events_list = gson.fromJson(json, API_EVENTS_LIST.class);

                Pagination pagination = api_events_list.pagination;
                int NUMBER_OF_PAGE = (pagination.total + NEWS_PER_PAGE - 1) / NEWS_PER_PAGE;
                result.addAll(api_events_list.data);
                //TODO modify here, change NUMBER_OF_PAGE to 4 for debug
                for(int i = 2; i <= 4; ++i) {
                    url = base_url + "?type=" + type + "&page=" + i + "&size=" + NEWS_PER_PAGE;
                    json = remoteGET(url);
                    api_events_list = gson.fromJson(json, API_EVENTS_LIST.class);
                    result.addAll(api_events_list.data);
                }
            }
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