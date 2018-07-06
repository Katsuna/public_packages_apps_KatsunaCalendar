package com.katsuna.calendar.data;

public enum DayType {
    CURRENT(0),
    SIMPLE(1),
    WITH_EVENT(2);

    private final int code;

    DayType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
