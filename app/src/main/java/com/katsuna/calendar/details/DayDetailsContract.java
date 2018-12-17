package com.katsuna.calendar.details;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;

public interface DayDetailsContract {
    interface View extends BaseView<DayDetailsContract.Presenter> {

        void focusOnEvent(Event event, boolean focus);

        void showAddEvent(Day day);

        void showEventDetailsUi(long eventId);
    }

    interface Presenter extends BasePresenter {

        void loadEvents();

        void addNewEvent(Day day);

        void focusOnEvent(@NonNull Event event, boolean focus);

        void openEventDetails(Event event);

        void deleteEvent(Event event);

    }
}
