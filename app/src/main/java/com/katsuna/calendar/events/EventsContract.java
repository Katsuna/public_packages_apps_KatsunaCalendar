package com.katsuna.calendar.events;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;


import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface EventsContract {

    interface View extends BaseView<Presenter> {

        void showEvents(List<Event> events);

        void showAddEvent();

        void showEventDetailsUi(long eventId);

        void showNoEvents();

        void focusOnEvent(Event event, boolean focus);

        void reloadEvent(Event event);
    }

    interface Presenter extends BasePresenter {

        void loadEvents();

        void addNewEvent();

        void openEventDetails(@NonNull Event alarm);

        void deleteEvent(@NonNull Event alarm);

        void updateEventStatus(@NonNull Event alarm, @NonNull EventStatus alarmStatus);

        void focusOnEvent(@NonNull Event alarm, boolean focus);
    }
}
