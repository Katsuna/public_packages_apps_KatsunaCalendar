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
package com.katsuna.calendar.grid_calendar;

import android.support.annotation.NonNull;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.days.DaysContract;
import com.katsuna.calendar.services.IEventsScheduler;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridCalendarPresenter implements GridCalendarContract.Presenter {
//    @NonNull
//    private final EventsDataSource mEventsDataSource;
//
    @NonNull
    private final GridCalendarContract.View mGridCalendarView;

//    @NonNull
//    private final IEventsScheduler mEventsScheduler;
    GridCalendarPresenter(@NonNull GridCalendarContract.View calendarView) {

        mGridCalendarView = calendarView;
        loadDays();
        mGridCalendarView.setPresenter(this);
        mGridCalendarView.setGridCalendarTitle(R.string.common_select_day);

    }

    @Override
    public void loadDays() {

    }

    @Override
    public void addOnDayNewEvent(@NonNull Day day) {
        mGridCalendarView.showAddEvent(day);
    }

    @Override
    public void start() {

    }

}
