package com.scleroid.financematic.utils.ui;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
		calendarTime.set(calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH),
				calendarDate.get(Calendar.DATE));
		Date date = calendarTime.getTime();
		return date;

	}

	private Calendar setCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public CharSequence getFormattedDate(Date parcelDate) {
		return DateFormat.format("MMM dd, yyyy", parcelDate);
	}

	public CharSequence getFormattedDate(Date date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	public boolean isThisDateWithinRange(Date date, int range) {

		//  SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		// sdf.setLenient(false);


		// if not valid, it will throw ParseException
		// Date date = sdf.parse(dateToValidate);

		// current date after 3 months
		Calendar dateAfterRange = Calendar.getInstance();
		dateAfterRange.add(Calendar.DATE, range);

		// current date
		Calendar currentDate = Calendar.getInstance();


		//ok everything is fine, date in range
		return date.before(dateAfterRange.getTime())
				&& date.after(currentDate.getTime());


	}

	public String differenceOfDates(Date date) {

		//  SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		// sdf.setLenient(false);


		// if not valid, it will throw ParseException
		// Date date = sdf.parse(dateToValidate);

		// current date after 3 months
		Calendar dateAfterAWeek = Calendar.getInstance();
		dateAfterAWeek.add(Calendar.DATE, 7);

		// current date
		Calendar currentDate = Calendar.getInstance();
		Date currentDateTime = currentDate.getTime();

		String diff = "";
		long timeDiff = Math.abs(date.getTime() - currentDateTime.getTime());
		diff = String.format("%d day(s) to go", TimeUnit.MILLISECONDS.toDays(timeDiff));
		return diff;
		//ChronoUnit.DAYS.between(firstDate, secondDate);
		//ok everything is fine, date in range


	}


}