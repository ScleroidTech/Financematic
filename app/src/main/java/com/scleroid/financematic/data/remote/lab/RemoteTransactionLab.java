package com.scleroid.financematic.data.remote.lab;

import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncTransactionJob;
import com.scleroid.financematic.data.remote.services.jobs.utils.JobManagerFactory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class RemoteTransactionLab implements RemoteDataSource<TransactionModel> {
	@Override
	public Completable sync(final TransactionModel transactionModel) {
		return Completable.fromAction(() ->
				JobManagerFactory.getJobManager()
						.addJobInBackground(new SyncTransactionJob<>(transactionModel)));
	}

	@Override
	public CompletableSource sync(final List<TransactionModel> items) {
		return Completable.fromAction(() ->
				JobManagerFactory.getJobManager()
						.addJobInBackground(new SyncTransactionJob<>(items)));
	}
}
