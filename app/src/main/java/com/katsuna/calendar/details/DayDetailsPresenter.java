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
package com.katsuna.calendar.details;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.events.EventsContract;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DayDetailsPresenter implements DayDetailsContract.Presenter{

    @NonNull
    private final DayDetailsContract.View mDayDetailsView;
    @NonNull
    private final EventsDataSource mEventsDataSource;


    @NonNull
    private final IEventsScheduler mEventsScheduler;

    DayDetailsPresenter(@NonNull DayDetailsContract.View mDayDetailsView, EventsDataSource eventsDataSource, IEventsScheduler eventsScheduler) {
        this.mDayDetailsView = checkNotNull(mDayDetailsView, "eventsView cannot be null!");

        this.mDayDetailsView.setPresenter(this);
        mEventsDataSource = eventsDataSource;
        mEventsScheduler = eventsScheduler;
    }

    @Override
    public void loadEvents() {
        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
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

                mDayDetailsView.showEvents(events);
            }

            @Override
            public void onDataNotAvailable() {
                mDayDetailsView.showNoEvents();
            }
        });
    }

    @Override
    public void addNewEvent(Day day) {
        mDayDetailsView.showAddEvent(day);
    }

    @Override
    public void focusOnEvent(@NonNull Event event, boolean focus) {
        mDayDetailsView.focusOnEvent(event,focus);

    }

    @Override
    public void openEventDetails(Event event) {
        mDayDetailsView.showEventDetailsUi(event.getEventId());

    }

    @Override
    public void deleteEvent(Event event) {
        mEventsDataSource.deleteEvent(event.getEventId());
        mEventsScheduler.cancel(event);
        loadEvents();
//        mDayDetailsView.moveFabsToBottomAndTint(false);
    }

    @Override
    public void start() {

    }
}
