package com.katsuna.calendar.details;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.katsuna.calendar.data.Event;
import com.katsuna.calendar.events.EventItemListener;
import com.katsuna.commons.utils.IUserProfileProvider;

import java.util.ArrayList;
import java.util.List;

public class DayDetailsAdapter extends BaseAdapter {

    DayDetailsAdapter(List<Event> events, EventItemListener eventItemListener, IUserProfileProvider userProfileProvider) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
