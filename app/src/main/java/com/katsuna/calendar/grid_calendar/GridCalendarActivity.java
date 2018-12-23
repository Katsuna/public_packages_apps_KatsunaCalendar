package com.katsuna.calendar.grid_calendar;

import android.content.Intent;
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
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.Date;

import static com.katsuna.calendar.event.ManageEventActivity.EXTRA_EVENT_TYPE;

public class GridCalendarActivity extends KatsunaActivity implements GridCalendarContract.View, IUserProfileProvider {
    private static final int REQUEST_CODE_NEW_EVENT = 1;

    private CalendarView calendarView;
    private GridCalendarContract.Presenter mPresenter;
    private UserProfile userProfile;
    private TextView mGridCalendarTItle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mPresenter.start();

    }

    @Override
    public UserProfile getProfile() {
        return mUserProfileContainer.getActiveUserProfile();
    }
}
