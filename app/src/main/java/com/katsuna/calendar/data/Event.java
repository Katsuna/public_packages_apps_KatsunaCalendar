package com.katsuna.calendar.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.gson.Gson;

import java.util.Calendar;

@Entity(tableName = "events")
public final class Event implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eventId")
    private long mEventId;

    @NonNull
    @ColumnInfo(name = "description")
    private final String mDescription;


    @NonNull
    @ColumnInfo(name = "type")
    private final EventType mEventType;

    @NonNull
    @ColumnInfo(name = "month")
    private final Integer mMonth;

    @NonNull
    @ColumnInfo(name = "dayOfMonth")
    private final Integer mDayOfMonth;

    @NonNull
    @ColumnInfo(name = "year")
    private final Integer mYear;

    @Nullable
    @ColumnInfo(name = "hour")
    private final Integer mHour;

    @Nullable
    @ColumnInfo(name = "minute")
    private final Integer mMinute;

    @NonNull
    @ColumnInfo(name = "status")
    private EventStatus mEventStatus = EventStatus.ACTIVE;

    @Ignore
    public Event(@NonNull EventType mEventType, @NonNull Integer mMonth, @NonNull Integer mDayOfMonth,@NonNull String mDescription) {
        this(0,mEventType, mMonth,mDayOfMonth, Calendar.getInstance().get(Calendar.YEAR),
                0,1,mDescription, EventStatus.ACTIVE);

    }

    @Ignore
    public Event(@NonNull EventType eventType,  @NonNull Integer month,  @NonNull Integer dayOfMonth,@NonNull Integer year, @NonNull Integer hour,
                 @NonNull Integer minute, @Nullable String description,
                 @NonNull EventStatus eventStatus) {
        this(0, eventType,month,dayOfMonth, year, hour, minute, description, eventStatus);
    }

    public Event(long mEventId, @NonNull EventType mEventType,
                 @NonNull Integer mMonth, @NonNull Integer mDayOfMonth, @NonNull Integer mYear,
                 @NonNull Integer mHour,@NonNull  Integer mMinute,@NonNull String mDescription, @NonNull EventStatus eventStatus) {
        this.mEventId = mEventId;
        this.mDescription = mDescription;
        this.mEventType = mEventType;
        this.mMonth = mMonth;
        this.mDayOfMonth = mDayOfMonth;
        this.mYear = mYear;
        this.mHour = mHour;
        this.mMinute = mMinute;
        this.mEventStatus = eventStatus;
    }

    protected Event(Parcel in) {
        mEventId = in.readLong();
        mDescription = in.readString();
        int tmpMEventType = in.readInt();

        this.mEventType = EventType.values()[tmpMEventType];

        if (in.readByte() == 0) {
            mMonth = null;
        } else {
            mMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            mDayOfMonth = null;
        } else {
            mDayOfMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            mYear = null;
        } else {
            mYear = in.readInt();
        }
        if (in.readByte() == 0) {
            mHour = null;
        } else {
            mHour = in.readInt();
        }
        if (in.readByte() == 0) {
            mMinute = null;
        } else {
            mMinute = in.readInt();
        }
        int tmpMEventStatus = in.readInt();
        this.mEventStatus = EventStatus.values()[tmpMEventStatus];
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public long getEventId() {
        return mEventId;
    }

    public void setEventId(long mEventId) {
        this.mEventId = mEventId;
    }


    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @NonNull
    public Integer getMonth() {
        return mMonth;
    }

    @NonNull
    public Integer getDayOfMonth() {
        return mDayOfMonth;
    }

    @Nullable
    public Integer getHour() {
        return mHour;
    }

    @Nullable
    public Integer getMinute() {
        return mMinute;
    }

    @NonNull
    public EventType getEventType() {
        return mEventType;
    }

    @NonNull
    public Integer getYear() {
        return mYear;
    }

    @NonNull
    public EventStatus getEventStatus() {
        return mEventStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            if( o.getClass() == Day.class) {
                Day day = (Day) o;
                return (Integer.parseInt(day.getDay())== mDayOfMonth && Integer.parseInt(day.getMonth()) == mMonth && Integer.parseInt(day.getYear()) == mYear);
            }
            return false;
        }
        else {
            Event event = (Event) o;
            return Objects.equal(mEventId, event.mEventId) &&
                    Objects.equal(mEventType, event.mEventType) &&
                    Objects.equal(mDescription, event.mDescription) &&
                    Objects.equal(mDayOfMonth, event.mDayOfMonth) &&
                    Objects.equal(mHour, event.mHour) &&
                    Objects.equal(mMinute, event.mMinute) &&
                    Objects.equal(mMonth, event.mMonth) &&
                    Objects.equal(mYear, event.mYear) &&
                    Objects.equal(mEventStatus, event.mEventStatus);
        }
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

    public void setEventStatus(@NonNull EventStatus mEventStatus) {
        this.mEventStatus = mEventStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mEventId);
        dest.writeString(mDescription);
        dest.writeInt(this.mEventType.ordinal());

        if (mMonth == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mMonth);
        }
        if (mDayOfMonth == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mDayOfMonth);
        }
        if (mYear == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mYear);
        }
        if (mHour == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mHour);
        }
        if (mMinute == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mMinute);
        }
        dest.writeInt(this.mEventStatus.ordinal());

    }
}
