package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncTransactionJob;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class RemoteTransactionLab implements RemoteDataSource<TransactionModel> {
	private final JobManager jobManager;

	@Inject
	public RemoteTransactionLab(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public Completable sync(final TransactionModel transactionModel) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncTransactionJob<>(transactionModel)));
	}

}
