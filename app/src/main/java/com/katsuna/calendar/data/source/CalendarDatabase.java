package com.katsuna.calendar.data.source;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.source.converters.EventStatusConverter;
import com.katsuna.calendar.data.source.converters.EventTypeConverter;

@Database(entities = {Event.class}, version = 5)
@TypeConverters({EventTypeConverter.class, EventStatusConverter.class})
public abstract class CalendarDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private static CalendarDatabase INSTANCE;

    public static CalendarDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CalendarDatabase.class, "events.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract EventsDao eventsDao();
}
