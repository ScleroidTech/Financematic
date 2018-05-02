package com.scleroid.financematic.data.remote.services.jobs;

import com.scleroid.financematic.base.BaseJob;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class SyncInstallmentsJob<Installment> extends BaseJob {

	private static final String TAG = SyncInstallmentsJob.class.getCanonicalName();


	public SyncInstallmentsJob(Installment installment) {
		super(TAG, installment);

	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for installment " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		//    webService.addInstallment(installment);

		// remote call was successful--the Installment will be updated locally to reflect that sync
		// is no longer pending
		//       Installment updatedInstallment = InstallmentUtils.clone(installment, false);
		//   SyncInstallmentRxBus.getInstance().post(SyncResponseEventType.SUCCESS,
		// updatedInstallment);
	}


}
