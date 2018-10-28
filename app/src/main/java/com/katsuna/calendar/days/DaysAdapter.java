package com.katsuna.calendar.days;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.DayType;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.events.EventItemListener;
import com.katsuna.calendar.events.EventsContract;
import com.katsuna.calendar.formatters.DayFormatter;
import com.katsuna.calendar.formatters.EventFormatter;
import com.katsuna.commons.entities.OpticalParams;
import com.katsuna.commons.entities.SizeProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.IUserProfileProvider;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.SizeCalcV2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

class DaysAdapter extends BaseAdapter {

    private final DayItemListener mItemListener;
//    private EventItemListener mEventListener;
    private final IUserProfileProvider mUserProfileProvider;
    private List<Day> mDays;
    private int mMonth;
    private int mYear;
    private Day mDayFocused;
    private Calendar calendar;
    private UserProfile userProfile;
    private EventsContract.Presenter mPresenter;




    DaysAdapter(ArrayList<Day> days, DayItemListener itemListener, IUserProfileProvider userProfileProvider, int month, int year) {
        calendar = Calendar.getInstance();

        calendar.setTime(calendar.getTime());

        setList(days);
        mItemListener = itemListener;
        mUserProfileProvider = userProfileProvider;
        mMonth = month;
        mYear = year;
    }

    private void setList(List<Day> days) {
        mDays = checkNotNull(days);
    }

    public void focusOnDay(Day day, boolean focus){
        mDayFocused = focus ? day : null;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDays.size();
    }

    @Override
    public Day getItem(int position) {
        return mDays.get(position);
    }

    @Override
    public long getItemId(int position) {
      return Long.parseLong(mDays.get(position).getDay());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        userProfile = mUserProfileProvider.getProfile();

        View rowView = view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (rowView == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.day, viewGroup, false);
        }
        final Day day = getItem(i);

        final Context context = viewGroup.getContext();
        DayFormatter dayFormatter = new DayFormatter(context, day);


        TextView title = rowView.findViewById(R.id.day_name);
        title.setText(day.getDayName());

        TextView description = rowView.findViewById(R.id.day_number);
        description.setText(day.getDay());

        TextView month = rowView.findViewById(R.id.month_short);
        month.setText(day.getMonthShort());


        CardView dayCard = rowView.findViewById(R.id.day_container_card);

        System.out.println("the user profile is" + userProfile);
        if (calendar.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(day.getDay()) && calendar.get(Calendar.MONTH) == mMonth & calendar.get(Calendar.YEAR) == mYear) {
            day.setDayType(DayType.CURRENT);
        } else if (day.getEvents() != null && !day.getEvents().isEmpty()) {
            day.setDayType(DayType.WITH_EVENT);
        } else {
            day.setDayType(DayType.SIMPLE);
        }
        dayCard.setCardBackgroundColor(ContextCompat.getColor(context,
                dayFormatter.getCardHandleColor(userProfile)));
        View alarmCardInner = rowView.findViewById(R.id.date_container_inner);
        alarmCardInner.setBackgroundColor(ContextCompat.getColor(context,
                dayFormatter.getCardInnerColor(userProfile)));


        dayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onDayFocus(day, !day.equals(mDayFocused));
            }
        });

        View actionsContainer = rowView.findViewById(R.id.day_buttons_container);
        if (day.equals(mDayFocused)) {
            actionsContainer.setVisibility(View.VISIBLE);
        } else {
            actionsContainer.setVisibility(View.GONE);
        }

        ViewGroup buttonsWrapper = rowView.findViewById(R.id.action_buttons_wrapper);
            if (day.getDayType().equals(DayType.SIMPLE)) {
                if (userProfile.isRightHanded) {
                        View buttonsView = inflater.inflate(R.layout.add_button_rh, buttonsWrapper,
                                false);
                        buttonsWrapper.removeAllViews();
                        buttonsWrapper.addView(buttonsView);

                } else {
                    View buttonsView = inflater.inflate(R.layout.add_button_lh, buttonsWrapper,
                            false);
                    buttonsWrapper.removeAllViews();
                    buttonsWrapper.addView(buttonsView);
                }

                Button addNewBtn = rowView.findViewById(R.id.add_new_button);
                addNewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemListener.onDayAddEvent(day);
                    }
                });
                buttonsWrapper.setVisibility(View.VISIBLE);

            }


        if (day.getDayType().equals(DayType.WITH_EVENT)) {
            TextView eventInfo = rowView.findViewById(R.id.day_events_info);
            eventInfo.setText(R.string.avail_events);
            if (userProfile.isRightHanded) {
                buttonsWrapper.removeAllViews();

                RelativeLayout.LayoutParams paramsEnd = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int count =0;
                for(final Event event: day.getEvents()) {
                    EventFormatter eventFormatter = new EventFormatter(context, event);

                    View eventDetails = inflater.inflate(R.layout.event_info_rh, buttonsWrapper,
                            false);
                    ImageView typeImage =  eventDetails.findViewById(R.id.alarm_type_image);
                    Drawable icon = context.getDrawable(eventFormatter.getEventTypeIconResId());
                    typeImage.setImageDrawable(icon);

                    TextView eventDescription = eventDetails.findViewById(R.id.event_description);
                    eventDescription.setText(event.getDescription()+", " +event.getHour()+":"+event.getMinute());


                    if( count == 0) {
                        buttonsWrapper.addView(eventDetails);
                    }
                    else {
                        buttonsWrapper.addView(eventDetails, paramsEnd);
                    }
//                    ToggleButton toggleButton = eventDetails.findViewById(R.id.event_description);
                    final Button turnOffButton = eventDetails.findViewById(R.id.event_status_button);
                    if (event.getEventStatus() == EventStatus.ACTIVE) {
                        turnOffButton.setText(R.string.turn_off);
                    } else {
                        turnOffButton.setText(R.string.turn_on);
                    }
                    turnOffButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (event.getEventStatus() == EventStatus.ACTIVE) {
                                mItemListener.onEventStatusUpdate(event, EventStatus.INACTIVE);
                            } else {
                                mItemListener.onEventStatusUpdate(event, EventStatus.ACTIVE);
                            }
                        }
                    });

                    paramsEnd.addRule(RelativeLayout.BELOW, eventDetails.getId());
                }
                View buttonsView = inflater.inflate(R.layout.action_buttons_rh, buttonsWrapper,
                        false);
                buttonsWrapper.addView(buttonsView,paramsEnd);

            } else {
                View buttonsView = inflater.inflate(R.layout.action_buttons_lh, buttonsWrapper,
                        false);
                buttonsWrapper.removeAllViews();
                buttonsWrapper.addView(buttonsView);
            }


//            Button editButton = rowView.findViewById(R.id.button_edit);
//            editButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItemListener.onDayAddEvent(day);
//                }
//            });

            final Button turnOffButton = rowView.findViewById(R.id.button_turn_off);
//            if (alarm.getAlarmStatus() == AlarmStatus.ACTIVE) {
//
//
//                turnOffButton.setText(R.string.turn_off);
//            } else {
//                turnOffButton.setText(R.string.turn_on);
//            }

        }
//        turnOffButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (alarm.getAlarmStatus() == AlarmStatus.ACTIVE) {
//                    mItemListener.onAlarmStatusUpdate(alarm, AlarmStatus.INACTIVE);
//                } else {
//                    mItemListener.onAlarmStatusUpdate(alarm, AlarmStatus.ACTIVE);
//                }
//            }
//        });

//        TextView deleteText = rowView.findViewById(R.id.txt_delete);
//        deleteText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemListener.onDayEventDelete(event);
//            }
//        });
//
//        // adjust buttons
//        OpticalParams opticalParams = SizeCalcV2.getOpticalParams(SizeProfileKeyV2.BUTTON,
//                userProfile.opticalSizeProfile);
//        SizeAdjuster.adjustText(context, editButton, opticalParams);
//        SizeAdjuster.adjustText(context, turnOffButton, opticalParams);
//
//        // more text
//        opticalParams = SizeCalcV2.getOpticalParams(SizeProfileKeyV2.BUTTON,
//                userProfile.opticalSizeProfile);
//        SizeAdjuster.adjustText(context, deleteText, opticalParams);
//
//        ColorAdjusterV2.adjustButtons(context, userProfile, editButton, turnOffButton, deleteText);


        return rowView;
    }

    public void replaceData(List<Event> events) {
        /***** TODO ADD EVENTS****/
        System.out.println("IM in replace Data");
        for (Event event: events){
            if(event.getYear() == mYear && event.getMonth() == mMonth) {
                for(Day day: mDays){
                    if(Integer.parseInt(day.getDay()) == event.getDayOfMonth()){
                        if (mDays.get(event.getDayOfMonth()).getEvents() == null) {
                            List<Event> dayEvents = new ArrayList<>();
                            dayEvents.add(event);
                            mDays.get(event.getDayOfMonth()).setEvents(dayEvents);
                        }
                        else {
                            mDays.get(event.getDayOfMonth()).getEvents().add(event);
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
