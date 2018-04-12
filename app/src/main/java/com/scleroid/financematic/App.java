package com.scleroid.financematic;

import android.app.Application;

import com.scleroid.financematic.di.AppComponent;
import com.scleroid.financematic.di.AppInjector;
import com.scleroid.financematic.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

/**
 * Copyright (C) 3/9/18 Author ganesh
 */

/**
 * We create a custom{@link Application} class that extends  {@link DaggerApplication}.
 * We then override applicationInjector() which tells Dagger how to make our @Singleton Component
 * We never have to call `component.inject(this)` as {@link DaggerApplication} will do that for us.
 */
public class App extends DaggerApplication implements HasActivityInjector {

	@Override
	public void onCreate() {
		super.onCreate();

		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}

		DaggerAppComponent
				.builder()
				.application(this)
				.build()
				.inject(this);
		AppInjector.init(this);

		//    JobManagerFactory.getJobManager(this);
	}

	@Override
	protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
		AppComponent appComponent = DaggerAppComponent
				.builder()
				.application(this)
				.build();
		appComponent.inject(this);

		return appComponent;
	}

}
