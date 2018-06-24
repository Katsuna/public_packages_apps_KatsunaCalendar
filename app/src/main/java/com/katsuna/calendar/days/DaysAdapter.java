package com.katsuna.calendar.days;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.katsuna.calendar.data.Day;

import java.util.ArrayList;

class DaysAdapter extends BaseAdapter {

    public DaysAdapter(ArrayList<Day> days, DayItemListener mItemListener, MainCalendarActivity mainCalendarActivity) {
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
