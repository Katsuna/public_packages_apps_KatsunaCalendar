package com.katsuna.calendar.events;

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
import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.data.EventStatus;
import com.katsuna.calendar.formatters.EventFormatter;
import com.katsuna.commons.entities.OpticalParams;
import com.katsuna.commons.entities.SizeProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.IUserProfileProvider;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.SizeCalcV2;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventsAdapter extends BaseAdapter {

    private final EventItemListener mItemListener;
    private final IUserProfileProvider mUserProfileProvider;
    private List<Event> mEvents;
    private Event mEventFocused;

    public EventsAdapter(List<Event> tasks, EventItemListener itemListener,
                  IUserProfileProvider userProfileProvider) {
        setList(tasks);
        mItemListener = itemListener;
        mUserProfileProvider = userProfileProvider;
    }

    public void focusOnEvent(Event event, boolean focus) {
        mEventFocused = focus ? event : null;
        notifyDataSetChanged();
    }

    public void reloadEvent(Event event) {
        int itemIndex = mEvents.indexOf(event);
        if (itemIndex != -1) {
            mEvents.set(itemIndex, event);
        }

        notifyDataSetChanged();
    }

    public void replaceData(List<Event> events) {
        setList(events);
        notifyDataSetChanged();
    }

    private void setList(List<Event> events) {
        mEvents = checkNotNull(events);
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Event getItem(int i) {
        return mEvents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.event, viewGroup, false);
        }

        final Event event = getItem(i);

        final Context context = viewGroup.getContext();
        EventFormatter eventFormatter = new EventFormatter(context, event);

        ImageView eventTypeIcon = rowView.findViewById(R.id.event_type_image);
        Drawable icon = context.getDrawable(eventFormatter.getEventTypeIconResId());
        eventTypeIcon.setImageDrawable(icon);

        TextView title = rowView.findViewById(R.id.event_title);
        title.setText(eventFormatter.getTitle());

        TextView description = rowView.findViewById(R.id.event_description);
        description.setText(eventFormatter.getDescription());



        UserProfile userProfile = mUserProfileProvider.getProfile();

        CardView eventCard = rowView.findViewById(R.id.event_container_card);
        eventCard.setCardBackgroundColor(ContextCompat.getColor(context,
                eventFormatter.getCardHandleColor(userProfile)));

        View eventCardInner = rowView.findViewById(R.id.event_container_card_inner);
        eventCardInner.setBackgroundColor(ContextCompat.getColor(context,
                eventFormatter.getCardInnerColor(userProfile)));
        eventCardInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onEventFocus(event, !event.equals(mEventFocused));
            }
        });

        View actionsContainer = rowView.findViewById(R.id.event_buttons_container);
        if (event.equals(mEventFocused)) {
            actionsContainer.setVisibility(View.VISIBLE);
        } else {
            actionsContainer.setVisibility(View.GONE);
        }

        Button editButton = rowView.findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onEventEdit(event);
            }
        });

        final Button turnOffButton = rowView.findViewById(R.id.button_turn_off);
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

        TextView deleteText = rowView.findViewById(R.id.txt_delete);
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onEventDelete(event);
            }
        });

        // adjust buttons
        OpticalParams opticalParams = SizeCalcV2.getOpticalParams(SizeProfileKeyV2.BUTTON,
                userProfile.opticalSizeProfile);
        SizeAdjuster.adjustText(context, editButton, opticalParams);
        SizeAdjuster.adjustText(context, turnOffButton, opticalParams);

        // more text
        opticalParams = SizeCalcV2.getOpticalParams(SizeProfileKeyV2.BUTTON,
                userProfile.opticalSizeProfile);
        SizeAdjuster.adjustText(context, deleteText, opticalParams);

        ColorAdjusterV2.adjustButtons(context, userProfile, editButton, turnOffButton, deleteText);


        return rowView;
    }
}