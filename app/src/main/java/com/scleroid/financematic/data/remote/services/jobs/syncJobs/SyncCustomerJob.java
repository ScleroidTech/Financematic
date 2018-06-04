package com.scleroid.financematic.data.remote.services.jobs.syncJobs;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;

import timber.log.Timber;

public class SyncCustomerJob extends BaseJob<Customer> {

	private static final String TAG = SyncCustomerJob.class.getCanonicalName();


	public SyncCustomerJob(Customer customer,
	                       final RemotePostEndpoint service) {
		super(TAG, customer, service);
	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for customer " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		if (service != null) { service.addCustomer(t); }

		// remote call was successful--the Customer will be updated locally to reflect that sync
		// is no longer pending
		//       Customer updatedCustomer = CustomerUtils.clone(customer, false);
		//   SyncCustomerRxBus.getInstance().post(SyncResponseEventType.SUCCESS, updatedCustomer);
	}


}
