package com.katsuna.calendar;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class CalendarApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
    }
}
