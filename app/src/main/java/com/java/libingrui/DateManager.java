package com.java.libingrui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    static public Date NewsStringToDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d M y HH:mm:ss z");
        Date date = null;
        try {
            date = sdf.parse(str);
        }
        catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }
}
