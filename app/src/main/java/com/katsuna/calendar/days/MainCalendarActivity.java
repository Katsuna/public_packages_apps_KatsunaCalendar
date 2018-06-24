package com.katsuna.calendar.days;

import android.os.Bundle;
import android.view.View;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.utils.Injection;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.ArrayList;
import java.util.List;

public class MainCalendarActivity extends KatsunaActivity implements DaysContract.View,
        IUserProfileProvider {


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

        mEventsAdapter = new DaysAdapter(new ArrayList<Event>(0), mItemListener, this);
        mEventsList = findViewById(R.id.events_list);
        mEventsList.setAdapter(mEventsAdapter);

        initToolbar();
        initDrawer();
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
