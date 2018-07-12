/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.scleroid.financematic.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.annotations.NonNull;

/**
 * The base class for all dialog classes,
 * adds dagger and other relevant items common for all dialogs
 */

public abstract class BaseDialog extends DialogFragment {

	@Nullable
	private BaseActivity mActivity;

	/**
	 * gets base activity
	 *
	 * @return activity object
	 */
	@Nullable
	public BaseActivity getBaseActivity() {
		return mActivity;
	}

	/**
	 * Dismiss the fragment and its dialog.  If the fragment was added to the back stack, all back
	 * stack state up to and including this entry will be popped.  Otherwise, a new transaction
	 * will
	 * be committed to remove the fragment.
	 */
	@Override
	public void dismiss() {
		this.dismissAllowingStateLoss();
		super.dismiss();
	}

	/**
	 * Attaches itself to activity,
	 * Performs DI,
	 * then adds itself to activity if the context is right
	 *
	 * @param context Object of current activity
	 */
	@Override
	public void onAttach(Context context) {
		performDependencyInjection();
		super.onAttach(context);
		if (context instanceof BaseActivity) {
			BaseActivity mActivity = (BaseActivity) context;
			this.mActivity = mActivity;
		}
	}

	/**
	 * Removes attachment with the activity it was previously attached to
	 */
	@Override
	public void onDetach() {
		mActivity = null;
		super.onDetach();
	}

	/**
	 * Initializes the dialog,
	 * creates its view, attaches itself to root etc,
	 *
	 * @param savedInstanceState bundle object to be passed for initialization parameter
	 * @return dialog object after initialization
	 */
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// creates the layout object
		final RelativeLayout root = new RelativeLayout(getActivity());
		root.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		// creates the fullscreen dialog
		Dialog dialog = new Dialog(mActivity/* this can be null */);
		// removes the title
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//sets layout
		dialog.setContentView(root);
		if (dialog.getWindow() != null) {
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.getWindow().setLayout(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		//disallows to be cancelable if touched outside
		dialog.setCanceledOnTouchOutside(false);

		return dialog;
	}

	/**
	 * Dismisses the dialog first before stopping the lifecycle
	 */
	@Override
	public void onStop() {
		this.dismiss();
		super.onStop();

	}

	/**
	 * Adds it to DI list
	 */
	private void performDependencyInjection() {
		AndroidSupportInjection.inject(this);
	}


}
