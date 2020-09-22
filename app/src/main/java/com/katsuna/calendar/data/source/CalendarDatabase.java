/**
* Copyright (C) 2020 Manos Saratsis
*
* This file is part of Katsuna.
*
* Katsuna is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Katsuna is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Katsuna.  If not, see <https://www.gnu.org/licenses/>.
*/
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
