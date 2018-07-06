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
        if (mDay.getDayType() == DayType.CURRENT) {
            profileKey = ColorProfileKeyV2.PRIMARY_COLOR_1;
        } else if (mDay.getDayType() == DayType.WITH_EVENT) {
            profileKey = ColorProfileKeyV2.PRIMARY_COLOR_2;
        } else {
            profileKey = ColorProfileKeyV2.PRIMARY_GREY_1;
        }
        return ColorCalcV2.getColorResId(profileKey, profile.colorProfile);
    }


}
