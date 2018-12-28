package com.katsuna.calendar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


import com.katsuna.calendar.EventActivationActivity;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventState;
import com.katsuna.calendar.data.EventStateManager;
import com.katsuna.calendar.services.EventsScheduler;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.util.AsyncHandler;
import com.katsuna.calendar.util.EventAlertWakeLock;
import com.katsuna.calendar.util.Injection;
import com.katsuna.calendar.util.LogUtils;

import java.util.Objects;

public class EventReceiver extends BroadcastReceiver {

    private static final String TAG = "EventReceiver";

    // 1 minute auto snooze if we have events launching at the same time
    private final static int SNOOZE_DELAY_OVERLAPPING = 60;

    public void handleIntent(final Context context, Intent intent) {
        // Put here YOUR code.
        final long eventId = Objects.requireNonNull(intent.getExtras()).getLong(EventsScheduler.EVENT_ID);
        //Toast.makeText(context, "Event with id: " + eventId, Toast.LENGTH_LONG).show();

        LogUtils.i("%s onReceive, eventId: %s", TAG, eventId);

        EventsDataSource eventsDataSource = Injection.provideEventsDataSource(context);
        Event event = eventsDataSource.getEvent(eventId);

            if (event == null) {
                LogUtils.i("%s event not found with eventId: %s", TAG, eventId);
                LogUtils.i(TAG, "onReceive another event is active.");
                System.out.println( "onReceive another event is active.");
//                        eventsScheduler.snooze(event, SNOOZE_DELAY_OVERLAPPING);
            } else {
                IEventsScheduler eventsScheduler = Injection.provideEventScheduler(context);
                EventStateManager eventStateManager = EventStateManager.getInstance();
                EventState eventState = eventStateManager.getEventState(event);
                if (eventState == null) {
                    if (eventStateManager.eventActive()) {
                        LogUtils.i("%s onReceive another event is active.", TAG);
                        eventsScheduler.snooze(event, SNOOZE_DELAY_OVERLAPPING);
                    } else {
                        LogUtils.i("%s onReceive event activated.", TAG);
                        EventStateManager.getInstance().setEventState(event, EventState.ACTIVATED);
                        Intent i = new Intent(context, EventActivationActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("event", event);
                        context.startActivity(i);
                        LogUtils.d("%s onReceive, startActivity requested.", TAG);
                    }
                }
             }
        }
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final PendingResult result = goAsync();
            AsyncHandler.post(new Runnable() {
                @Override
                public void run() {
                    final PowerManager.WakeLock wl = EventAlertWakeLock.createPartialWakeLock(context);
                    // 1 minute should be enough
                    long wakeLockTimeout = 60 * 1000;
                    wl.acquire(wakeLockTimeout);
                    LogUtils.d("%s onReceive, before handleIntent", TAG);
                    handleIntent(context, intent);
                    LogUtils.d("%s onReceive, after handleIntent", TAG);
                    result.finish();
                }
            });
        }
}
