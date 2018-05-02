package com.scleroid.financematic.data.remote.services.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.remote.services.jobs.utils.JobPriority;
import com.scleroid.financematic.data.remote.services.networking.RemoteException;

import javax.inject.Inject;

import timber.log.Timber;

public class SyncCustomerJob extends BaseJob {

	private static final String TAG = SyncCustomerJob.class.getCanonicalName();
	private final Customer customer;

	@Inject
	WebService webService;

	public SyncCustomerJob(Customer customer) {
		super(new Params(JobPriority.MID)
				.requireNetwork()
				.groupBy(TAG)
				.persist());
		this.customer = customer;
	}

	@Override
	public void onAdded() {
		Timber.d("Executing onAdded() for customer " + customer);
	}

	@Override
	public void onRun() {
		Timber.d("Executing onRun() for customer " + customer);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		//    webService.addCustomer(customer);

		// remote call was successful--the Customer will be updated locally to reflect that sync
		// is no longer pending
		//       Customer updatedCustomer = CustomerUtils.clone(customer, false);
		//   SyncCustomerRxBus.getInstance().post(SyncResponseEventType.SUCCESS, updatedCustomer);
	}

	@Override
	protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
		Timber.d("canceling job. reason: %d, throwable: %s", cancelReason, throwable);
		// sync to remote failed
		//     SyncCustomerRxBus.getInstance().post(SyncResponseEventType.FAILED, customer);
	}

	@Override
	protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
	                                                 int maxRunCount) {
		if (throwable instanceof RemoteException) {
			RemoteException exception = (RemoteException) throwable;

			int statusCode = exception.getResponse().code();
			if (statusCode >= 400 && statusCode < 500) {
				return RetryConstraint.CANCEL;
			}
		}
		// if we are here, most likely the connection was lost during job execution
		return RetryConstraint.RETRY;
	}
}
