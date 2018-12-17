package com.katsuna.calendar.details;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.events.EventsContract;
import com.katsuna.calendar.services.IEventsScheduler;

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
    }

    @Override
    public void start() {

    }
}
