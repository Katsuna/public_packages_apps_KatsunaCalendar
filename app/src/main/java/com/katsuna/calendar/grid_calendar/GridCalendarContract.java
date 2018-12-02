package com.katsuna.calendar.grid_calendar;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Day;

class GridCalendarContract {
    interface View extends BaseView<Presenter> {

        void showAddEvent(Day day);

    }

    interface Presenter extends BasePresenter {

        void loadDays();

        void addOnDayNewEvent(@NonNull Day day);

    }
}
