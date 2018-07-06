package com.scleroid.financematic.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;
import com.scleroid.financematic.data.remote.services.jobs.utils.JobPriority;
import com.scleroid.financematic.data.remote.services.networking.RemoteException;

import java.util.List;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public abstract class BaseJob<T> extends Job {

	protected T t;
	protected List<T> tList;

	protected RemotePostEndpoint service;


	protected BaseJob(String TAG, T t,
	                  final RemotePostEndpoint service) {
		super(new Params(JobPriority.MID)
				.requireNetwork()
				.groupBy(TAG));
		//	.persist());
		this.t = t;
		this.service = service;
	}

	protected BaseJob(String TAG, List<T> t) {
		super(new Params(JobPriority.MID)
				.requireNetwork()
				.groupBy(TAG)
				.persist());
		this.tList = t;
	}


	@Override
	public void onAdded() {
		Timber.d("Executing onAdded() for  " + t.getClass().getSimpleName() + "  " + t.toString());
	}

	@Override
	public abstract void onRun();

	@Override
	protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
		Timber.d("canceling job. reason: %d, throwable: %s", cancelReason, throwable);
		// sync to remote failed
		//     SyncExpenseRxBus.getInstance().post(SyncResponseEventType.FAILED, expense);
	}

	@Override
	protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
	                                                 int maxRunCount) {
		if (throwable instanceof RemoteException) {
			RemoteException exception = (RemoteException) throwable;

			int statusCode = exception.getResponse().code();
			if (statusCode >= 400 && statusCode < 500) {
				return RetryConstraint.CANCEL;
			}
		}
		// if we are here, most likely the connection was lost during job execution
		return RetryConstraint.RETRY;
	}
}