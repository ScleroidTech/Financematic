package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.financematic.R;

/**
 * Created by scleroid on 2/3/18.
 * <p>
 * Created by scleroid on 2/3/18.
 * <p>
 * Created by scleroid on 2/3/18.
 */
/**
 * Created by scleroid on 2/3/18.
 */
/**
 * Created by scleroid on 2/3/18.
 */

public class ReminderFragment extends Fragment {

    public ReminderFragment() {
        // Required empty public constructor
    }

    public static ReminderFragment newInstance(String param1, String param2) {
        ReminderFragment fragment = new ReminderFragment();
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
        return inflater.inflate(R.layout.reminders, container, false);
    }
}
