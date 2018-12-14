package com.katsuna.calendar.details;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.katsuna.calendar.R;
import com.katsuna.calendar.data.Day;
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventType;
import com.katsuna.calendar.days.DayItemListener;
import com.katsuna.calendar.events.EventItemListener;
import com.katsuna.calendar.formatters.DayFormatter;
import com.katsuna.calendar.formatters.EventFormatter;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DayDetailsAdapter extends BaseAdapter {

    private EventItemListener mEventListener;
    private final IUserProfileProvider mUserProfileProvider;
    private List<Event> mEvents;
    private UserProfile userProfile;


    DayDetailsAdapter(List<Event> events, EventItemListener eventItemListener, IUserProfileProvider userProfileProvider) {
        setList(events);
        mEventListener = eventItemListener;
        mUserProfileProvider = userProfileProvider;
    }

    private void setList(List<Event> events) {
        mEvents = checkNotNull(events);
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Event getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        final Context context = viewGroup.getContext();
        EventFormatter eventFormatter = new EventFormatter(context, event);


        TextView title = rowView.findViewById(R.id.event_title);
        if( event.getEventType().equals(EventType.REMINDER)) {
            title.setText(event.getDescription());
        }

//        TextView description = rowView.findViewById(R.id.day_number);
//        description.setText(day.getDay());
//
//        TextView month = rowView.findViewById(R.id.month_short);
//        month.setText(day.getMonthShort());


        CardView dayCard = rowView.findViewById(R.id.day_container_card);


        return rowView;
    }
}
