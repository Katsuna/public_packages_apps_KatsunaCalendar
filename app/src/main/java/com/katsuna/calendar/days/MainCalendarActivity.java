package com.katsuna.calendar.days;

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
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;

import com.katsuna.calendar.info.InfoActivity;
import com.katsuna.calendar.settings.SettingsActivity;
import com.katsuna.calendar.utils.Injection;
import com.katsuna.commons.controls.KatsunaNavigationView;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.ArrayList;
import java.util.List;

import static com.katsuna.commons.utils.Constants.KATSUNA_PRIVACY_URL;

public class MainCalendarActivity extends KatsunaActivity implements DaysContract.View,
        IUserProfileProvider {

//    private TextView mNoEventsText;
    private ListView mDaysList;
    private DaysAdapter mDaysAdapter;
    private DrawerLayout mDrawerLayout;

    private static final String TAG = "DaysActivity";
    private DaysContract.Presenter mPresenter;
    /**
     * Listener for clicks on alarms in the ListView.
     */
    private final DayItemListener mItemListener = new DayItemListener() {
        @Override
        public void onDayFocus(@NonNull Day day, boolean focus) {
            mPresenter.focusOnEvent(day, focus);
        }

        @Override
        public void onDayEdit(@NonNull Day day) {
            mPresenter.openDayDetails(day);
        }

        @Override
        public void onDayTypeUpdate(@NonNull Day day, @NonNull DayType dayType) {
            mPresenter.updateDayStatus(day, dayType);
        }



    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_calendar);

        init();

        // Create the presenter
        new DaysPresenter(Injection.provideEventsDataSource(getApplicationContext()), this,
                Injection.provideEventScheduler(getApplicationContext()));
    }

    private void init() {
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

        mDaysAdapter = new DaysAdapter(new ArrayList<Day>(0), mItemListener, this);
        mDaysList = findViewById(R.id.days_list);
        mDaysList.setAdapter(mDaysAdapter);

        initToolbar();
        initDrawer();
    }

    private void initDrawer() {
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.common_navigation_drawer_open,
                R.string.common_navigation_drawer_close);
        assert mDrawerLayout != null;
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupDrawerLayout();
    }

    private void setupDrawerLayout() {
        KatsunaNavigationView mKatsunaNavigationView = findViewById(R.id.katsuna_main_navigation_view);
        mKatsunaNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.drawer_settings:
                                startActivity(new Intent(MainCalendarActivity.this,
                                        SettingsActivity.class));
                                break;
                            case R.id.drawer_info:
                                startActivity(new Intent(MainCalendarActivity.this, InfoActivity.class));
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
    public void showDays(List<Day> days) {

    }

    @Override
    public void showAddEvent() {

    }

    @Override
    public void showEventDetailsUi(long eventId) {

    }

    @Override
    public void showNoEvents() {

    }

    @Override
    public void focusOnEvent(Event event, boolean focus) {

    }

    @Override
    public void reloadEvent(Event event) {

    }

    @Override
    public void setPresenter(DaysContract.Presenter presenter) {

    }

    @Override
    protected void showPopup(boolean flag) {

    }

    @Override
    public UserProfile getProfile() {
        return null;
    }
}
