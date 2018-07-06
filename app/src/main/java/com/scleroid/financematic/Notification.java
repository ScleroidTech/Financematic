package com.scleroid.financematic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.financematic.data.tempModels.Passbook;
import com.scleroid.financematic.fragments.passbook.PassbookAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by scleroid on 11/4/18.
 */


/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 2/3/18
 */
public class Notification extends Fragment {

	@NonNull
	private List<Passbook> passbookList = new ArrayList<>();
	private RecyclerView recyclerView;
	private PassbookAdapter mAdapter;





	public Notification() {
		// Required empty public constructor
	}

	@NonNull
	public static Notification newInstance(String param1, String param2) {
		Notification fragment = new Notification();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.notification, container, false);


		AlarmManager alarmManager =
				(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		//AlarmReceiver notificationIntent = new AlarmReceiver();
		Intent notificationIntent = new Intent(this.getActivity(), AlarmReceiver.class);
		PendingIntent broadcast =
				PendingIntent.getBroadcast(this.getActivity(), 100, notificationIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 3);
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);


		return rootView;


	}


}

