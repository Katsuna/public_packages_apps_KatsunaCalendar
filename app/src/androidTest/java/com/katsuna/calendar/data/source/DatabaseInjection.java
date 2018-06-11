package com.katsuna.calendar.data.source;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseInjection {

    public static CalendarDatabase getDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context, CalendarDatabase.class).build();
    }
}
