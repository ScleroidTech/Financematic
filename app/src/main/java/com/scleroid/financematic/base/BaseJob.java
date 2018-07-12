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

/**
 * Creates a base class for all the jobs it created
 * jobs are forwarded to
 *
 * @param <T> generic object of the job to be handled
 * @see com.birbit.android.jobqueue.JobManager to be handled later
 */
public abstract class BaseJob<T> extends Job {

	/**
	 * generic object to be handled
	 */
	protected T t;
	/**
	 * List of generic objects
	 */
	protected List<T> tList;

	/**
	 * API endpoints to be called by the job
	 */
	protected RemotePostEndpoint service;

	/**
	 * Constructor which initializes the basic attributes of job
	 * @param TAG the tag by which jobs will be prioritized
	 * @param t the object which needs to go to network
	 * @param service the api endpoint to be called
	 */
	protected BaseJob(String TAG, T t,
	                  final RemotePostEndpoint service) {
		super(new Params(JobPriority.MID)
				.requireNetwork()
				.groupBy(TAG));
		//Persist attribute is to be used if we need the data to be persistent,
		// due to some bug, it throws an error if enabled,
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

	/**
	 * to be called when a new job is added to the list
	 */
	@Override
	public void onAdded() {
		Timber.d("Executing onAdded() for  " + t.getClass().getSimpleName() + "  " + t.toString());
	}

	/**
	 * where the job actually executes, this needs to be overridden by each of its child class
	 */
	@Override
	public abstract void onRun();

	/**
	 * called when the job is cancelled,
	 * it's not used in the code explicitly
	 * @param cancelReason reason code for cancellation
	 * @param throwable the exception thrown because of that
	 */
	@Override
	protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
		Timber.d("canceling job. reason: %d, throwable: %s", cancelReason, throwable);

	}

	/**
	 * THe retry constraint to repeat the job until its successful
	 * @param throwable the exception code
	 * @param runCount number of times it ran already
	 * @param maxRunCount max times it allowed to retry
	 * @return retryConstraint to be put on the job
	 */
	@Override
	protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
	                                                 int maxRunCount) {
		if (throwable instanceof RemoteException) {
			RemoteException exception = (RemoteException) throwable;

			int statusCode = exception.getResponse().code();
			// if the status code is in this range,means it's the server issue, and we should cancel the job
			if (statusCode >= 400 && statusCode < 500) {
				return RetryConstraint.CANCEL;
			}
		}
		// if we are here, most likely the connection was lost during job execution
		return RetryConstraint.RETRY;
	}
}