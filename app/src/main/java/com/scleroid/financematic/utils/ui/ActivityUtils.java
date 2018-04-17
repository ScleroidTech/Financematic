package com.scleroid.financematic.utils.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.scleroid.financematic.R;

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

	public void loadDialogFragment(DialogFragment fragment, Fragment targetFragment,
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


}
