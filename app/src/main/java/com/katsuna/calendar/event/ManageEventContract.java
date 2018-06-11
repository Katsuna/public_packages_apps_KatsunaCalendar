package com.katsuna.calendar.event;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventType;


import java.util.List;

class ManageEventContract {

    interface View extends BaseView<ManageEventContract.Presenter> {
        void showEmptyAlarmError();

        void showAlarmsList();

        void setTime(String hour, String minute);

        void setHour(String hour);

        void setMinute(String minute);

        void loadAlarm(Event event);

        void showValidationResults(List<ValidationResult> results);

        void showNextStepFab(boolean flag);

        void showPreviousStepFab(boolean flag);

        void showDescriptionControl(boolean flag);

        void showAlarmTypeControl(boolean flag);

        void showAlarmTimeControl(boolean flag);

        void showAlarmDaysControl(boolean flag);

        void adjustFabPositions(ManageEventStep step);
    }

    interface Presenter extends BasePresenter {

        void saveEvent(@NonNull EventType eventType, String description, String hour, String minute, String day );

        void populateAlarm();

        boolean isDataMissing();

        void previousStep();

        void validateAlarmTypeInfo(EventType eventType, String description);

        void validateAlarmTime(String hour, String minute);

        ManageEventStep getCurrentStep();

        void alarmTypeSelected(EventType alarmType);

        void addHours(String hour, int hours);

        void addMinutes(String minute, int minutes);
    }

}
