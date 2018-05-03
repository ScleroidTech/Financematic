package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncLoanJob;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class RemoteLoanLab implements RemoteDataSource<Loan> {
	private final JobManager jobManager;

	@Inject
	public RemoteLoanLab(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public Completable sync(final Loan loan) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncLoanJob<>(loan)));
	}

}
