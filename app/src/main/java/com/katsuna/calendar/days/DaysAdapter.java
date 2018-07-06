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
    private Day mDayFocused;


    public DaysAdapter(ArrayList<Day> days, DayItemListener itemListener, IUserProfileProvider userProfileProvider, int month, int year) {
        setList(days);
        mItemListener = itemListener;
        mUserProfileProvider = userProfileProvider;
    }

    private void setList(List<Day> days) {
        mDays = checkNotNull(days);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Day getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
        title.setText(alarmFormatter.getName());

        TextView description = rowView.findViewById(R.id.alarm_description);
        description.setText(alarmFormatter.getDescription());



        UserProfile userProfile = mUserProfileProvider.getProfile();

        CardView alarmCard = rowView.findViewById(R.id.day_container_card);
        alarmCard.setCardBackgroundColor(ContextCompat.getColor(context,
                alarmFormatter.getCardHandleColor(userProfile)));

        View alarmCardInner = rowView.findViewById(R.id.day_container_card);
        alarmCardInner.setBackgroundColor(ContextCompat.getColor(context,
                alarmFormatter.getCardHandleColor(userProfile)));
        alarmCardInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onDayFocus(day, !day.equals(mDayFocused));
            }
        });


        ViewGroup buttonsWrapper = rowView.findViewById(R.id.action_buttons_wrapper);
        if (userProfile.isRightHanded) {

        } else {

        }




        // adjust buttons
        OpticalParams opticalParams = SizeCalcV2.getOpticalParams(SizeProfileKeyV2.BUTTON,
                userProfile.opticalSizeProfile);
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
}
