package com.scleroid.financematic.data.remote.services.jobs;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.TransactionModel;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class SyncTransactionJob extends BaseJob {

	private static final String TAG = SyncTransactionJob.class.getCanonicalName();

	public SyncTransactionJob(TransactionModel transaction) {
		super(TAG, transaction);

	}


	@Override
	public void onRun() {
		Timber.d("Executing onRun() for transaction " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		//    webService.addTransaction(transaction);

		// remote call was successful--the Transaction will be updated locally to reflect that sync
		// is no longer pending
		//       Transaction updatedTransaction = TransactionUtils.clone(transaction, false);
		//   SyncTransactionRxBus.getInstance().post(SyncResponseEventType.SUCCESS,
		// updatedTransaction);
	}


}
