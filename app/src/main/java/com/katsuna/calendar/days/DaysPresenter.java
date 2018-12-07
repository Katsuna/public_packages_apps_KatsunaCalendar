package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.util.EspressoIdlingResource;

import java.util.List;

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
        loadDays();
        mDaysView.setPresenter(this);
    }
    @Override
    public void loadDays() {

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

                mDaysView.showEvents(events);
            }

            @Override
            public void onDataNotAvailable(){
            }
        });

    }

    @Override
    public void addOnGridCalendarNewEvent() {
        mDaysView.showGridCalendar();
    }

    @Override
    public void addNewEvent() {
        mDaysView.showAddEvent(null);

    }

    @Override
    public void addOnDayNewEvent(@NonNull Day day) {
        mDaysView.showAddEvent(day);
    }

    @Override
    public void openDayDetails(@NonNull Day day) {
//TODO details
    }

    @Override
    public void updateDayStatus(@NonNull Day day, @NonNull DayType dayType) {

    }

    @Override
    public void updateEventStatus(@NonNull Event event, @NonNull EventStatus eventStatus) {
        event.setEventStatus(eventStatus);
        mEventsDataSource.saveEvent(event);
        mEventsScheduler.reschedule(event);
//        mEventsView.reloadEvent(event);
    }

    @Override
    public void focusOnDay(@NonNull Day day, boolean focus) {
        mDaysView.focusOnDay(day,focus);
    }

    @Override
    public void start() {

    }
}
