package com.katsuna.calendar.details;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;

import static com.google.common.base.Preconditions.checkNotNull;

public class DayDetailsPresenter implements DayDetailsContract.Presenter{

    @NonNull
    private final DayDetailsContract.View mDayDetailsView;

    DayDetailsPresenter(@NonNull DayDetailsContract.View mDayDetailsView) {
        this.mDayDetailsView = checkNotNull(mDayDetailsView, "eventsView cannot be null!");

        this.mDayDetailsView.setPresenter(this);
    }

    @Override
    public void loadEvents() {

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
    public void start() {

    }
}
