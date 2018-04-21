package com.scleroid.financematic.utils.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.scleroid.financematic.MainActivity;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseDialog;

import javax.inject.Inject;

public class ActivityUtils {
	@Inject
	public ActivityUtils() {
	}//for intend passook

	public void loadFragment(Fragment fragment, FragmentManager fm) {

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		// replace the FrameLayout with new Fragment
		fragmentTransaction.replace(R.id.frame_container, fragment);

		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		fragmentTransaction.commit(); // save the changes
		// load fragment
	}

	public void loadFragmentWithoutBackStack(Fragment fragment, FragmentManager fm) {

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		// replace the FrameLayout with new Fragment
		fragmentTransaction.replace(R.id.frame_container, fragment);
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		fragmentTransaction.commit(); // save the changes
		// load fragment
	}


	public void loadDialogFragment(BaseDialog fragment, Fragment targetFragment,
	                               FragmentManager fm, int requestValue, String dialogValue) {

		// FragmentTransaction fragmentTransaction = fm.beginTransaction();
		// replace the FrameLayout with new Fragment
		// fragmentTransaction.replace(R.id.frame_container, fragment);
		//  fragmentTransaction.addToBackStack(null);
		//  fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
		//          android.R.anim.fade_out);
		// fragmentTransaction.commit(); // save the changes

		fragment.setTargetFragment(targetFragment, requestValue);

		fragment.show(fm, dialogValue);
		// load fragment
	}

	public void loadDialogFragment(DialogFragment fragment,
	                               FragmentManager fm, String dialogValue) {

		// FragmentTransaction fragmentTransaction = fm.beginTransaction();
		// replace the FrameLayout with new Fragment
		// fragmentTransaction.replace(R.id.frame_container, fragment);
		//  fragmentTransaction.addToBackStack(null);
		//  fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
		//          android.R.anim.fade_out);
		// fragmentTransaction.commit(); // save the changes

		///fragment.setTargetFragment(targetFragment, requestValue);
		fragment.show(fm, dialogValue);
		// load fragment
	}


	public void callIntent(Activity activity, String number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
		activity.startActivity(intent);
	}

	public void useUpButton(MainActivity activity, boolean value) {
		ActionBar actionBar = activity.getSupportActionBar();
		// Code here will be triggered once the drawer open as we dont want anything to
// happen so we leave this blank
//Used to change the z index of a custom drawer,
//Hack when navigation drawer doesn't listen to click events
// Code here will be triggered once the drawer closes as we dont want anything to
// happen so we leave this blank


		if (value) {

			activity.getToggle().setDrawerIndicatorEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
			activity.getToggle()
					.setToolbarNavigationClickListener(v -> activity.onBackPressed());
		} else {
			actionBar.setDisplayHomeAsUpEnabled(false);
			activity.getToggle().setDrawerIndicatorEnabled(true);
			activity.getToggle().setToolbarNavigationClickListener(null);
		}
	}

	public void setTitle(AppCompatActivity activity, String msg) {
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(msg);
	}

	public void addressIntent(final Activity activity, final String address) {
		String drivingMode = "&mode=d";
		Uri gmmIntentUri = Uri.parse("google.navigation:0,0?q=" + address + drivingMode);

		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
		mapIntent.setPackage("com.google.android.apps.maps");

		activity.startActivity(mapIntent);
	}
}
