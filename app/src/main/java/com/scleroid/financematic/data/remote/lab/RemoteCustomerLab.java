package com.scleroid.financematic.data.remote.lab;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.RemoteDataSource;
import com.scleroid.financematic.data.remote.services.jobs.SyncCustomerJob;
import com.scleroid.financematic.data.remote.services.jobs.utils.JobManagerFactory;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class RemoteCustomerLab implements RemoteDataSource<Customer> {

	@Override
	public Completable sync(final Customer customer) {
      /*  return Completable.fromAction(()->
        JobManagerFactory.getJobManager().addJobInBackground(new SyncCustomerJob(customer)));*/
		return Completable.fromAction(() ->
				JobManagerFactory.getJobManager()
						.addJobInBackground(new SyncCustomerJob(customer)));
	}
}
