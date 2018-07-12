package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;
import com.scleroid.financematic.data.remote.services.jobs.delete.DeleteInstallmentsJob;
import com.scleroid.financematic.data.remote.services.jobs.sync.SyncInstallmentsJob;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class RemoteInstallmentLab implements RemoteDataSource<Installment> {

	private final JobManager jobManager;
	private RemotePostEndpoint service;

	@Inject
	public RemoteInstallmentLab(JobManager jobManager,
	                            final RemotePostEndpoint service) {
		this.jobManager = jobManager;
		this.service = service;
	}

	@Override
	public Completable sync(final Installment installment) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncInstallmentsJob(installment, service)));
	}


	public Completable delete(final Installment installment) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new DeleteInstallmentsJob(installment, service)));
	}

}
