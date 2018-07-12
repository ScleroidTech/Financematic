package com.scleroid.financematic.utils.roomconverters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/3/18
 */
public class MoneyConverter {
	@Nullable
	@TypeConverter
	public static BigDecimal toBigDecimal(@Nullable String number) {
		return number == null ? null : new BigDecimal(number).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	@Nullable
	@TypeConverter
	public static String toString(@Nullable BigDecimal number) {
		return number == null ? null : number.setScale(2, BigDecimal.ROUND_HALF_EVEN)
				.toPlainString();
	}
}
