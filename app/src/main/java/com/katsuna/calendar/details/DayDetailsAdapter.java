package com.katsuna.calendar.details;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.events.EventItemListener;
import com.katsuna.calendar.formatters.DayFormatter;
import com.katsuna.calendar.formatters.EventFormatter;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DayDetailsAdapter extends BaseAdapter {

    private EventItemListener mEventListener;
    private final IUserProfileProvider mUserProfileProvider;
    private List<Event> mEvents;
    private UserProfile userProfile;
    private Calendar calendar;
    private Event mEventFocused;



    DayDetailsAdapter(List<Event> events, EventItemListener eventItemListener, IUserProfileProvider userProfileProvider) {
        calendar = Calendar.getInstance();

        calendar.setTime(calendar.getTime());
        System.out.println("Size" +events.size());
        setList(events);
        mEventListener = eventItemListener;
        mUserProfileProvider = userProfileProvider;
    }



    private void setList(List<Event> events) {
        mEvents = checkNotNull(events);
    }

    @Override
    public int getCount() {
        return  mEvents.size();
    }

    @Override
    public Event getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mEvents.get(position).getEventId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        userProfile = mUserProfileProvider.getProfile();

        View rowView = view;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (rowView == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.event, viewGroup, false);
        }
        final Event event = getItem(i);
        System.out.println("Event"+event.getMinute());
        final Context context = viewGroup.getContext();
        EventFormatter eventFormatter = new EventFormatter(context, event);

        String minutes = String.valueOf(event.getMinute());
        String hour = String.valueOf(event.getHour());
        if (event.getMinute() < 10) {
            minutes = "0"+minutes;
        }
        if (event.getHour() < 10) {
            hour = "0" + hour;
        }

        TextView title = rowView.findViewById(R.id.event_title);
        TextView subtitle = rowView.findViewById(R.id.event_subtitle);

        if( event.getEventType().equals(EventType.REMINDER)) {
            title.setText(event.getDescription());
            subtitle.setText(hour+":"+minutes);
        }
        else {
            title.setText(hour+":"+minutes);
            subtitle.setText("ALARM");
        }

        TextView day = rowView.findViewById(R.id.event_day);
        if(calendar.get(Calendar.DAY_OF_MONTH) == event.getDayOfMonth() && calendar.get(Calendar.MONTH) == event.getMonth() & calendar.get(Calendar.YEAR) == event.getYear()) {
            day.setText("Today");
        }
        else {
            day.setText(event.getDayOfMonth() + " " + getMonth(event.getMonth()) +", " +event.getYear());
        }


        CardView eventCard = rowView.findViewById(R.id.event_container_card);
        eventCard.setCardBackgroundColor(ContextCompat.getColor(context,
                eventFormatter.getCardHandleColor(userProfile)));
        View eventCardInner = rowView.findViewById(R.id.event_container_inner);
        eventCardInner.setBackgroundColor(ContextCompat.getColor(context,
                eventFormatter.getCardInnerColor(userProfile)));

        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.onEventFocus(event, !event.equals(mEventFocused));
            }
        });

        ImageView typeImage =  rowView.findViewById(R.id.event_type_image);
        Drawable icon = context.getDrawable(eventFormatter.getEventTypeIconResId());
        typeImage.setImageDrawable(icon);


        View actionsContainer = rowView.findViewById(R.id.event_buttons_container);
        if (mEventFocused !=null && event.equals(mEventFocused)) {
            actionsContainer.setVisibility(View.VISIBLE);
        } else {
            actionsContainer.setVisibility(View.GONE);
        }
        ViewGroup buttonsWrapper = rowView.findViewById(R.id.action_buttons_wrapper);
        RelativeLayout.LayoutParams paramsEnd = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paramsEnd.addRule(RelativeLayout.BELOW, actionsContainer.getId());

        if (userProfile.isRightHanded) {
            View buttonsView = inflater.inflate(R.layout.event_action_buttons_rh, buttonsWrapper,
                    false);
            buttonsWrapper.removeAllViews();
            buttonsWrapper.addView(buttonsView,paramsEnd);

        } else {
            View buttonsView = inflater.inflate(R.layout.event_action_buttons_lh, buttonsWrapper,
                    false);
            buttonsWrapper.removeAllViews();
            buttonsWrapper.addView(buttonsView,paramsEnd);
        }

        Button editBtn = rowView.findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.onEventEdit(event);
            }
        });
        buttonsWrapper.setVisibility(View.VISIBLE);

        TextView deletBtn = rowView.findViewById(R.id.txt_delete);
        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.onEventDelete(event);
            }
        });
        return rowView;
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public void focusOnEvent(Event event, boolean focus){
        mEventFocused = focus ? event : null;
        notifyDataSetChanged();
    }

    public void replaceData(List<Event> events) {
        setList(events);
        notifyDataSetChanged();
    }

}
