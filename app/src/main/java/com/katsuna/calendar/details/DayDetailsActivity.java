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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;

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
    private TextView mNoEventsText;
    private int currentMonth, currentYear, currentDay;



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
        Day day =  (Day) i.getParcelableExtra("Day");

        init(day);

        // Create the presenter
        new DayDetailsPresenter(this, Injection.provideEventsDataSource(getApplicationContext()), Injection.provideEventScheduler(getApplicationContext()));
    }

    public void init (Day day) {
        mDay =  day;
        currentDay = Integer.parseInt(day.getDay());
        currentMonth = Integer.parseInt(day.getMonth());
        currentYear = Integer.parseInt(day.getYear());
        mEventList = findViewById(R.id.day_details_list);
        mNoEventsText = findViewById(R.id.no_events);

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
        mFabContainer = findViewById(R.id.fab_container);

        mDayDetailsAdapter = new DayDetailsAdapter(mDay.getEvents(), mItemListener, this,day);
        mDayDetailsAdapter.notifyDataSetChanged();
        mEventList.setAdapter(mDayDetailsAdapter);

        initToolbar(R.drawable.common_ic_close_black54_24dp);

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

    @Override
    public void showEvents(List<Event> events) {
        mDayDetailsAdapter.replaceData(events);

        mEventList.setVisibility(View.VISIBLE);
        mNoEventsText.setVisibility(View.GONE);
        mFab2.setVisibility(View.GONE);
//        mPopupButton2.setVisibility(View.GONE);
        mDayDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoEvents() {
        mNoEventsText.setVisibility(View.VISIBLE);
//        mFab1.setVisibility(View.VISIBLE);
        mFab2.setVisibility(View.VISIBLE);
//        mPopupButton1.setVisibility(View.VISIBLE);
//        mPopupButton2.setVisibility(View.VISIBLE);
        mEventList.setVisibility(View.GONE);
    }

    @Override
    public void moveFabsToBottomAndTint(boolean flag) {
        tintFabs(flag);
        adjustFabPosition(!flag);
    }

    public void previousDay(View view) {
        Day newDay = new Day();
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth,currentDay);
        if(currentDay == 1) {

            if(currentMonth == 1){
                currentMonth = 12;
                currentYear = currentYear -1;
            }
            else
                currentMonth = currentMonth -1;

            YearMonth yearMonthObject = YearMonth.of(currentYear, currentMonth);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            currentDay=daysInMonth;
        }
        else {
            currentDay --;
        }
        newDay.setDay(String.valueOf(currentDay));
        newDay.setMonth(String.valueOf(currentMonth));
        newDay.setYear(String.valueOf(currentYear));
        newDay.setDayName(new SimpleDateFormat("EEEE").format(calendar.getTime()));
//        TextView monthView = findViewById(R.id.month_name);
//        monthView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()));
//
//        TextView yearView = findViewById(R.id.year);
//        yearView.setText(Integer.toString(calendar.get(Calendar.YEAR)));

        init(newDay);
        mPresenter.loadEvents();
    }

    public void nextDay(View view) {
        Day newDay = new Day();
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth,currentDay);
        YearMonth yearMonthObject = YearMonth.of(currentYear, currentMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        if(currentDay == daysInMonth) {

            if(currentMonth == 12){
                currentMonth = 1;
                currentYear = currentYear ++;
            }
            else
                currentMonth = currentMonth ++;

            yearMonthObject = YearMonth.of(currentYear, currentMonth);
            daysInMonth = yearMonthObject.lengthOfMonth();
            currentDay=daysInMonth;
        }
        else {
            currentDay ++;
        }
        newDay.setDay(String.valueOf(currentDay));
        newDay.setMonth(String.valueOf(currentMonth));
        newDay.setYear(String.valueOf(currentYear));
        newDay.setDayName(new SimpleDateFormat("EEEE").format(calendar.getTime()));
        init(newDay);
        mPresenter.loadEvents();
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
