package com.scleroid.financematic.data.remote.lab;

import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncTransactionJob;
import com.scleroid.financematic.data.remote.services.jobs.utils.JobManagerFactory;

import io.reactivex.Completable;

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
						.addJobInBackground(new SyncTransactionJob(transactionModel)));
	}
}
