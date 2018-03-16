package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.financematic.R;

/**
 * Created by scleroid on 2/3/18.
 */

public class Fragment_loan_details extends Fragment {

    public Fragment_loan_details() {
        // Required empty public constructor
    }

    public static Fragment_loan_details newInstance(String param1, String param2) {
        Fragment_loan_details fragment = new Fragment_loan_details();
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
        return inflater.inflate(R.layout.dashboard, container, false);
    }
}
