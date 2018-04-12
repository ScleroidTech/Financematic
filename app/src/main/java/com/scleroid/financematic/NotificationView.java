package com.scleroid.financematic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;

/**
 * Created by scleroid on 11/4/18.
 */


public class NotificationView extends BaseFragment {


    public NotificationView () {
        // Required empty public constructor
    }

    public static com.scleroid.financematic.NotificationView  newInstance(String param1, String param2) {
        com.scleroid.financematic.NotificationView fragment = new com.scleroid.financematic.NotificationView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return layout resource id
     */
    @Override
    public int getLayoutId() {
        return R.layout.notification;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View rootView = getRootView();




        return rootView;


    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }



}
