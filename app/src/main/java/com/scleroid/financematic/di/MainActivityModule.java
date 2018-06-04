package com.scleroid.financematic.di;

import android.app.Activity;
import android.content.Context;

import com.scleroid.financematic.MainActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
@Module
public abstract class MainActivityModule {
	@ContributesAndroidInjector(modules = FragmentBuildersModule.class)
	abstract MainActivity contributeMainActivity();

	private final Activity context;

	public MainActivityModule(Activity context) {
		this.context = context;
	}

	@Provides
	@Named("activity_context")
	public Context context() {
		return context;
	}
}
