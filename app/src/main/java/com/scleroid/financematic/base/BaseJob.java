package com.scleroid.financematic.base;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.di.AppComponent;
import com.scleroid.financematic.di.JobManagerInjectable;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public abstract class BaseJob extends Job implements JobManagerInjectable {
	// annotate fields that should be injected and made available to subclasses
	@Inject
	WebService service;

	protected BaseJob(Params params) {
		super(params);
	}

	@Override
	public void inject(AppComponent component) {
		component.inject(this);
	}
}