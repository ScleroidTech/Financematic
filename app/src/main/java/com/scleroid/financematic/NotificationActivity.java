package com.scleroid.financematic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by scleroid on 16/4/18.
 */


/**
 * Copyright (C) 2018
 * @author Ganesh Kaple
 * @since 2/3/18
 */

public class NotificationActivity extends Fragment {

    public NotificationActivity() {
        // Required empty public constructor
    }

	public static NotificationActivity newInstance() {
        NotificationActivity fragment = new NotificationActivity();
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
        View rootView =  inflater.inflate(R.layout.notification_activity, container, false);



        return rootView;



    }


}
