package com.katsuna.calendar.grid_calendar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.event.ManageEventActivity;
import com.katsuna.calendar.util.Injection;
import com.katsuna.commons.entities.ColorProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.ColorCalcV2;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.Date;

import static com.katsuna.calendar.event.ManageEventActivity.EXTRA_EVENT_TYPE;

public class GridCalendarActivity extends KatsunaActivity implements GridCalendarContract.View, IUserProfileProvider {
    private static final int REQUEST_CODE_NEW_EVENT = 1;

    private CalendarView calendarView;
    private GridCalendarContract.Presenter mPresenter;
    private UserProfile userProfile;
    private TextView mGridCalendarTItle;
    private UserProfile mUserProfile;
    private int mPrimaryColor2;
    private int mSecondaryColor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.grid_calendar);
//        init();
//
//        // Create the presenter
//        new GridCalendarPresenter(this);
    }

    private void initActivity(UserProfile userProfile){
        if (userProfile.isRightHanded) {
            setContentView(R.layout.grid_calendar);
        } else {
            setContentView(R.layout.grid_calendar_lh);
        }

        setContentView(R.layout.grid_calendar);
        init();

        // Create the presenter
        new GridCalendarPresenter(this);
    }


    private void init(){
        calendarView = (CalendarView) findViewById(R.id.calendar_view); // get the reference of CalendarView
        mFab2 = findViewById(R.id.create_event_fab);
        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(calendarView.getDate());

                Day day = new Day();
                day.setDay((String) DateFormat.format("d",   date));
                day.setMonth((String) DateFormat.format("M",   date));
                day.setYear((String) DateFormat.format("yyyy",   date));
                day.setDayType(DayType.SIMPLE);
                day.setMonthShort((String) DateFormat.format("MMM",  date));
                day.setDayName((String) DateFormat.format("EEEE",   date));
                mPresenter.addOnDayNewEvent(day);
            }
        });
//        mFab2.setBackgroundTintList(ColorStateList.valueOf(mPrimaryColor2));
//        mFab2.setImageTintList(ColorStateList.valueOf(mWhiteColor));
        initToolbar(R.drawable.common_ic_close_black54_24dp);
//        mGridCalendarTItle = findViewById(R.id.titl);

    }

    @Override
    public void showAddEvent(Day day) {
        Intent i = new Intent(this, ManageEventActivity.class);
        i.putExtra(EXTRA_EVENT_TYPE, EventType.ALARM);
        i.putExtra("Day",  day);

        startActivityForResult(i, REQUEST_CODE_NEW_EVENT);
    }

    @Override
    public void setGridCalendarTitle(int common_select_day) {

    }

    @Override
    protected void showPopup(boolean flag) {

    }

    @Override
    public void setPresenter(GridCalendarContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserProfile userProfile = getActiveUserProfile();

        if (mUserProfile == null) {
            mUserProfile = userProfile;
            initActivity(userProfile);
        } else if (!mUserProfile.equals(userProfile)){
            // check if we need to change layouts
            if (mUserProfile.isRightHanded != userProfile.isRightHanded) {
                mUserProfile = userProfile;
                initActivity(userProfile);
            }
        }
        adjustProfiles(userProfile);
        mPresenter.start();

    }
    private void adjustProfiles(UserProfile userProfile) {


        mPrimaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.PRIMARY_COLOR_2,
                userProfile.colorProfile);
        mSecondaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.SECONDARY_COLOR_2,
                userProfile.colorProfile);

        // first time init check


    }
    @Override
    public UserProfile getProfile() {
        return mUserProfileContainer.getActiveUserProfile();
    }
}
