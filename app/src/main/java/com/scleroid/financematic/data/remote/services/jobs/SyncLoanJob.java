package com.scleroid.financematic.data.remote.services.jobs;

import com.scleroid.financematic.base.BaseJob;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class SyncLoanJob<Loan> extends BaseJob {

	private static final String TAG = SyncLoanJob.class.getCanonicalName();

	public SyncLoanJob(Loan loan) {
		super(TAG, loan);

	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for loan " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		//    webService.addLoan(loan);

		// remote call was successful--the Loan will be updated locally to reflect that sync
		// is no longer pending
		//       Loan updatedLoan = LoanUtils.clone(loan, false);
		//   SyncLoanRxBus.getInstance().post(SyncResponseEventType.SUCCESS, updatedLoan);
	}


}
