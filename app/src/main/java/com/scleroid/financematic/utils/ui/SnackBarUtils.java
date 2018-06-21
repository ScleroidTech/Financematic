package com.scleroid.financematic.utils.ui;

import android.support.design.widget.Snackbar;
import android.view.View;

import javax.inject.Inject;

public class SnackBarUtils {
	@Inject
	public SnackBarUtils() {
	}

	public void showSnackbar(View view, int msg) {

		Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
	}
}