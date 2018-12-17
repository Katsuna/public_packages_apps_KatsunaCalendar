package com.katsuna.calendar.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.event.ManageEventActivity;
import com.katsuna.calendar.events.EventItemListener;
import com.katsuna.calendar.util.Injection;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.text.DateFormatSymbols;

import static com.katsuna.calendar.event.ManageEventActivity.EXTRA_EVENT_TYPE;

public class DayDetailsActivity extends KatsunaActivity implements DayDetailsContract.View,
        IUserProfileProvider {

    private static final int REQUEST_CODE_NEW_EVENT = 1;
    private boolean focusFlag = false;
    private Event previousEvent = null;


    private Day mDay;
    private TextView mDayTitle;
    private DayDetailsAdapter mDayDetailsAdapter;
    private ListView mEventList;
    private DayDetailsContract.Presenter mPresenter;


    private final EventItemListener mItemListener = new EventItemListener() {
        @Override
        public void onEventFocus(@NonNull Event event, boolean focus) {
            mPresenter.focusOnEvent(event, focus);
        }

        @Override
        public void onEventEdit(@NonNull Event event) {
            mPresenter.openEventDetails(event);

        }

        @Override
        public void onEventStatusUpdate(@NonNull Event event, @NonNull EventStatus eventStatus) {

        }

        @Override
        public void onEventDelete(@NonNull Event event) {
            mPresenter.deleteEvent(event);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_day_details);
//        adjustProfiles();
        Intent i = getIntent();
        mDay =  (Day) i.getParcelableExtra("Day");

        init();

        // Create the presenter
        new DayDetailsPresenter(this, Injection.provideEventsDataSource(getApplicationContext()), Injection.provideEventScheduler(getApplicationContext()));
    }

    public void init () {
        mEventList = findViewById(R.id.day_details_list);

        mDayTitle = findViewById(R.id.day_title);

        mFab2 = findViewById(R.id.create_event_fab);
        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewEvent(mDay);
            }
        });
        if(mDay != null) {
            mDayTitle.setText(mDay.getDayName() + " "+mDay.getDay() + ", " + getMonth(Integer.parseInt(mDay.getMonth())));
        }
//        mDay.getEvents().forEach((i)->);
        mDayDetailsAdapter = new DayDetailsAdapter(mDay.getEvents(), mItemListener, this);
        mDayDetailsAdapter.notifyDataSetChanged();
        mEventList.setAdapter(mDayDetailsAdapter);
    }

    @Override
    public void setPresenter(DayDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void showPopup(boolean flag) {

    }

    @Override
    public UserProfile getProfile() {
        return mUserProfileContainer.getActiveUserProfile();    }

    @Override
    public void focusOnEvent(Event event, boolean focus) {
        mDayDetailsAdapter.focusOnEvent(event, focus);

        if( focusFlag == false) {
            mFab2.setVisibility(View.GONE);
            focusFlag = true;
        }
        else if( focusFlag == true && event.equals(previousEvent)){
            mFab2.setVisibility(View.VISIBLE);
            focusFlag = false;
        }
        previousEvent = event;
    }

    @Override
    public void showAddEvent(Day day) {
        Intent i = new Intent(this, ManageEventActivity.class);
        i.putExtra(EXTRA_EVENT_TYPE, EventType.ALARM);
        i.putExtra("Day",  day);

        startActivityForResult(i, REQUEST_CODE_NEW_EVENT);
    }

    @Override
    public void showEventDetailsUi(long eventId) {
        Intent intent = new Intent(this, ManageEventActivity.class);
        intent.putExtra(ManageEventActivity.EXTRA_EVENT_ID, eventId);
        startActivity(intent);
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
