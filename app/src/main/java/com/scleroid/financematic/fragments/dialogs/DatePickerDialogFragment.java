package com.scleroid.financematic.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import android.support.v7.app.AlertDialog;

/**
 * Copyright (C)
 *
 * @author Ganesh Kaple
 * @since 09-01-2016
 */
public class DatePickerDialogFragment extends BaseDialog {
	public static final String EXTRA_DATE = "com.example.ganesh.criminalintent.date";
	private static final String ARG_DATE = "crime_date";
	private DatePicker mDatePicker;


	@NonNull
	public static DatePickerDialogFragment newInstance(Date date) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_DATE, date);

		DatePickerDialogFragment fragment = new DatePickerDialogFragment();
		fragment.setArguments(bundle);
		return fragment;


	}

	@NonNull
	public static DatePickerDialogFragment newInstance() {
		Bundle bundle = new Bundle();
		//  bundle.putSerializable(ARG_DATE, date);

		DatePickerDialogFragment fragment = new DatePickerDialogFragment();
		fragment.setArguments(bundle);
		return fragment;


	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_DATE);
		Calendar calendar = Calendar.getInstance();
		if (date != null) { calendar.setTime(date); }

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
		mDatePicker = v.findViewById(R.id.dialog_date_picker);
		mDatePicker.init(year, month, day, null);

		return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title)
				.setView(v)
				.setPositiveButton(android.R.string.ok, (dialog, which) -> {
					int year1 = mDatePicker.getYear();
					int month1 = mDatePicker.getMonth();
					int day1 = mDatePicker.getDayOfMonth();
					Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
					sendResult(Activity.RESULT_OK, date1);
				})
				.create();
	}

	private void sendResult(int ResultCode, Date date) {
		if (getTargetFragment() == null) return;
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, date);
		getTargetFragment().onActivityResult(getTargetRequestCode(), ResultCode, intent);
	}

/*
	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
	}
*/


}
