package com.katsuna.calendar.validators;

import com.katsuna.calendar.data.EventType;

import java.util.List;

public interface IEventValidator {

    List<ValidationResult> validateAll(EventType eventType, String description, String hour,
                                       String minute);

    List<ValidationResult> validateEventType(EventType eventType, String description);

    List<ValidationResult> validateTime(String hour, String minute);
}
