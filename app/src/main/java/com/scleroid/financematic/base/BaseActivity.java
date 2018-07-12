package com.scleroid.financematic.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.scleroid.financematic.utils.eventbus.GlobalBus;

import org.greenrobot.eventbus.EventBus;

import dagger.android.AndroidInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @see dagger and an eventbus implementation
 * Also there's a basic progressDialog which can be used and it injects fragments with DI
 * @since 4/6/18
 * All activities must extend this
 * so they can have basic features like Dependency Injection using
 */
public abstract class BaseActivity
		extends AppCompatActivity
		implements HasSupportFragmentInjector {

	/**
	 * THis initializes Eventbus with singleton pattern,
	 * Not included via dagger because it is used in places where dagger isn't available
	 */
	EventBus eventBus = GlobalBus.getBus();
	// this can probably depend on isLoading variable of BaseViewModel,
	// since its going to be common for all the activities
	private ProgressDialog mProgressDialog;


	/**
	 * Performs Dependency injection,
	 * calls onCreate to create the view, and sets contentView
	 *
	 * @param savedInstanceState Bundle object with data to be passed while instantiation
	 */
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		performDependencyInjection();
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());

	}

	/**
	 * Injects activity with
	 *
	 * @see AndroidInjection
	 */
	public void performDependencyInjection() {
		AndroidInjection.inject(this);
	}

	/**
	 * returns layout resource id, must be overridden in activity
	 *
	 * @return layout resource id
	 */
	public abstract
	@LayoutRes
	int getLayoutId();

	/**
	 * Dispatch onPause() to fragments.
	 * Unregisters Eventbus after the pause, if not done, can have unexpected results
	 */
	@Override
	protected void onPause() {
		super.onPause();
		eventBus.unregister(this);
	}

	/**
	 * Dispatch onResume()
	 * Registers eventBus so it can listen to events happening
	 */
	@Override
	public void onResume() {
		super.onResume();
		eventBus.register(this);

	}

	/**
	 * @return actionBar
	 */
	public abstract android.support.v7.app.ActionBar getActionBarBase();


}

