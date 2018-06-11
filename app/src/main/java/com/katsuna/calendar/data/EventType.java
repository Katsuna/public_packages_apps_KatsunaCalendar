package com.katsuna.calendar.data;

public enum EventType {
    ALARM(0),
    REMINDER(1);

    private final int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}