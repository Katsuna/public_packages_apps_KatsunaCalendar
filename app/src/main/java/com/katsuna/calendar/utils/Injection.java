package com.katsuna.calendar.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.katsuna.calendar.services.EventsScheduler;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.CalendarDatabase;
import com.katsuna.calendar.data.source.DatabaseInjection;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.data.source.EventsLocalDataSource;
import com.katsuna.calendar.validators.EventValidator;
import com.katsuna.calendar.validators.IEventValidator;

public class Injection {

    public static EventsDataSource provideEventsDataSource(@NonNull Context context) {
        CalendarDatabase database = DatabaseInjection.getDatabase(context);
        return EventsLocalDataSource.getInstance(new AppExecutors(), database.eventsDao());
    }

    public static IEventValidator provideEventValidator() {
        return new EventValidator();
    }

    public static IEventsScheduler provideEventScheduler(@NonNull Context context) {
        return new EventsScheduler(context, provideEventsDataSource(context));
    }
}