package com.katsuna.calendar.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.days.DaysPresenter;
import com.katsuna.calendar.events.EventItemListener;
import com.katsuna.calendar.util.Injection;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

public class DayDetailsActivity extends KatsunaActivity implements DayDetailsContract.View,
        IUserProfileProvider {

    private Day mDay;
    private TextView mDayTitle;
    private DayDetailsAdapter mDayDetailsAdapter;
    private ListView mEventList;

    private final EventItemListener mItemListener = new EventItemListener() {
        @Override
        public void onEventFocus(@NonNull Event alarm, boolean focus) {

        }

        @Override
        public void onEventEdit(@NonNull Event alarm) {

        }

        @Override
        public void onEventStatusUpdate(@NonNull Event event, @NonNull EventStatus eventStatus) {

        }

        @Override
        public void onEventDelete(@NonNull Event alarm) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_day_details);
//        adjustProfiles();
        Bundle bundle = getIntent().getExtras();
        mDay = bundle.getParcelable("com.katsuna.calendar.data.Day");

        init();

        // Create the presenter
        new DayDetailsPresenter();
    }

    public void init () {
        mDayTitle = findViewById(R.id.day_title);
        if(mDay != null) {
            mDayTitle.setText(mDay.getDayName() + ", "+mDay.getDay() + ", " + mDay.getMonth());
        }
//        mDay.getEvents().forEach((i)->);
        mDayDetailsAdapter = new DayDetailsAdapter(mDay.getEvents(), mItemListener, this);
        mDayDetailsAdapter.notifyDataSetChanged();
        mEventList.setAdapter(mDayDetailsAdapter);
    }

    @Override
    public void setPresenter(DayDetailsContract.Presenter presenter) {

    }

    @Override
    protected void showPopup(boolean flag) {

    }

    @Override
    public UserProfile getProfile() {
        return null;
    }

    @Override
    public void focusOnEvent(Event event, boolean focus) {

    }

    @Override
    public void showAddEvent(Day day) {

    }
}
