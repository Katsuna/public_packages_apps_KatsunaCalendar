package com.katsuna.calendar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventState;
import com.katsuna.calendar.data.EventStateManager;
import com.katsuna.calendar.services.EventsScheduler;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.utils.Injection;
import com.katsuna.calendar.utils.LogUtils;

import java.util.Objects;

public class EventReceiver extends BroadcastReceiver {

    private static final String TAG = "EventReceiver";

    // 1 minute auto snooze if we have events launching at the same time
    private final static int SNOOZE_DELAY_OVERLAPPING = 60;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Put here YOUR code.
        final long eventId = Objects.requireNonNull(intent.getExtras()).getLong(EventsScheduler.EVENT_ID);
        LogUtils.i(TAG, "onReceive, eventId: " + eventId);

        //Toast.makeText(context, "Event with id: " + eventId, Toast.LENGTH_LONG).show();

        EventsDataSource eventsDataSource = Injection.provideEventsDataSource(context);
        eventsDataSource.getEvent(eventId, new EventsDataSource.GetEventCallback() {
            @Override
            public void onEventLoaded(Event event) {

                IEventsScheduler eventsScheduler = Injection.provideEventScheduler(context);

                EventStateManager eventStateManager = EventStateManager.getInstance();

                EventState eventState = eventStateManager.getEventState(event);
                LogUtils.i(TAG, "onReceive eventState: " + eventState);
                if (eventState == null) {
                    if (eventStateManager.eventActive()) {
                        LogUtils.i(TAG, "onReceive another event is active.");
//                        eventsScheduler.snooze(event, SNOOZE_DELAY_OVERLAPPING);
                    } else {
                        LogUtils.i(TAG, "onReceive event activated.");
                        EventStateManager.getInstance().setEventState(event, EventState.ACTIVATED);
//                        Intent i = new Intent(context, EventActivationActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.putExtra(EventsScheduler.EVENT_ID, eventId);
//                        context.startActivity(i);
                     }
                }
            }

            @Override
            public void onDataNotAvailable() {
                LogUtils.i(TAG, "event not found with eventId: " + eventId);
            }
        });
    }

}
