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
import java.lang.reflect.Type;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;

public class RemoteServiceManager {
    private final int NEWS_PER_PAGE;
    private final int PAGE_NUMBER;

    private final int TIMEOUT = 3000;

    RemoteServiceManager() {
        NEWS_PER_PAGE = 10;
        PAGE_NUMBER = 1;
    }

    RemoteServiceManager(final int NEWS_PER_PAGE, final int PAGE_NUMBER) {
        this.NEWS_PER_PAGE = NEWS_PER_PAGE;
        this.PAGE_NUMBER = PAGE_NUMBER;
    }

    public List<News> flushNews(String type) throws MyException{
        String base_url = "https://covid-dashboard.aminer.cn/api/events/list";
        List<News> result = new ArrayList<News>();
        if( type.equals("news") || type.equals("paper") || type.equals("event")) {
            String url = base_url + "?type=" + type + "&page=" + PAGE_NUMBER + "&size=" + NEWS_PER_PAGE;

            String json = remoteGET(url);
            if(json.length() > 0) {
                Gson gson = new Gson();
                API_EVENTS_LIST api_events_list = gson.fromJson(json, API_EVENTS_LIST.class);
                for (News news : api_events_list.data) {
                    news.selected = 0;
                }
                result.addAll(api_events_list.data);
            }
        }
        Log.v("debug", "from url get news size=" + result.size());
        return result;
    }

    public List<Person> flushPerson() throws MyException {
        String url = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
        List<Person> result = new ArrayList<>();

        String json = remoteGET(url);
        if(json.length() > 0) {
            Gson gson = new Gson();
            API_PERSON_LIST api_person_list = gson.fromJson(json, API_PERSON_LIST.class);
            for(Person person : api_person_list.data) {
                person.selected = 0;
            }
            result.addAll(api_person_list.data);
        }
        Log.v("debug", "from url get Person size = " + result.size());
        return result;
    }

    public Map<RegionName,DaysEpidemicData> getEpidemicData() throws MyException{
        String url = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
        String json = remoteGET(url);
        Map<RegionName, DaysEpidemicData> result = new HashMap<RegionName, DaysEpidemicData>();
        if( json != null) {
            Gson gson = new Gson();
            API_EPIDEMIC api_epidemic = new API_EPIDEMIC();
            Type type = new TypeToken<Map<String,API_DAYSEPIDEMICDATA>>(){}.getType();
           // Log.v("debug", "json=" + json);
            api_epidemic.item = gson.fromJson(json, type);
            for(Entry<String, API_DAYSEPIDEMICDATA> entry : api_epidemic.item.entrySet() ) {
             //   Log.v("debug",  "fuck");
                RegionName regionName = String2RegionName(entry.getKey());
                DaysEpidemicData data = API2DaysEpidemicData(entry.getValue());
                result.put(regionName, data);
            }
        }
        return result;
    }

    public News getNewsById(String id) throws MyException{
        String url = "https://covid-dashboard.aminer.cn/event/" + id;
        String json = remoteGET(url);
        if(json.length() > 0) {
            Gson gson = new Gson();
            API_GETNEWSBYID api_getnewsbyid = gson.fromJson(json, API_GETNEWSBYID.class);
            return api_getnewsbyid.data;
        }
        return null;
    }

    public EntityDetails getEntityByUrl(String url) throws MyException{
        String base_url = "https://covid-dashboard.aminer.cn/api/entity?url=" + url + "&time=0";
        String json = remoteGET(base_url);
        if(json.length() > 0) {
            Gson gson = new Gson();
            Log.v("debug", "json=" + json);
            API_GETENTITYBYURL api_getentitybyurl = gson.fromJson(json, API_GETENTITYBYURL.class);
            return api_getentitybyurl.data;
        }
        else {
            Log.v("debug", "json=null");
            return null;
        }
    }

    public List<EntityDetails> getEntitiesByKeyWord(String keyWord) throws MyException{
        String url = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=" + keyWord;
        String json = remoteGET(url);
        List<EntityDetails> list = new ArrayList<EntityDetails>();
        if(json.length() > 0) {
            Gson gson = new Gson();
            API_GETENTITIESBYKEYWORD api_getentitiesbykeyword = gson.fromJson(json, API_GETENTITIESBYKEYWORD.class);
            list.addAll(api_getentitiesbykeyword.data);
        }
        Log.v("debug", "keyword entity size=" + list.size());
        return list;
    }

    public Bitmap getBitmapByUrl(String url) {
        Bitmap bitmap = null;
        try {
            URL imageurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            conn.setConnectTimeout(TIMEOUT);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private RegionName String2RegionName(String str) {
        int len = str.length();
        List<String> strings = new ArrayList<String>();
        int last_pos = -1;
        for(int i = 0; i < len; ++i) {
            if(str.charAt(i) ==  '|') {
                strings.add(str.substring(last_pos + 1, i));
                last_pos = i;
            }
        }
        strings.add(str.substring(last_pos + 1, len));
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

    private String remoteGET(String path) throws MyException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.setConnectTimeout(TIMEOUT);
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
        throw new MyException("remoteGet " + path);
    }
}

class API_EVENTS_LIST {
    public List<News> data;
    public Pagination pagination;
    public boolean status;
}

class API_PERSON_LIST {
    public int status;
    public String message;
    public List<Person> data;
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
    public List<List<String>> data;
}

class API_GETNEWSBYID {
    public News data;
    public boolean status;
}

class API_GETENTITYBYURL {
    public EntityDetails data;
    public boolean status;
}

class API_GETENTITIESBYKEYWORD {
    public int code;
    public String msg;
    public List<EntityDetails> data;
}
