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
package com.katsuna.calendar.events;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;


import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface EventsContract {

    interface View extends BaseView<Presenter> {

        void showEvents(List<Event> events);

        void showAddEvent();

        void showEventDetailsUi(long eventId);

        void showNoEvents();

        void focusOnEvent(Event event, boolean focus);

        void reloadEvent(Event event);
    }

    interface Presenter extends BasePresenter {

        void loadEvents();

        void addNewEvent();

        void openEventDetails(@NonNull Event alarm);

        void deleteEvent(@NonNull Event alarm);

        void updateEventStatus(@NonNull Event alarm, @NonNull EventStatus alarmStatus);

        void focusOnEvent(@NonNull Event alarm, boolean focus);
    }
}
