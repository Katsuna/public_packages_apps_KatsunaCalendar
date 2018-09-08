package com.katsuna.calendar.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.event.ManageEventActivity;
import com.katsuna.calendar.info.InfoActivity;
import com.katsuna.calendar.settings.SettingsActivity;
import com.katsuna.calendar.utils.Injection;
import com.katsuna.calendar.utils.LogUtils;
import com.katsuna.commons.controls.KatsunaNavigationView;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.ArrayList;
import java.util.List;

import static com.katsuna.commons.utils.Constants.KATSUNA_PRIVACY_URL;


/**
 * Display a list of {@link Event}s. User can choose to view all active and inactive alarms,
 * and create and edit alarms.
 */
public class EventsActivity extends KatsunaActivity implements EventsContract.View,
        IUserProfileProvider {

    private static final String TAG = "EventsActivity";
    private EventsContract.Presenter mPresenter;
    /**
     * Listener for clicks on alarms in the ListView.
     */
    private final EventItemListener mItemListener = new EventItemListener() {
        @Override
        public void onEventFocus(@NonNull Event alarm, boolean focus) {
            mPresenter.focusOnEvent(alarm, focus);
        }

        @Override
        public void onEventEdit(@NonNull Event alarm) {
            mPresenter.openEventDetails(alarm);
        }

        @Override
        public void onEventStatusUpdate(@NonNull Event alarm, @NonNull EventStatus alarmStatus) {
            mPresenter.updateEventStatus(alarm, alarmStatus);
        }

        @Override
        public void onEventDelete(@NonNull Event alarm) {
            mPresenter.deleteEvent(alarm);
        }
    };
    private TextView mNoEventsText;
    private ListView mEventsList;
    private EventsAdapter mEventsAdapter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);

        init();

        // Create the presenter
        new EventsPresenter(Injection.provideEventsDataSource(getApplicationContext()), this,
                Injection.provideEventScheduler(getApplicationContext()));
    }

    private void init() {
        mNoEventsText = findViewById(R.id.no_events);
        mPopupButton2 = findViewById(R.id.create_event_button);
        mPopupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewEvent();
            }
        });

        mFab2 = findViewById(R.id.create_event_fab);
        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewEvent();
            }
        });

        mEventsAdapter = new EventsAdapter(new ArrayList<Event>(0), mItemListener, this);
        mEventsList = findViewById(R.id.events_list);
        mEventsList.setAdapter(mEventsAdapter);

        initToolbar();
        initDrawer();
    }

    private void initDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.common_navigation_drawer_open,
                R.string.common_navigation_drawer_close);
        assert mDrawerLayout != null;
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupDrawerLayout();
    }

    private void setupDrawerLayout() {
        KatsunaNavigationView mKatsunaNavigationView = findViewById(R.id.katsuna_navigation_view);
        mKatsunaNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.drawer_settings:
                                startActivity(new Intent(EventsActivity.this,
                                        SettingsActivity.class));
                                break;
                            case R.id.drawer_info:
                                startActivity(new Intent(EventsActivity.this, InfoActivity.class));
                                break;
                            case R.id.drawer_privacy:
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(KATSUNA_PRIVACY_URL));
                                startActivity(browserIntent);
                                break;
                        }

                        return true;
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void showPopup(boolean flag) {
        // no op
    }

    @Override
    public void setPresenter(@NonNull EventsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEvents(List<Event> events) {
        mEventsAdapter.replaceData(events);

        mEventsList.setVisibility(View.VISIBLE);
        mNoEventsText.setVisibility(View.GONE);
        mPopupButton2.setVisibility(View.GONE);
        LogUtils.d(TAG, "events fetched: " + events.size());
    }

    @Override
    public void showAddEvent() {
        Intent i = new Intent(this, ManageEventActivity.class);
        startActivity(i);
    }

    @Override
    public void showEventDetailsUi(long alarmId) {
        Intent intent = new Intent(this, ManageEventActivity.class);
        intent.putExtra(ManageEventActivity.EXTRA_EVENT_ID, alarmId);
        startActivity(intent);
    }

    @Override
    public void showNoEvents() {
        mNoEventsText.setVisibility(View.VISIBLE);
        mFab2.setVisibility(View.VISIBLE);
        mPopupButton2.setVisibility(View.VISIBLE);
        mEventsList.setVisibility(View.GONE);
    }

    @Override
    public void focusOnEvent(Event alarm, boolean focus) {
        mEventsAdapter.focusOnEvent(alarm, focus);
    }

    @Override
    public void reloadEvent(Event alarm) {
        mEventsAdapter.reloadEvent(alarm);
    }

    @Override
    public UserProfile getProfile() {
        return mUserProfileContainer.getActiveUserProfile();
    }
}
