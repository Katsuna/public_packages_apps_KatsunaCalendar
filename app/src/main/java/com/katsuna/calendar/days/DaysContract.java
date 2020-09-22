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

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;

import java.util.List;

public interface DaysContract {
    interface View extends BaseView<DaysContract.Presenter> {

        void showDays(List<Day> days);

        void showEvents(List<Event> events);

        void showAddEvent(Day day);

        void showEventDetailsUi(long eventId);

        void showNoEvents();

        void focusOnDay(Day day, boolean focus);

        void reloadDay(Day day);

        void showGridCalendar();

        void showDayDetails(Day day);
    }

    interface Presenter extends BasePresenter {

        void loadDays();

        void addOnGridCalendarNewEvent();

        void addNewEvent();

        void addOnDayNewEvent(@NonNull Day day);

        void openDayDetails(@NonNull Day day);

        void updateDayStatus(@NonNull Day day, @NonNull DayType dayType);

        void focusOnDay(@NonNull Day day, boolean focus);

        void updateEventStatus(@NonNull Event event, @NonNull EventStatus eventStatus);
    }

}
