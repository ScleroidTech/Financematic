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
	public static BigDecimal toBigDecimal(String number) {
		return number == null ? null : new BigDecimal(number).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	@TypeConverter
	public static String toString(BigDecimal number) {
		return number == null ? null : number.setScale(2, BigDecimal.ROUND_HALF_EVEN)
				.toPlainString();
	}
}
