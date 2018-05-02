package com.scleroid.financematic.data.remote.services.jobs;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.Customer;

import timber.log.Timber;

public class SyncCustomerJob extends BaseJob {

	private static final String TAG = SyncCustomerJob.class.getCanonicalName();


	public SyncCustomerJob(Customer customer) {
		super(TAG, customer);
	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for customer " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		//    webService.addCustomer(customer);

		// remote call was successful--the Customer will be updated locally to reflect that sync
		// is no longer pending
		//       Customer updatedCustomer = CustomerUtils.clone(customer, false);
		//   SyncCustomerRxBus.getInstance().post(SyncResponseEventType.SUCCESS, updatedCustomer);
	}


}
