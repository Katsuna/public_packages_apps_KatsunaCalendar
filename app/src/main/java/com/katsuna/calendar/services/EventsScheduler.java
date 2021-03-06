/**
* Copyright (C) 2020 Manos Saratsis
*
* This file is part of Katsuna.
*
* Katsuna is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Katsuna is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Katsuna.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.katsuna.calendar.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.event.ManageEventActivity;
import com.katsuna.calendar.receivers.EventReceiver;
import com.katsuna.calendar.util.DateUtils;
import com.katsuna.calendar.util.LogUtils;

import org.threeten.bp.LocalDateTime;

import java.util.List;
import java.util.Objects;

import static android.app.PendingIntent.FLAG_NO_CREATE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class EventsScheduler implements IEventsScheduler {

    private static final String TAG = EventsScheduler.class.getSimpleName();
    public static final String EVENT_ID = "event_id";

    private final Context mContext;
    private final EventsDataSource mEventsDatasource;
    private final INextEventCalculator mNextEventCalculator;

    public EventsScheduler(@NonNull Context context, @NonNull EventsDataSource eventsDataSource, @NonNull INextEventCalculator nextEventCalculator) {
        mContext = context;
        mEventsDatasource = eventsDataSource;
        mNextEventCalculator = nextEventCalculator;
    }

    @Override
    public void schedule(@NonNull final CallBack callBack) {

        mEventsDatasource.getEvents(new EventsDataSource.LoadEventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {

                try {
                    for (Event event : events) {
                        setEvent(event);
                    }
                    callBack.schedulingFinished();
                } catch (Exception ex) {
                    callBack.schedulingFailed(ex);
                }
            }

            @Override
            public void onDataNotAvailable() {
                callBack.schedulingFinished();
            }
        });
    }

    @Override
    public void reschedule(Event event) {
        cancel(event);

        if (event.getEventStatus() == EventStatus.ACTIVE) {
            setEvent(event);
        }
    }

    @Override
    public void setEvent(Event event) {
        LogUtils.d(TAG, "setEvent called. event: " + event.toString());
        if (event.getEventStatus() != EventStatus.ACTIVE) return;

        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime triggerDateTime = mNextEventCalculator.getTriggerDateTime(now, event);

        AlarmManager.AlarmClockInfo eventClockInfo = new AlarmManager.AlarmClockInfo(
                DateUtils.toEpochMillis(triggerDateTime),
                getPendindEditIntent(event));

        Objects.requireNonNull(am).setAlarmClock(eventClockInfo, getPendingTriggerIntent(event,
                FLAG_UPDATE_CURRENT));

        LogUtils.i(TAG, String.format("Event %s scheduled at (%s)", event, triggerDateTime));
    }



    @Override
    public void cancel(Event event) {
        LogUtils.d(TAG, "cancelEvent: " + event.toString());
        PendingIntent pi = getPendingTriggerIntent(event, FLAG_UPDATE_CURRENT);
        AlarmManager eventManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(eventManager).cancel(pi);

        pi.cancel();
    }

    @Override
    public void snooze(Event event, long delay) {
        LogUtils.d("%s snooze: %s", TAG, event);

        if (event.getEventStatus() != EventStatus.ACTIVE) return;

        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        LocalDateTime rescheduledTime = LocalDateTime.now().plusSeconds(delay);

        AlarmManager.AlarmClockInfo eventClockInfo = new AlarmManager.AlarmClockInfo(
                DateUtils.toEpochMillis(rescheduledTime),
                getPendindEditIntent(event));
        Objects.requireNonNull(am).setAlarmClock(eventClockInfo, getPendingTriggerIntent(event,
                FLAG_UPDATE_CURRENT));

        LogUtils.i("%s Alarm %s snoozed and  scheduled at (%s)", TAG, event, rescheduledTime);
    }

    private PendingIntent getPendingTriggerIntent(Event event, int flag) {
        Intent i = new Intent(mContext, EventReceiver.class);
        i.putExtra(EVENT_ID, event.getEventId());
        return PendingIntent.getBroadcast(mContext, event.hashCode(), i, flag);
    }

    private PendingIntent getPendindEditIntent(Event event) {
        Intent i = new Intent(mContext, ManageEventActivity.class);
        i.putExtra(ManageEventActivity.EXTRA_EVENT_ID, event.getEventId());
        return PendingIntent.getActivity(mContext, event.hashCode(), i, 0);
    }

    public boolean isEventSet(Event event) {
        PendingIntent pi = getPendingTriggerIntent(event, FLAG_NO_CREATE);
        return pi != null;
    }
}
