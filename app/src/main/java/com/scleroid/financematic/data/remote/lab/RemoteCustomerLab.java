package com.scleroid.financematic.data.remote.lab;

import com.birbit.android.jobqueue.JobManager;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.deleteJobs.DeleteCustomerJob;
import com.scleroid.financematic.data.remote.services.jobs.syncJobs.SyncCustomerJob;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class RemoteCustomerLab implements RemoteDataSource<Customer> {
	private final JobManager jobManager;

	@Inject
	public RemoteCustomerLab(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public Completable sync(final Customer customer) {
      /*  return Completable.fromAction(()->
        JobManagerFactory.getJobManager().addJobInBackground(new SyncCustomerJob(customer)));*/
		return Completable.fromAction(() ->
				jobManager
						.addJobInBackground(new SyncCustomerJob(customer)));
	}


	@Override
	public Completable delete(final Customer t) {
		return Completable.fromAction(() -> jobManager
				.addJobInBackground(new DeleteCustomerJob(t)));
	}
}
