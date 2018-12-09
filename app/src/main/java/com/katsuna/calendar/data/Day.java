package com.katsuna.calendar.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Day implements Parcelable {

    private String day;
    private String dayName;
    private String month;
    private String monthShort;
    private String year;

    private List<Event> events;
    private DayType dayType;

    public Day(){
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    protected Day(Parcel in) {
        day = in.readString();
        dayName = in.readString();
        month = in.readString();
        monthShort = in.readString();
        year = in.readString();


        this.events = new ArrayList<Event>();
        in.readTypedList(events, Event.CREATOR);
        int tmpDayType = in.readInt();
        this.dayType = DayType.values()[tmpDayType];
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMonthShort() {
        return monthShort;
    }

    public void setMonthShort(String monthShort) {
        this.monthShort = monthShort;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(dayName);
        dest.writeString(month);
        dest.writeString(monthShort);
        dest.writeString(year);
        dest.writeTypedList(events);

        dest.writeInt(this.dayType.ordinal());


    }
}
