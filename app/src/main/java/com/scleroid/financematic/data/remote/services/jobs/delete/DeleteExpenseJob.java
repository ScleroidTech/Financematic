package com.scleroid.financematic.data.remote.services.jobs.delete;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class DeleteExpenseJob extends BaseJob<Expense> {

	private static final String TAG = DeleteExpenseJob.class.getCanonicalName();

	public DeleteExpenseJob(final Expense expense,
	                        final RemotePostEndpoint service) {
		super(TAG, expense, service);
	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for installment " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		service.deleteExpense(t.getExpenseId());

		// remote call was successful--the Installment will be updated locally to reflect that sync
		// is no longer pending
		//       Installment updatedInstallment = InstallmentUtils.clone(installment, false);
		//   SyncInstallmentRxBus.getInstance().post(SyncResponseEventType.SUCCESS,
		// updatedInstallment);
	}


}
