package com.katsuna.calendar.days;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.formatters.DayFormatter;
import com.katsuna.commons.entities.OpticalParams;
import com.katsuna.commons.entities.SizeProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.IUserProfileProvider;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.SizeCalcV2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

class DaysAdapter extends BaseAdapter {

    private final DayItemListener mItemListener;
    private final IUserProfileProvider mUserProfileProvider;
    private List<Day> mDays;
    private int mMonth;
    private int mYear;
    private Day mDayFocused;


    public DaysAdapter(ArrayList<Day> days, DayItemListener itemListener, IUserProfileProvider userProfileProvider, int month, int year) {
        setList(days);
        mItemListener = itemListener;
        mUserProfileProvider = userProfileProvider;
        mMonth = month;
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
        View rowView = view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (rowView == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.day, viewGroup, false);
        }

        final Day day = getItem(i);

        final Context context = viewGroup.getContext();
        DayFormatter alarmFormatter = new DayFormatter(context, day);



        TextView title = rowView.findViewById(R.id.day_name);
        title.setText(day.getDayName());

        TextView description = rowView.findViewById(R.id.day_number);
        description.setText(day.getDay());

        TextView month = rowView.findViewById(R.id.month_short);
        month.setText(day.getMonthShort());

        UserProfile userProfile = mUserProfileProvider.getProfile();

        CardView dayCard = rowView.findViewById(R.id.day_container_card);
////        alarmCard.setCardBackgroundColor(ContextCompat.getColor(context,
////                alarmFormatter.getCardHandleColor(userProfile)));
//
//        View alarmCardInner = rowView.findViewById(R.id.date_container);
////        alarmCardInner.setBackgroundColor(ContextCompat.getColor(context,
////                alarmFormatter.getCardHandleColor(userProfile)));
        dayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onDayFocus(day, !day.equals(mDayFocused));
            }
        });

        View actionsContainer = rowView.findViewById(R.id.event_buttons_container);
        if (day.equals(mDayFocused)) {
            Button addNewBtn = rowView.findViewById(R.id.add_new_button);
            addNewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onDayAddEvent(day);
                }
            });
            actionsContainer.setVisibility(View.VISIBLE);
        } else {
            actionsContainer.setVisibility(View.GONE);
        }

        ViewGroup buttonsWrapper = rowView.findViewById(R.id.action_buttons_wrapper);
//        if (userProfile.isRightHanded) {
//
//        } else {
//
//        }




        // adjust buttons
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
                if (mDays.get(event.getDayOfMonth()).getEvents() == null) {
                    List<Event> dayEvents = new ArrayList<>();
                    dayEvents.add(event);
                    mDays.get(event.getDayOfMonth()).setEvents(dayEvents);
                    //mDays.get
                }
                else {
                    mDays.get(event.getDayOfMonth()).getEvents().add(event);
                }
            }
        }
        notifyDataSetChanged();
    }
}
