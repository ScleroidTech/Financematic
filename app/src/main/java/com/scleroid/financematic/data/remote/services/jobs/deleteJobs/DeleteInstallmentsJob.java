package com.scleroid.financematic.data.remote.services.jobs.deleteJobs;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class DeleteInstallmentsJob extends BaseJob<Installment> {

	private static final String TAG = DeleteInstallmentsJob.class.getCanonicalName();


	public DeleteInstallmentsJob(Installment installment,
	                             final RemotePostEndpoint service) {
		super(TAG, installment, service);

	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for installment " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		service.deleteInstallment(t.getInstallmentId());

		// remote call was successful--the Installment will be updated locally to reflect that sync
		// is no longer pending
		//       Installment updatedInstallment = InstallmentUtils.clone(installment, false);
		//   SyncInstallmentRxBus.getInstance().post(SyncResponseEventType.SUCCESS,
		// updatedInstallment);
	}


}
