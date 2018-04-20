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
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	@TypeConverter
	public static Date toDate(String timestamp) {
		Date date = null;
		try {
			Timber.d(
					"converting Date " + timestamp);
			date = dateFormat.parse(timestamp.trim());
			return date;
		} catch (ParseException e) {
			Timber.e(
					"Exception occurred while parsing date" + timestamp + " error is " + e
							.getMessage() + " " + e
							.getErrorOffset());
		} catch (ArrayIndexOutOfBoundsException e) {
			Timber.e(
					"Exception occurred while parsing date Out of Bound " + timestamp + " error is" +
							" " +
							"" + e
							.getMessage() + " ");
			String[] str = timestamp.trim().split("/");
			Timber.e(
					"Exception  Let's see how the string looks  " + str.length + " " + str[0] +
							str[1] + str[2]);
			Date newDate = new Date();
			newDate.setDate(Integer.parseInt(str[0]));
			newDate.setMonth(Integer.parseInt(str[1]));
			newDate.setYear(Integer.parseInt(str[2]));
			Timber.e(
					"Handled the exception like this,  " + dateFormat.format(newDate));
			return newDate;



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
