package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.services.IEventsScheduler;

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

        mDaysView.setPresenter(this);
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
    public void updateDayStatus(@NonNull Day day, @NonNull DayType dayType) {

    }

    @Override
    public void focusOnEvent(@NonNull Day day, boolean focus) {

    }

    @Override
    public void start() {

    }
}
