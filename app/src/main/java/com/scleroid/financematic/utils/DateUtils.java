package com.scleroid.financematic.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class DateUtils {
    private Date date, time;

    @Inject
    public DateUtils() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date combineDateAndTime() {
        Calendar calendarDate = setCalendar(date);
        Calendar calendarTime = setCalendar(time);
        calendarTime.set(calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DATE));
        Date date = calendarTime.getTime();
        return date;

    }

    private Calendar setCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public CharSequence getFormattedDate(Date parcelDate) {
        return DateFormat.format("hh:mm AA, MMM dd, yyyy", parcelDate);
    }


}