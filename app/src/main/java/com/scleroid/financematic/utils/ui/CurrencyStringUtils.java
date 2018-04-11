package com.scleroid.financematic.utils.ui;

import javax.inject.Inject;

public class CurrencyStringUtils {
	@Inject
	public CurrencyStringUtils() {
	}

	public String bindNumber(String amount) {
		return String.format("₹ %s", amount);
	}

	public String bindNumber(int amount) {
		return String.format("₹ %d", amount);
	}
}