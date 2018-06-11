package com.katsuna.calendar.data;

public class EventStatus {
    ACTIVE(0),
    INACTIVE(1);

    private final int code;

    EventStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
