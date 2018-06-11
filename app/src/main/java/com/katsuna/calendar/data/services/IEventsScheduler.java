package com.katsuna.calendar.data.services;


import com.katsuna.calendar.data.Event;

public interface IEventsScheduler {

    void schedule(CallBack callBack);

    void reschedule(Event event);

    void setEvent(Event event);

    void cancel(Event event);

    interface CallBack {
        void schedulingFinished();

        void schedulingFailed(Exception ex);
    }
}
