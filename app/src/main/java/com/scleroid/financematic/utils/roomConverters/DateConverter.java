package com.scleroid.financematic.utils.roomConverters;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Ganesh on 27-11-2017.
 */

public class DateConverter {
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
	@TypeConverter
	public static Date toDate(String timestamp) {
		Date date = null;
		try {
			date = dateFormat.parse(timestamp);
		} catch (ParseException e) {
			Timber.e(
					"Exception occurred while parsing date" + timestamp + " error is " + e
							.getMessage() + " " + e
							.getErrorOffset());
		}
		return date;
	}

	@TypeConverter
	public static String toTimestamp(Date date) {
		// Create an instance of SimpleDateFormat used for formatting
// the string representation of date (month/day/year)


// Using DateFormat format method we can create a string
// representation of a date with the defined format.


		return dateFormat.format(date);
	}
}
