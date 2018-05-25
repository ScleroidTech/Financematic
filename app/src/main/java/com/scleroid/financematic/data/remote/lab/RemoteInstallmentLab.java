package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.DeleteInstallmentsJob;
import com.scleroid.financematic.data.remote.services.jobs.SyncInstallmentsJob;

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

	@Inject
	public RemoteInstallmentLab(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public Completable sync(final Installment installment) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncInstallmentsJob(installment)));
	}


	public Completable delete(final Installment installment) {
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new DeleteInstallmentsJob(installment)));
	}

}
