package com.scleroid.financematic.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.scleroid.financematic.utils.CommonUtils;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.network.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

import dagger.android.AndroidInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */

public abstract class BaseActivity
		extends AppCompatActivity
		implements BaseFragment.Callback, HasSupportFragmentInjector {

	EventBus eventBus = GlobalBus.getBus();
	// TODO
	// this can probably depend on isLoading variable of BaseViewModel,
	// since its going to be common for all the activities
	private ProgressDialog mProgressDialog;

	@Override
	public void onFragmentAttached() {

	}

	@Override
	public void onFragmentDetached(String tag) {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		performDependencyInjection();
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());


	}

	/**
	 * @return layout resource id
	 */
	public abstract
	@LayoutRes
	int getLayoutId();


	/**
	 * Dispatch onPause() to fragments.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		eventBus.unregister(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	public void performDependencyInjection() {
		AndroidInjection.inject(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		eventBus.register(this);

        /*if (listState != null) {
            recyclerViewPager.getLayoutManager().onRestoreInstanceState(listState);
        }*/
	}

	@TargetApi(Build.VERSION_CODES.M)
	public boolean hasPermission(@NonNull String permission) {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
				checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	public void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm =
					(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}

	public boolean isNetworkConnected() {
		return NetworkUtils.isNetworkConnected(getApplicationContext());
	}

	@TargetApi(Build.VERSION_CODES.M)
	public void requestPermissionsSafely(@NonNull String[] permissions, int requestCode) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(permissions, requestCode);
		}
	}

	public void showLoading() {
		hideLoading();
		mProgressDialog = CommonUtils.showLoadingDialog(this);
	}

	public void hideLoading() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}

	/**
	 * @return actionBar
	 */
	public abstract android.support.v7.app.ActionBar getActionBarBase();



}

