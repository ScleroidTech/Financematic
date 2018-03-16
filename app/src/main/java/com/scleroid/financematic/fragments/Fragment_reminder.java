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

public class Fragment_reminder extends Fragment {

    public Fragment_reminder() {
        // Required empty public constructor
    }

    public static Fragment_reminder newInstance(String param1, String param2) {
        Fragment_reminder fragment = new Fragment_reminder();
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
