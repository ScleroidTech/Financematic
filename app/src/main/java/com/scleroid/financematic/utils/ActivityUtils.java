package com.scleroid.financematic.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.scleroid.financematic.R;

public class ActivityUtils {
    public ActivityUtils() {
    }//for intend passook

    public void loadFragment(Fragment fragment, FragmentManager fm) {

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit(); // save the changes
        // load fragment

    }
}