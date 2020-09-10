package com.java.libingrui;

import android.provider.ContactsContract;

public class Day {
    public Day(){}

    /**
     * Parse String to Day
     * date is Like YYYY-MM-DD
     * @param date
     */
    public Day(String date) {
        year = Integer.parseInt(date.substring(0, 4));
        month = Integer.parseInt(date.substring(5, 7));
        this.date = Integer.parseInt(date.substring(8, 10));
        calcTimeValue();
    }

    public void calcTimeValue() {
        timeValue = year * 10000 + month * 100 + this.date;
    }

    public void addOne() {
        if(hitMonthLimit(month,date)) {
            date = 1;
            if(hitYearLimit(year,month)) {
                month = 1;
                year += 1;
            }
            else {
                month += 1;
            }
        }
        else {
            date += 1;
        }

        timeValue = year * 10000 + month * 100 + this.date;
    }

    private boolean hitMonthLimit(int month, int date) {
        int[] limit = new int[13];
        limit[1] = 31;
        limit[2] = 29;
        limit[3] = 31;
        limit[4] = 30;
        limit[5] = 31;
        limit[6] = 30;
        limit[7] = 31;
        limit[8] = 31;
        limit[9] = 30;
        limit[10] = 31;
        limit[11] = 30;
        limit[12] = 31;
        return date == limit[month];
    }

    private boolean hitYearLimit(int year, int month) {
        return month == 12;
    }

    public int year;
    public int month;
    public int date;

    public int timeValue;

}

