package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;

import java.util.List;

public interface DaysContract {
    interface View extends BaseView<DaysContract.Presenter> {

        void showDays(List<Day> days);

        void showAddEvent();

        void showEventDetailsUi(long eventId);

        void showNoEvents();

        void focusOnDay(Day day, boolean focus);

        void reloadDay(Day day);
    }

    interface Presenter extends BasePresenter {

        void loadDays();

        void addNewEvent();

        void openDayDetails(@NonNull Day day);

        void updateDayStatus(@NonNull Day day, @NonNull DayType dayType);

        void focusOnDay(@NonNull Day day, boolean focus);
    }

}
