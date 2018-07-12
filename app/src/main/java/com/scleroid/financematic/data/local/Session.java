package com.scleroid.financematic.data.local;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 6/19/18
 * Handles the initial amount of user holds in his account
 * Helpful to calculate how much he currently has and how much he can add
 */
public class Session {
	private static final String KEY_USER_TOTAL_AMOUNT = "total_amount";
	private SharedPreferences shref;
	private SharedPreferences.Editor editor;

	@Inject
	public Session(SharedPreferences shpref) {
		this.shref = shpref;
		this.editor = shref.edit();
		editor.apply();
	}

	/**
	 * Updates the amount adding the parameter to the current amount
	 *
	 * @param amount the amount which needs to be added
	 */
	public void updateAmount(float amount) {
		editor.putFloat(KEY_USER_TOTAL_AMOUNT, getAmount() + amount);
		editor.apply();
	}

	/**
	 * returns the current total amount,
	 * @return amount
	 */
	public float getAmount() {
		return shref.getFloat(KEY_USER_TOTAL_AMOUNT, 0);
	}
}
