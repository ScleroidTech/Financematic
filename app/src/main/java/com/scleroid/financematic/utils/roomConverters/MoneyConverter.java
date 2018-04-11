package com.scleroid.financematic.utils.roomConverters;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/3/18
 */
public class MoneyConverter {
	@TypeConverter
	public static BigDecimal toBigDecimal(double number) {
		return number == 0 ? null : new BigDecimal(number);
	}

	@TypeConverter
	public static double toDouble(BigDecimal number) {
		return number == null ? 0 : number.doubleValue();
	}
}
