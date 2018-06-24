package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.services.IEventsScheduler;

import static com.google.common.base.Preconditions.checkNotNull;

public class DaysPresenter implements DaysContract.Presenter {


    DaysPresenter(@NonNull EventsDataSource eventsDataSource,
                    @NonNull EventsContract.View eventsView,
                    @NonNull IEventsScheduler eventsScheduler) {
        mEventsDataSource = checkNotNull(eventsDataSource, "dataSource cannot be null");
        mDaysView = checkNotNull(eventsView, "eventsView cannot be null!");
        mEventsScheduler = checkNotNull(eventsScheduler, "eventsScheduler cannot be null!");

        mEventsView.setPresenter(this);
    }
    @Override
    public void loadDays() {

    }

    @Override
    public void addNewEvent() {

    }

    @Override
    public void openDayDetails(@NonNull Day day) {

    }

    @Override
    public void updateDayStatus(@NonNull Event event, @NonNull EventStatus eventStatus) {

    }

    @Override
    public void focusOnEvent(@NonNull Event event, boolean focus) {

    }

    @Override
    public void start() {

    }
}
