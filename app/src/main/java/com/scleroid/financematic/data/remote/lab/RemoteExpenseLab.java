package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.deleteJobs.DeleteExpenseJob;
import com.scleroid.financematic.data.remote.services.jobs.syncJobs.SyncExpenseJob;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class RemoteExpenseLab implements RemoteDataSource<Expense> {
	private final JobManager jobManager;

	@Inject
	public RemoteExpenseLab(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public Completable sync(final Expense expense) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncExpenseJob(expense)));
	}

	@Override
	public Completable delete(final Expense expense) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new DeleteExpenseJob(expense)));
	}

}
