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
package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DaysPresenter implements DaysContract.Presenter {
    @NonNull
    private final EventsDataSource mEventsDataSource;

    @NonNull
    private final DaysContract.View mDaysView;

    @NonNull
    private final IEventsScheduler mEventsScheduler;



    DaysPresenter(@NonNull EventsDataSource eventsDataSource,
                    @NonNull DaysContract.View eventsView,
                    @NonNull IEventsScheduler eventsScheduler) {
        mEventsDataSource = checkNotNull(eventsDataSource, "dataSource cannot be null");
        mDaysView = checkNotNull(eventsView, "eventsView cannot be null!");
        mEventsScheduler = checkNotNull(eventsScheduler, "eventsScheduler cannot be null!");
//        loadDays();
        mDaysView.setPresenter(this);
    }
    @Override
    public void loadDays() {

        EspressoIdlingResource.increment(); // App is busy until further notice

        mEventsDataSource.getEvents(new EventsDataSource.LoadEventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {
                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }
//                System.out.println("EVENTS ARE :"+events.size());
                mDaysView.showEvents(events);
            }

            @Override
            public void onDataNotAvailable(){
            }
        });

    }

    @Override
    public void addOnGridCalendarNewEvent() {
        mDaysView.showGridCalendar();
    }

    @Override
    public void addNewEvent() {
        mDaysView.showAddEvent(null);

    }

    @Override
    public void addOnDayNewEvent(@NonNull Day day) {
        mDaysView.showAddEvent(day);
    }

    @Override
    public void openDayDetails(@NonNull Day day) {
        mDaysView.showDayDetails(day);
    }

    @Override
    public void updateDayStatus(@NonNull Day day, @NonNull DayType dayType) {

    }

    @Override
    public void updateEventStatus(@NonNull Event event, @NonNull EventStatus eventStatus) {
        event.setEventStatus(eventStatus);
        mEventsDataSource.saveEvent(event);
        mEventsScheduler.reschedule(event);
//        mEventsView.reloadEvent(event);
    }

    @Override
    public void focusOnDay(@NonNull Day day, boolean focus) {
        mDaysView.focusOnDay(day,focus);
    }

    @Override
    public void start() {
        loadDays();
    }
}
