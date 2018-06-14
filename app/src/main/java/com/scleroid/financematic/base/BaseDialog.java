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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by amitshekhar on 10/07/17.
 */

public abstract class BaseDialog extends DialogFragment {
	/*@Inject
	DispatchingAndroidInjector<Fragment> childFragmentInjector;*/
	@Nullable
	private BaseActivity mActivity;
	private boolean dialogDismissed;
	@Nullable
	private Dialog dialog;

/*	@Override
	public void onResume() {
		super.onResume();
//...
		if (dialogDismissed && dialog != null) {
			dialog.dismiss();
		}
	}*/

	public void dismissDialog(String tag) {
		dismiss();
		getBaseActivity().onFragmentDetached(tag);
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


	/* A hack that didn't work
	@Override
	public void show(final FragmentManager fragmentManager, final String tagName) {
		//super.show(manar, tag);
		if (fragmentManager.isStateSaved()) return;

		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(this, tagName);
		ft.disallowAddToBackStack();
		ft.commitAllowingStateLoss();
	}
*/

	@Override
	public void onAttach(Context context) {
		performDependencyInjection();
		super.onAttach(context);
		if (context instanceof BaseActivity) {
			BaseActivity mActivity = (BaseActivity) context;
			this.mActivity = mActivity;
			mActivity.onFragmentAttached();
		}
	}

	@Override
	public void onDetach() {
		mActivity = null;
		super.onDetach();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// the content
		final RelativeLayout root = new RelativeLayout(getActivity());
		root.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		// creating the fullscreen dialog
		dialog = new Dialog(getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(root);
		if (dialog.getWindow() != null) {
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.getWindow().setLayout(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		dialog.setCanceledOnTouchOutside(false);

		return dialog;
	}

	@Override
	public void onStop() {
		this.dismiss();
		super.onStop();

	}

	@Nullable
	public BaseActivity getBaseActivity() {
		return mActivity;
	}

	private void performDependencyInjection() {
		AndroidSupportInjection.inject(this);
	}

	public void hideKeyboard() {
		if (mActivity != null) {
			mActivity.hideKeyboard();
		}
	}

	public void hideLoading() {
		if (mActivity != null) {
			mActivity.hideLoading();
		}
	}

	public boolean isNetworkConnected() {
		return mActivity != null && mActivity.isNetworkConnected();
	}

/*	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
		dialogDismissed = true;
		dismiss();
	}

	*/

	public void showLoading() {
		if (mActivity != null) {
			mActivity.showLoading();
		}
	}
}
