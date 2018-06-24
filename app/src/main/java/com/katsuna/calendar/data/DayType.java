package com.katsuna.calendar.data;

public enum DayType {
    CURRENT(0),
    WITH_EVENT(1);

    private final int code;

    DayType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
