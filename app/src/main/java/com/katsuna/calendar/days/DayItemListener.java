package com.katsuna.calendar.days;

import android.support.annotation.NonNull;

import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;


interface DayItemListener {

    void onDayFocus(@NonNull Day day, boolean focus);

    void onDayEdit(@NonNull Day day);

    void onDayTypeUpdate(@NonNull Day alarm, @NonNull DayType dayType);

    void onDayDelete(@NonNull Day day);
}