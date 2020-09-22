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
package com.katsuna.calendar.formatters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;
import com.katsuna.commons.entities.ColorProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorCalcV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DayFormatter {
    private final Day mDay;
//    private List<Event> dayEvents;

    private final Context mContext;

    public DayFormatter(@NonNull Context context, @NonNull Day day) {
        mContext = context;
        mDay = day;
    }

    public String getName() {
        List<String> days = new ArrayList<>();


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.size(); i++) {
            sb.append(days.get(i));
            if (days.size() > (i+2)) {
                // we have at least two more to add
                sb.append(", ");
            } else if (days.size() > (i+1)) {
                // we have one more to add
                sb.append(" ").append("").append(" ");
            }
        }

        return sb.toString();
    }



    public String getDescription() {
        String output ="";

        return output;
    }

    private String getString(int resId) {
        return mContext.getString(resId);
    }

    private String showEventTime(Event event) {

        return String.format(Locale.getDefault(), "%02d:%02d", event.getHour(), event.getMinute());
    }

    public int getCardHandleColor(UserProfile profile) {
        ColorProfileKeyV2 profileKey;
        if (mDay.getDayType() == DayType.CURRENT || mDay.getDayType() == DayType.CURRENT_WITH_EVENT) {
            profileKey = ColorProfileKeyV2.PRIMARY_COLOR_1;
        } else if (mDay.getDayType() == DayType.WITH_EVENT) {
            profileKey = ColorProfileKeyV2.PRIMARY_COLOR_2;
        } else {
            profileKey = ColorProfileKeyV2.PRIMARY_GREY_1;
        }
        return ColorCalcV2.getColorResId(profileKey, profile.colorProfile);
    }

    public int getCardInnerColor(UserProfile profile) {
        ColorProfileKeyV2 profileKey;
        if (mDay.getDayType() == DayType.CURRENT || mDay.getDayType() == DayType.CURRENT_WITH_EVENT) {
            profileKey = ColorProfileKeyV2.SECONDARY_COLOR_1;
        } else if (mDay.getDayType() == DayType.WITH_EVENT) {
            profileKey = ColorProfileKeyV2.SECONDARY_COLOR_2;
        } else {
            profileKey = ColorProfileKeyV2.SECONDARY_GREY_2;
        }
        return ColorCalcV2.getColorResId(profileKey, profile.colorProfile);
    }

}
