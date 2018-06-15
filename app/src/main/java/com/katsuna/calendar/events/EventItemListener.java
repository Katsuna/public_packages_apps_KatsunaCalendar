package com.katsuna.calendar.events;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;


interface EventItemListener {

    void onEventFocus(@NonNull Event alarm, boolean focus);

    void onEventEdit(@NonNull Event alarm);

    void onEventStatusUpdate(@NonNull Event alarm, @NonNull EventStatus alarmStatus);

    void onEventDelete(@NonNull Event alarm);
}