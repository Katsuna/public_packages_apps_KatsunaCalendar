package com.katsuna.calendar.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Day implements Serializable{

    private String day;
    private String dayName;
    private String month;
    private String monthShort;
    private String year;
    private List<Event> events;
    private DayType dayType;

    public Day(){
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
}
