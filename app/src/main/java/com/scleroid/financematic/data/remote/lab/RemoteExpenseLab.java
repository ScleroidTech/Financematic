package com.scleroid.financematic.data.remote.lab;

import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncExpenseJob;
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
public class RemoteExpenseLab implements RemoteDataSource<Expense> {
	@Override
	public Completable sync(final Expense expense) {
		return Completable.fromAction(() ->
				JobManagerFactory.getJobManager()
						.addJobInBackground(new SyncExpenseJob<>(expense)));
	}

	@Override
	public CompletableSource sync(final List<Expense> items) {
		return Completable.fromAction(() ->
				JobManagerFactory.getJobManager()
						.addJobInBackground(new SyncExpenseJob<>(items)));
	}
}
