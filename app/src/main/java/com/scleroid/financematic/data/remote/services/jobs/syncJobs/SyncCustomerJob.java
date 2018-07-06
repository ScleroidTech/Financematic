package com.scleroid.financematic.data.remote.services.jobs.syncJobs;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
		service.addCustomer(t).enqueue(new Callback<Customer>() {
			@Override
			public void onResponse(final Call<Customer> call, final Response<Customer> response) {
				if (response.isSuccessful()) {
					Timber.d(response.body()
							.toString() + " " + response.code() + " " + response.headers() + " " +
							response
									.message() + "\n" + response.raw());
				} else { Timber.e(response.errorBody() + "\n" + response.raw()); }
			}

			@Override
			public void onFailure(final Call<Customer> call, final Throwable t) {
				Timber.e(t.toString());

			}
		});


		// remote call was successful--the Customer will be updated locally to reflect that sync
		// is no longer pending
		//       Customer updatedCustomer = CustomerUtils.clone(customer, false);
		//   SyncCustomerRxBus.getInstance().post(SyncResponseEventType.SUCCESS, updatedCustomer);
	}


}
