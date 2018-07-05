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


    public DaysAdapter(ArrayList<Day> days, DayItemListener itemListener, MainCalendarActivity mainCalendarActivity, IUserProfileProvider userProfileProvider) {
        setList(days);
        mItemListener = itemListener;
        mUserProfileProvider = userProfileProvider;
    }

    private void setList(List<Day> alarms) {
        mDays = checkNotNull(alarms);
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
//        DayFormatter alarmFormatter = new DayFormatter(context, day);
//
//        ImageView alarmTypeIcon = rowView.findViewById(R.id.alarm_type_image);
//        Drawable icon = context.getDrawable(alarmFormatter.getDayTypeIconResId());
//        alarmTypeIcon.setImageDrawable(icon);
//
//        TextView title = rowView.findViewById(R.id.alarm_title);
//        title.setText(alarmFormatter.getTitle());
//
//        TextView description = rowView.findViewById(R.id.alarm_description);
//        description.setText(alarmFormatter.getDescription());
//
//        TextView days = rowView.findViewById(R.id.alarm_days);
//        days.setText(alarmFormatter.getDays());
//
//        UserProfile userProfile = mUserProfileProvider.getProfile();
//
//        CardView alarmCard = rowView.findViewById(R.id.alarm_container_card);
//        alarmCard.setCardBackgroundColor(ContextCompat.getColor(context,
//                alarmFormatter.getCardHandleColor(userProfile)));
//
//        View alarmCardInner = rowView.findViewById(R.id.alarm_container_card_inner);
//        alarmCardInner.setBackgroundColor(ContextCompat.getColor(context,
//                alarmFormatter.getCardInnerColor(userProfile)));
//        alarmCardInner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemListener.onDayFocus(alarm, !alarm.equals(mDayFocused));
//            }
//        });
//
//        View actionsContainer = rowView.findViewById(R.id.alarm_buttons_container);
//        if (alarm.equals(mDayFocused)) {
//            actionsContainer.setVisibility(View.VISIBLE);
//        } else {
//            actionsContainer.setVisibility(View.GONE);
//        }
//
//        ViewGroup buttonsWrapper = rowView.findViewById(R.id.action_buttons_wrapper);
//        if (userProfile.isRightHanded) {
//            View buttonsView = inflater.inflate(R.layout.action_buttons_rh, buttonsWrapper,
//                    false);
//            buttonsWrapper.removeAllViews();
//            buttonsWrapper.addView(buttonsView);
//        } else {
//            View buttonsView = inflater.inflate(R.layout.action_buttons_lh, buttonsWrapper,
//                    false);
//            buttonsWrapper.removeAllViews();
//            buttonsWrapper.addView(buttonsView);
//        }
//
//        Button editButton = rowView.findViewById(R.id.button_edit);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemListener.onDayEdit(alarm);
//            }
//        });
//
//        final Button turnOffButton = rowView.findViewById(R.id.button_turn_off);
//        if (alarm.getDayStatus() == DayStatus.ACTIVE) {
//            turnOffButton.setText(R.string.turn_off);
//        } else {
//            turnOffButton.setText(R.string.turn_on);
//        }
//        turnOffButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (alarm.getDayStatus() == DayStatus.ACTIVE) {
//                    mItemListener.onDayStatusUpdate(alarm, DayStatus.INACTIVE);
//                } else {
//                    mItemListener.onDayStatusUpdate(alarm, DayStatus.ACTIVE);
//                }
//            }
//        });
//
//        TextView deleteText = rowView.findViewById(R.id.txt_delete);
//        deleteText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemListener.onDayDelete(alarm);
//            }
//        });

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
//
//
//        return rowView;
        return null;
    }
}
