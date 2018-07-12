package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;
import com.scleroid.financematic.data.remote.services.jobs.delete.DeleteLoanJob;
import com.scleroid.financematic.data.remote.services.jobs.sync.SyncLoanJob;

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
	private RemotePostEndpoint service;

	@Inject
	public RemoteLoanLab(JobManager jobManager, RemotePostEndpoint service) {
		this.jobManager = jobManager;
		this.service = service;
	}

	@Override
	public Completable sync(final Loan loan) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncLoanJob(service, loan)));
	}

	@Override
	public Completable delete(final Loan loan) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new DeleteLoanJob(loan, service)));
	}


}
