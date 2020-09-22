/**
* Copyright (C) 2020 Manos Saratsis
*
* This file is part of Katsuna.
*
* Katsuna is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Katsuna is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Katsuna.  If not, see <https://www.gnu.org/licenses/>.
*/
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

        void showCalendarOnReturn(Event event);

        void showEventList();

        void setTime(String hour, String minute);

        void setHour(String hour);

        void setMinute(String minute);

        void loadEvent(Event event);

        void showValidationResults(List<ValidationResult> results);

        void showNextStepFab(boolean flag);

        void showPreviousStepFab(boolean flag);

        void showDescriptionControl(boolean flag);

        void showDescriptionStep(boolean flag);

        void setTitle(int resId);

        void setEventTimeTitle(int resId);

        void adjustFabPositions(ManageEventStep step);

        void showEventDayControl(boolean flag);

        void showEventTimeControl(boolean flag);

        void showEventOptionsControl(boolean flag);

        void setDefaultRingtone();

        void setDefaultVibrate();

        void hideKeyboard();
    }

    interface Presenter extends BasePresenter {

        void saveEvent(@NonNull EventType eventType, String description, String hour, String minute,String day, String month, String year , String ringtone, boolean vibrate);

        void populateEvent();

        boolean isDataMissing();

        void previousStep();

        void validateEventTypeInfo(EventType eventType, String description);

        void validateEventTime(String hour, String minute);

        ManageEventStep getCurrentStep();

        void eventTypeSelected(EventType eventType);

        void addHours(String hour, int hours);

        void addMinutes(String minute, int minutes);

        void showStep(ManageEventStep step);
    }

}
