package com.katsuna.calendar.data.source;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Event;

import java.util.List;

public interface EventsDataSource {
    void getEvents(@NonNull LoadEventsCallback callback);

    void getEvent(long alarmId, @NonNull GetEventCallback callback);

    void saveEvent(@NonNull Event event);

    void deleteEvent(long eventId);

    interface LoadEventsCallback {

        void onEventsLoaded(List<Event> events);

        void onDataNotAvailable();
    }

    interface GetEventCallback {

        void onEventLoaded(Event event);

        void onDataNotAvailable();
    }
}
