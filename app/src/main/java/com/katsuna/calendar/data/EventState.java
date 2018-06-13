package com.katsuna.calendar.data;

public enum EventState {
    ACTIVATED(0);

    private final int code;

    EventState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
