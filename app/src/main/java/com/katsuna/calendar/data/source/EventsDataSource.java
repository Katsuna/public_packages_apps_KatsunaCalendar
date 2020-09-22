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

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Event;

import java.util.List;

public interface EventsDataSource {
    void getEvents(@NonNull LoadEventsCallback callback);

    void getEvent(long eventId, @NonNull GetEventCallback callback);

    Event getEvent(long eventId);

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
