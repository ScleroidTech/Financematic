package com.scleroid.financematic.data.remote.lab;

import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncInstallmentsJob;
import com.scleroid.financematic.data.remote.services.jobs.utils.JobManagerFactory;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class RemoteInstallmentLab implements RemoteDataSource<Installment> {
	@Override
	public Completable sync(final Installment installment) {
		return Completable.fromAction(() ->
				JobManagerFactory.getJobManager()
						.addJobInBackground(new SyncInstallmentsJob<>(installment)));
	}
}
