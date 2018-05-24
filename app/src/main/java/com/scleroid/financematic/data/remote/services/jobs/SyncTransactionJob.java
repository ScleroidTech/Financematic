package com.scleroid.financematic.data.remote.services.jobs;

import com.scleroid.financematic.base.BaseJob;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;

import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
public class SyncTransactionJob extends BaseJob<TransactionModel> {

	private static final String TAG = SyncTransactionJob.class.getCanonicalName();

	public SyncTransactionJob(TransactionModel transaction) {
		super(TAG, transaction);

	}

	public SyncTransactionJob(List<TransactionModel> transactionModels) {
		super(TAG, transactionModels);
	}

	@Override
	public void onRun() {
		Timber.d("Executing onRun() for transaction " + t);


		// if any exception is thrown, it will be handled by shouldReRunOnThrowable()
		service.addTransaction(t);

		// remote call was successful--the Transaction will be updated locally to reflect that sync
		// is no longer pending
		//       Transaction updatedTransaction = TransactionUtils.clone(transaction, false);
		//   SyncTransactionRxBus.getInstance().post(SyncResponseEventType.SUCCESS,
		// updatedTransaction);
	}


}
