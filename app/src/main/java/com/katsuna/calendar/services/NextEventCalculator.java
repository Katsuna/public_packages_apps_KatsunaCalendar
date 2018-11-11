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

    private LocalDateTime getNearestTriggerDateTime(LocalDateTime now, Event event) {
        List<LocalDateTime> nearestDays = new ArrayList<>();


        // get nearest datetime
        LocalDateTime output = null;
        for (LocalDateTime dateTime : nearestDays) {
            if (output == null) {
                output = dateTime;
            } else if (dateTime.isBefore(output)) {
                output = dateTime;
            }
        }

        return output;
    }

    private LocalDateTime getNearestDay(LocalDateTime now, DayOfWeek dayOfWeek, Event event) {
        LocalDateTime output;
        if (now.getDayOfWeek() == dayOfWeek) {
            output = LocalDateTime.of(now.toLocalDate(),
                    LocalTime.of(event.getHour(), event.getMinute()));
            if (output.isBefore(now) || output.isEqual(now)) {
                output = LocalDateTime.of(
                        now.toLocalDate().plusDays(1).with(TemporalAdjusters.next(dayOfWeek)),
                        LocalTime.of(event.getHour(), event.getMinute()));
            }
        } else {
            output = LocalDateTime.of(
                    now.toLocalDate().with(TemporalAdjusters.next(dayOfWeek)),
                    LocalTime.of(event.getHour(), event.getMinute()));
        }

        return output;
    }

}
