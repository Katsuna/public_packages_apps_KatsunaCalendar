package com.katsuna.calendar.services;

import com.katsuna.calendar.data.Event;

import org.threeten.bp.LocalDateTime;

public interface INextEventCalculator {

    LocalDateTime getTriggerDateTime(LocalDateTime now, Event event);
}
