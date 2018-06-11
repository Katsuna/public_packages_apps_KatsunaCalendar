package com.katsuna.calendar.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.gson.Gson;

import java.util.Calendar;

@Entity(tableName = "events")
public final class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eventId")
    private long mEventId;

//    @NonNull
//    @ColumnInfo(name = "name")
//    private String mName;
//
    @NonNull
    @ColumnInfo(name = "description")
    private final String mDescription;


    @NonNull
    @ColumnInfo(name = "type")
    private final EventType mEventType;

    @NonNull
    @ColumnInfo(name = "month")
    private final int mMonth;

    @NonNull
    @ColumnInfo(name = "dayOfMonth")
    private final int mDayOfMonth;

    @NonNull
    @ColumnInfo(name = "year")
    private final int mYear;

    @Nullable
    @ColumnInfo(name = "startTime")
    private final String mHour;

    @Nullable
    @ColumnInfo(name = "startTime")
    private final String mMinute;

    @Nullable
    @ColumnInfo(name = "endTime")
    private final String mEndTime;

    @Ignore
    public Event(@NonNull String mDescription,EventType mEventType, @NonNull int mMonth, @NonNull int mDayOfMonth) {
        this(0, mDescription,mEventType, mMonth,mDayOfMonth, Calendar.getInstance().get(Calendar.YEAR), "0","1","24:00");

    }

    public Event(long mEventId, @NonNull String mDescription, @NonNull EventType mEventType, @NonNull int mMonth, @NonNull int mDayOfMonth, @NonNull int mYear, String mHour, String mMinute, String mEndTime) {
        this.mEventId = mEventId;
//        this.mName = mName;
        this.mDescription = mDescription;
        this.mEventType = mEventType;
        this.mMonth = mMonth;
        this.mDayOfMonth = mDayOfMonth;
        this.mYear = mYear;
        this.mHour = mHour;
        this.mMinute = mMinute;
        this.mEndTime = mEndTime;
    }

    public long getEventId() {
        return mEventId;
    }

    public void setEventId(long mEventId) {
        this.mEventId = mEventId;
    }

//    @NonNull
//    public String getmName() {
//        return mName;
//    }
//
//    public void setmName(@NonNull String mName) {
//        this.mName = mName;
//    }

    @Nullable
    public String getmDescription() {
        return mDescription;
    }

    @NonNull
    public int getmMonth() {
        return mMonth;
    }

    @NonNull
    public int getmDayOfMonth() {
        return mDayOfMonth;
    }

    @Nullable
    public String getmHour() {
        return mHour;
    }

    @Nullable
    public String getmMinute() {
        return mMinute;
    }

    @Nullable
    public String getmEndTime() {
        return mEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equal(mEventId, event.mEventId) &&
                Objects.equal(mEventType, event.mEventType) &&
                Objects.equal(mDescription, event.mDescription) &&
                Objects.equal(mDayOfMonth, event.mDayOfMonth) &&
                Objects.equal(mHour, event.mHour) &&
                Objects.equal(mEndTime, event.mEndTime) &&
                Objects.equal(mMonth, event.mMonth);
    }

    @Override
    public int hashCode() {
        return Long.valueOf(mEventId).hashCode();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
