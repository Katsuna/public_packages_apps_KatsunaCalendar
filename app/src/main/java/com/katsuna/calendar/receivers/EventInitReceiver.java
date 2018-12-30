package com.katsuna.calendar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.katsuna.calendar.Utils;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.util.Injection;
import com.katsuna.calendar.util.LogUtils;


public class EventInitReceiver extends BroadcastReceiver {

    private static final String TAG = EventInitReceiver.class.getSimpleName();

    /**
     * When running on N devices, we're interested in the boot completed event that is sent while
     * the user is still locked, so that we can schedule alarms.
     */
    private static final String ACTION_BOOT_COMPLETED = Utils.isNOrLater()
            ? Intent.ACTION_LOCKED_BOOT_COMPLETED : Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        LogUtils.i("%s EventInitReceiver action: %s", TAG, action);

        if (isActionSupported(action)) {

            IEventsScheduler alarmsScheduler = Injection.provideEventScheduler(context);
            alarmsScheduler.schedule(new IEventsScheduler.CallBack() {
                @Override
                public void schedulingFinished() {
                    LogUtils.i("%s alarms scheduling completed", TAG);
                    System.out.println("%s alarms scheduling completed");
                }

                @Override
                public void schedulingFailed(Exception ex) {
                    LogUtils.e("%s exception while scheduling alarms:  %s", TAG, ex.toString());
                    System.out.println("%s alarms scheduling failed");

                }
            });

        }
    }

    private boolean isActionSupported(String action) {
        return ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_LOCALE_CHANGED.equals(action)
                || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(action);
    }
}
