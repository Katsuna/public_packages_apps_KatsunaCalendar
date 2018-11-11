package com.katsuna.calendar.days;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;

import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.event.ManageEventActivity;
import com.katsuna.calendar.formatters.EventFormatter;
import com.katsuna.calendar.info.InfoActivity;
import com.katsuna.calendar.settings.SettingsActivity;
import com.katsuna.calendar.util.Injection;
import com.katsuna.commons.controls.KatsunaNavigationView;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.ui.KatsunaActivity;
import com.katsuna.commons.utils.IUserProfileProvider;
import com.katsuna.commons.utils.KatsunaAlertBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.katsuna.calendar.event.ManageEventActivity.EXTRA_EVENT_TYPE;
import static com.katsuna.commons.utils.Constants.KATSUNA_PRIVACY_URL;

public class DaysActivity extends KatsunaActivity implements DaysContract.View,
        IUserProfileProvider {


    private static final int REQUEST_CODE_NEW_EVENT = 1;
    private static final int REQUEST_CODE_EDIT_EVENT = 2;
//    private TextView mNoEventsText;
    private ListView mDaysList;
    private DaysAdapter mDaysAdapter;
    private DrawerLayout mDrawerLayout;
    private View mDayFocused;
    private boolean focusFlag = false;
    Calendar calendar;
    private int mPrimaryColor2;
    private int mSecondaryColor2;



    private int currentMonth, currentYear;

    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;



    private static final String TAG = "DaysActivity";
    private DaysContract.Presenter mPresenter;
    /**
     * Listener for clicks on days in the ListView.
     */
    private final DayItemListener mItemListener = new DayItemListener() {
        @Override
        public void onDayAddEvent(@NonNull Day day) {
            mPresenter.addOnDayNewEvent(day);
        }

        @Override
        public void onDayFocus(@NonNull Day day, boolean focus) {
            mPresenter.focusOnDay(day, focus);
        }

        @Override
        public void onDayEventEdit(@NonNull Day day) {
            mPresenter.openDayDetails(day);

        }

        @Override
        public void onDayTypeUpdate(@NonNull Day day, @NonNull DayType dayType) {
            mPresenter.updateDayStatus(day, dayType);
        }

        @Override
        public void onEventStatusUpdate(@NonNull Event event, @NonNull EventStatus eventStatus) {
            mPresenter.updateEventStatus(event, eventStatus);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_calendar);
//        adjustProfiles();

        init();

        // Create the presenter
        new DaysPresenter(Injection.provideEventsDataSource(getApplicationContext()), this,
                Injection.provideEventScheduler(getApplicationContext()));
    }

    private void init() {
        calendar = Calendar.getInstance();
        AndroidThreeTen.init(this);
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

//        mDaysAdapter = new DaysAdapter(new ArrayList<Day>(0), mItemListener, this);
        mDaysList = findViewById(R.id.days_list);


        initToolbar();
        initDrawer();
        initCalendar();
        startWithToday();
    }

    private void startWithToday() {
        calendar = Calendar.getInstance();
        mDaysList.setSelection(calendar.get(Calendar.DAY_OF_MONTH)-1);
    }

    public void previousMonth(View view)
    {
        if(currentMonth == 0){
            currentMonth = 11;
            currentYear = currentYear -1;
        }
        else
            currentMonth = currentMonth -1;

        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth ,1);
        TextView monthView = findViewById(R.id.month_name);
        monthView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()));

        TextView yearView = findViewById(R.id.year);
        yearView.setText(Integer.toString(calendar.get(Calendar.YEAR)));

        try {
            initMonth((currentMonth),currentYear);
            mPresenter.loadDays();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void nextMonth(View view) {
        if(currentMonth == 11){
            currentMonth = 0;
            currentYear = currentYear +1;
        }
        else
            currentMonth = currentMonth +1;

        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth ,1);
        TextView monthView = findViewById(R.id.month_name);
        monthView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()));

        TextView yearView = findViewById(R.id.year);
        yearView.setText(Integer.toString(calendar.get(Calendar.YEAR)));

        try {
            initMonth((currentMonth),currentYear);
            mPresenter.loadDays();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initCalendar(){
        Calendar calendar = Calendar.getInstance();
        TextView monthView = findViewById(R.id.month_name);
        monthView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()));

        TextView yearView = findViewById(R.id.year);
        yearView.setText(Integer.toString(calendar.get(Calendar.YEAR)));

        try {
            initMonth((calendar.get(Calendar.MONTH) ),calendar.get(Calendar.YEAR));
            currentMonth = calendar.get(Calendar.MONTH) ;
            currentYear = calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //
        System.out.println("Current Year is : " + calendar.get(Calendar.YEAR));
        // month start from 0 to 11
        System.out.println("Current Month is : " + (calendar.get(Calendar.MONTH) + 1));
        System.out.println("Current Date is : " + calendar.get(Calendar.DATE));

    }

    private void initMonth(int month, int year) throws ParseException {
        calendar.set(year, month ,1);
        String monthName = new SimpleDateFormat("MMM").format(calendar.getTime());

        while (month==calendar.get(Calendar.MONTH)) {
            System.out.print(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        ArrayList<Day> days = new ArrayList<>();

        int lastDate = calendar.getActualMaximum(Calendar.DATE);

        for(int i =1; i<=lastDate; i++){
            Day day = new Day();
            day.setDay(String.valueOf(i));
            day.setMonth(String.valueOf(month));
            day.setYear(String.valueOf(year));
            day.setDayType(DayType.SIMPLE);
            day.setMonthShort(monthName);
            String dateString = String.format("%d-%d-%d", year, month +1, i);
            Date date = new SimpleDateFormat("yyyy-M-d").parse(dateString);

            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
            day.setDayName(dayOfWeek);

            days.add(day);
        }

        mDaysAdapter = new DaysAdapter(days, mItemListener, this,month, year);
        mDaysAdapter.notifyDataSetChanged();
        mDaysList.setAdapter(mDaysAdapter);

//        mDaysAdapter.

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
                        startActivity(new Intent(DaysActivity.this,
                                SettingsActivity.class));
                        break;
                    case R.id.drawer_info:
                        startActivity(new Intent(DaysActivity.this, InfoActivity.class));
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
    public void showDays(List<Day> days) {

    }

    @Override
    public void showEvents(List<Event> events) {
        mDaysAdapter.replaceData(events);
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

    }

    @Override
    public void showNoEvents() {

    }

    @Override
    public void focusOnDay(Day day, boolean focus) {
        mDaysAdapter.focusOnDay(day, focus);
        if( focusFlag == false) {
            mFab2.setVisibility(View.GONE);
            focusFlag = true;
        }
        else {
            mFab2.setVisibility(View.VISIBLE);
            focusFlag = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_NEW_EVENT || requestCode == REQUEST_CODE_EDIT_EVENT) {
            if (resultCode == RESULT_OK) {
                Event event = data.getParcelableExtra("event");
                Log.e(TAG, "Event set with id: " + event);
                showEventSetEvent(event);
                mPresenter.loadDays();
//                mDaysList.setAdapter(mDaysAdapter);
//                mDaysAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void reloadDay(Day day) {

    }

    @Override
    public void setPresenter(DaysContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void showPopup(boolean flag) {

    }

    @Override
    public UserProfile getProfile() {
        return mUserProfileContainer.getActiveUserProfile();
    }

    private void showEventSetEvent(final Event event) {
        KatsunaAlertBuilder builder = new KatsunaAlertBuilder(this);
        String title = getString(R.string.event_set);
        builder.setTitle(title);

        // calc and set message
        EventFormatter eventFormatter = new EventFormatter(this, event);
        builder.setMessage(eventFormatter.getEventMessage());
        builder.setView(R.layout.common_katsuna_alert);
        builder.setUserProfile(mUserProfileContainer.getActiveUserProfile());
        builder.setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String back = getString(R.string.back);
        builder.setCancelButtonText(back);
        builder.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEventDetailsUi(event.getEventId());
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }




//    private void adjustProfiles() {
//        UserProfile userProfile = mUserProfileContainer.getActiveUserProfile();
//
//        mPrimaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.PRIMARY_COLOR_2,
//                userProfile.colorProfile);
//        mSecondaryColor2 = ColorCalcV2.getColor(this, ColorProfileKeyV2.SECONDARY_COLOR_2,
//                userProfile.colorProfile);
//    }
}
