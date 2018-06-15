package com.katsuna.calendar.formatters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.commons.entities.ColorProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorCalcV2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventFormatter {
    private final Event mEvent;
    private final Context mContext;

    public EventFormatter(@NonNull Context context, @NonNull Event event) {
        mContext = context;
        mEvent = event;
    }





    public int getEventTypeIconResId() {
        int output;
        if (mEvent.getEventType() == EventType.REMINDER) {
            output = R.drawable.ic_notifications_24dp;
        } else {
            output = R.drawable.ic_access_time_24dp;
        }
        return output;
    }

    public String getTitle() {
        String output;
        if (mEvent.getEventType() == EventType.ALARM) {
            output = showTime();
        } else {
            output = mEvent.getDescription();
        }
        return output;
    }

    public String getDescription() {
        String output;
        if (mEvent.getEventType() == EventType.ALARM) {
            output = getString(R.string.event);
        } else {
            output = showTime();
        }
        return output;
    }

    private String getString(int resId) {
        return mContext.getString(resId);
    }

    private String showTime() {
        return String.format(Locale.getDefault(), "%02d:%02d", mEvent.getHour(), mEvent.getMinute());
    }

    public int getCardHandleColor(UserProfile profile) {
        ColorProfileKeyV2 profileKey;
        if (mEvent.getEventStatus() == EventStatus.INACTIVE) {
            profileKey = ColorProfileKeyV2.PRIMARY_GREY_1;
        } else if (mEvent.getEventType() == EventType.ALARM) {
            profileKey = ColorProfileKeyV2.PRIMARY_COLOR_2;
        } else {
            profileKey = ColorProfileKeyV2.PRIMARY_COLOR_1;
        }
        return ColorCalcV2.getColorResId(profileKey, profile.colorProfile);
    }

    public int getCardInnerColor(UserProfile profile) {
        ColorProfileKeyV2 profileKey;
        if (mEvent.getEventStatus() == EventStatus.INACTIVE) {
            profileKey = ColorProfileKeyV2.SECONDARY_GREY_2;
        } else if (mEvent.getEventType() == EventType.ALARM) {
            profileKey = ColorProfileKeyV2.SECONDARY_COLOR_2;
        } else {
            profileKey = ColorProfileKeyV2.SECONDARY_COLOR_1;
        }
        return ColorCalcV2.getColorResId(profileKey, profile.colorProfile);
    }
}
