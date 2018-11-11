package com.katsuna.calendar.events;

import android.support.annotation.NonNull;


import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link EventsActivity}), retrieves the data and updates the
 * UI as required.
 */
public class EventsPresenter implements EventsContract.Presenter {

    @NonNull
    private final EventsDataSource mEventsDataSource;

    @NonNull
    private final EventsContract.View mEventsView;

    @NonNull
    private final IEventsScheduler mEventsScheduler;

    public EventsPresenter(@NonNull EventsDataSource eventsDataSource,
                    @NonNull EventsContract.View eventsView,
                    @NonNull IEventsScheduler eventsScheduler) {
        mEventsDataSource = checkNotNull(eventsDataSource, "dataSource cannot be null");
        mEventsView = checkNotNull(eventsView, "eventsView cannot be null!");
        mEventsScheduler = checkNotNull(eventsScheduler, "eventsScheduler cannot be null!");

        mEventsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadEvents();
    }

    @Override
    public void loadEvents() {
        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
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

                mEventsView.showEvents(events);
            }

            @Override
            public void onDataNotAvailable() {
                mEventsView.showNoEvents();
            }
        });
    }

    public void addNewEvent() {
        mEventsView.showAddEvent();
    }

    @Override
    public void openEventDetails(@NonNull Event requestedEvent) {
        mEventsView.showEventDetailsUi(requestedEvent.getEventId());
    }

    @Override
    public void deleteEvent(@NonNull Event event) {
        mEventsDataSource.deleteEvent(event.getEventId());
        mEventsScheduler.cancel(event);
        loadEvents();
    }

    @Override
    public void updateEventStatus(@NonNull Event event, @NonNull EventStatus eventStatus) {
        event.setEventStatus(eventStatus);
        mEventsDataSource.saveEvent(event);
        mEventsScheduler.reschedule(event);
        mEventsView.reloadEvent(event);
    }

    @Override
    public void focusOnEvent(@NonNull Event event, boolean focus) {
        mEventsView.focusOnEvent(event, focus);
    }
}
