package com.katsuna.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStateManager;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.data.source.EventsDataSource;
import com.katsuna.calendar.services.EventsScheduler;
import com.katsuna.calendar.services.IEventsScheduler;
import com.katsuna.calendar.util.Injection;
import com.katsuna.calendar.util.LogUtils;

import com.katsuna.commons.entities.ColorProfile;
import com.katsuna.commons.entities.ColorProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.entities.UserProfileContainer;
import com.katsuna.commons.utils.ColorCalcV2;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.commons.utils.Shape;

public class EventActivationActivity extends AppCompatActivity {

    private final static String TAG = EventActivationActivity.class.getSimpleName();
    // 3 minutes default snooze
    private final static int SNOOZE_DELAY = 3 * 60;

    private Button mSnoozeButton;
    private Button mDismissButton;
    private IEventsScheduler mEventsScheduler;
    private EventsDataSource mEventsDataSource;
    private Event mEvent;
    private boolean handled = false;
    private boolean mFocusDuringOnPause;
    private TextView mDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        enableLaunchingWhenLocked();
        hideNavigationBar();

        setContentView(R.layout.activation);

        mSnoozeButton = findViewById(R.id.snooze_button);
        mDismissButton = findViewById(R.id.dismiss_button);
        mDescription = findViewById(R.id.event_description);

        Long eventId = getEventId();

        mEventsDataSource = Injection.provideEventsDataSource(this);
        mEventsScheduler = Injection.provideEventScheduler(this);

        //noinspection ConstantConditions
        mEventsDataSource.getEvent(eventId, new EventsDataSource.GetEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                mEvent = event;
                if (mEvent.getEventType() == EventType.REMINDER) {
                    mDescription.setText(mEvent.getDescription());
                }

                init();
                soundTheEvent();
            }

            @Override
            public void onDataNotAvailable() {
                // TODO
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        adjustProfiles();
    }

    private void adjustProfiles() {
        UserProfileContainer userProfileContainer = ProfileReader.getKatsunaUserProfile(this);
        UserProfile userProfile = userProfileContainer.getActiveUserProfile();

        // color adjustments
        int primaryColor1 = ColorCalcV2.getColor(this, ColorProfileKeyV2.PRIMARY_COLOR_1,
                userProfile.colorProfile);
        int white = ContextCompat.getColor(this, R.color.common_white);

        if (userProfile.colorProfile == ColorProfile.CONTRAST) {
            int grey300 = ContextCompat.getColor(this, R.color.common_grey300);
            getWindow().getDecorView().setBackgroundColor(grey300);
        } else {
            getWindow().getDecorView().setBackgroundColor(primaryColor1);
        }

        int primaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.PRIMARY_COLOR_2,
                userProfile.colorProfile);
        float radius = getResources().getDimension(R.dimen.common_8dp);
        Shape.setRoundedBackground(mDismissButton, primaryColor2, radius);
        Shape.setRoundedBackground(mSnoozeButton, white , radius);
    }

    private Long getEventId() {
        Long eventId = null;
        Intent i = getIntent();
        if (i.getExtras() != null) {
            eventId = i.getExtras().getLong(EventsScheduler.EVENT_ID);
        }
        return eventId;
    }

    private void init() {
        mEventsScheduler = Injection.provideEventScheduler(this);

        mSnoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snoozeEvent(SNOOZE_DELAY);
            }
        });
        mDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissEvent();
            }
        });
    }

    private void dismissEvent() {
        stopEvent();

//        if (mEvent.is()) {
//            // reshedule
//            LogUtils.i(TAG, "onReceive rescheduling event: " + mEvent);
//            mEventsScheduler.reschedule(mEvent);
//        } else {
            // deactivate
            LogUtils.i(TAG, "onReceive deactivating event: " + mEvent);
            mEvent.setEventStatus(EventStatus.INACTIVE);
            mEventsDataSource.saveEvent(mEvent);
//        }
        handled = true;

        EventStateManager.getInstance().removeEvent(mEvent);
        finish();
    }

    private void snoozeEvent(long delay) {
        EventStateManager.getInstance().removeEvent(mEvent);
        stopEvent();
//        mEventsScheduler.snooze(mEvent, delay);
        handled = true;
        finish();
    }

    private void soundTheEvent() {
        EventKlaxon.start(this);
    }


    private void stopEvent() {
        EventKlaxon.stop(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFocusDuringOnPause = hasWindowFocus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop called: handled=" + handled);
        if (mFocusDuringOnPause) {
            if (!handled) {
                snoozeEvent(SNOOZE_DELAY);
            }
        }
        /* else {
                // activity was started when screen was off / screen was on with keygaurd displayed
                // see https://stackoverflow.com/a/25474853
           } */
    }

    private void enableLaunchingWhenLocked() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    private void hideNavigationBar() {
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
    }
}