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
