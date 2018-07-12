package com.scleroid.financematic.utils.roomconverters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by Ganesh on 27-11-2017.
 */

public class DateConverter {

	@Nullable
	@TypeConverter
	public static Date toDate(@Nullable Long timestamp) {
		return timestamp == null ? null : new Date(timestamp);
	}

	@Nullable
	@TypeConverter
	public static Long toTimestamp(@Nullable Date date) {
		return date == null ? null : date.getTime();
	}
}
