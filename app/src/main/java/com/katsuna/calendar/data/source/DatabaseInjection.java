package com.katsuna.calendar.data.source;

import android.content.Context;

public class DatabaseInjection {
    public static CalendarDatabase getDatabase(Context context) {
        return CalendarDatabase.getInstance(context);
    }
}
