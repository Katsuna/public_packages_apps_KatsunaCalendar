package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;

import java.util.List;

public interface DaysContract {
    interface View extends BaseView<DaysContract.Presenter> {

        void showDays(List<Day> days);

        void showEvents(List<Event> events);

        void showAddEvent(Day day);

        void showEventDetailsUi(long eventId);

        void showNoEvents();

        void focusOnDay(Day day, boolean focus);

        void reloadDay(Day day);
    }

    interface Presenter extends BasePresenter {

        void loadDays();

        void addNewEvent();

        void addOnDayNewEvent(@NonNull Day day);

        void openDayDetails(@NonNull Day day);

        void updateDayStatus(@NonNull Day day, @NonNull DayType dayType);

        void focusOnDay(@NonNull Day day, boolean focus);

        void updateEventStatus(@NonNull Event event, @NonNull EventStatus eventStatus);
    }

}
