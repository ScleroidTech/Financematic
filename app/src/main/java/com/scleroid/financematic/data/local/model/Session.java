package com.scleroid.financematic.data.local.model;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 6/19/18
 */
public class Session {
	public static final String KEY_USER_TOTAL_AMOUNT = "total_amount";
	private static final String TOTAL_AMOUNT = "total_amount";
	private Context context;
	private SharedPreferences.Editor editor;

	@Inject
	public Session(Context context, SharedPreferences.Editor editor) {
		this.context = context;
		this.editor = editor;
	}

	public void updateAmount(float amount) {
		editor.putFloat(KEY_USER_TOTAL_AMOUNT, amount);
	}
}
