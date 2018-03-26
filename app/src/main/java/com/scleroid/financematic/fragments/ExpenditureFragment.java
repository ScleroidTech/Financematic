package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.financematic.R;

/**
 * Created by scleroid on 24/3/18.
 */

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.scleroid.financematic.R;


public class ExpenditureFragment extends Fragment {

    public ExpenditureFragment() {
        // Required empty public constructor
    }

    public static ExpenditureFragment newInstance(String param1, String param2) {
        ExpenditureFragment fragment = new ExpenditureFragment();
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
        return inflater.inflate(R.layout.expenditure, container, false);
    }
}
