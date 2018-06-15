package com.katsuna.calendar.data.source.converters;

import android.arch.persistence.room.TypeConverter;

import com.katsuna.calendar.data.EventType;


public class EventTypeConverter {

    @TypeConverter
    public static EventType toType(int type) {
        if (type == EventType.ALARM.getCode()) {
            return EventType.ALARM;
        } else if (type == EventType.REMINDER.getCode()) {
            return EventType.REMINDER;
        } else {
            throw new IllegalArgumentException("Could not recognize type");
        }
    }

    @TypeConverter
    public static int toInteger(EventType type) {
        return type.getCode();
    }
}