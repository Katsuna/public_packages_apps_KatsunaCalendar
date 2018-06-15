package com.katsuna.calendar.data.source.converters;

import android.arch.persistence.room.TypeConverter;

import com.katsuna.calendar.data.EventStatus;


public class EventStatusConverter {

    @TypeConverter
    public static EventStatus toStatus(int status) {
        if (status == EventStatus.ACTIVE.getCode()) {
            return EventStatus.ACTIVE;
        } else if (status == EventStatus.INACTIVE.getCode()) {
            return EventStatus.INACTIVE;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toInteger(EventStatus status) {
        return status.getCode();
    }
}