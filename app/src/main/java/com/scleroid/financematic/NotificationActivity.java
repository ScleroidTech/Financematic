package com.scleroid.financematic;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scleroid.financematic.adapter.PassbookAdapter;
import com.scleroid.financematic.data.tempModels.Passbook;
import com.scleroid.financematic.fragments.passbook.PassbookFragment;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 16/4/18.
 */


        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import com.scleroid.financematic.R;
        import com.scleroid.financematic.adapter.PassbookAdapter;

        import java.util.ArrayList;
        import java.util.List;


/**
 * Copyright (C) 2018
 * @author Ganesh Kaple
 * @since 2/3/18
 */

public class NotificationActivity extends Fragment {

    public NotificationActivity() {
        // Required empty public constructor
    }

    public static NotificationActivity newInstance(String param1, String param2) {
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

;

        return rootView;



    }


}
