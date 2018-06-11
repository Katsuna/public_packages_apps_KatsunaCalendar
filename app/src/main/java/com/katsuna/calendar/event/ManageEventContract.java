package com.katsuna.calendar.event;

import android.support.annotation.NonNull;

import com.katsuna.calendar.BasePresenter;
import com.katsuna.calendar.BaseView;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.validators.ValidationResult;


import java.util.List;

class ManageEventContract {

    interface View extends BaseView<Presenter> {
        void showEmptyEventError();

        void showEventList();

        void setTime(String hour, String minute);

        void setHour(String hour);

        void setMinute(String minute);

        void loadEvent(Event event);

        void showValidationResults(List<ValidationResult> results);

        void showNextStepFab(boolean flag);

        void showPreviousStepFab(boolean flag);

        void showDescriptionControl(boolean flag);

        void adjustFabPositions(ManageEventStep step);
    }

    interface Presenter extends BasePresenter {

        void saveEvent(@NonNull EventType eventType, String description, String hour, String minute, String day );

        void populateEvent();

        boolean isDataMissing();

        void previousStep();

        void validateEventTypeInfo(EventType eventType, String description);

        void validateEventTime(String hour, String minute);

        ManageEventStep getCurrentStep();

        void eventTypeSelected(EventType eventType);

        void addHours(String hour, int hours);

        void addMinutes(String minute, int minutes);
    }

}
