package com.katsuna.calendar.services;


import com.katsuna.calendar.data.Event;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.List;

public class NextEventCalculator implements INextEventCalculator {

    @Override
    public LocalDateTime getTriggerDateTime(LocalDateTime now, Event event) {
        LocalDateTime output;


        output = LocalDateTime.of(now.toLocalDate(),
                LocalTime.of(event.getHour(), event.getMinute()));
        if (output.isBefore(now) || output.isEqual(now)) {
            output = output.plusDays(1);
        }

        return output;
    }


}
