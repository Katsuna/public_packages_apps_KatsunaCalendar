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
package com.katsuna.calendar.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.katsuna.calendar.services.EventsScheduler;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.CalendarDatabase;
import com.katsuna.calendar.data.source.DatabaseInjection;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.data.source.EventsLocalDataSource;
import com.katsuna.calendar.services.NextEventCalculator;
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
        return new EventsScheduler(context, provideEventsDataSource(context), new NextEventCalculator());
    }
}