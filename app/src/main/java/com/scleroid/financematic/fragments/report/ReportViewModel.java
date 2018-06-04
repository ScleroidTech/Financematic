package com.scleroid.financematic.fragments.report;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.utils.network.Resource;
import com.scleroid.financematic.viewmodels.LoanViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/13/18
 */
public class ReportViewModel extends BaseViewModel<TransactionModel> implements LoanViewModel {
	//private final LoanRepo loanRepo;
	private final TransactionsRepo transactionsRepo;
//	LiveData<List<TransactionModel>> transactionLiveData = new MutableLiveData<>();
	LiveData<Resource<List<TransactionModel>>> transactionLivedataDual =new MutableLiveData<>();

	public ReportViewModel(final TransactionsRepo transactionsRepo) {
		this.transactionsRepo = transactionsRepo;
	//	this.transactionLiveData = setTransactionLiveData();
		this.transactionLivedataDual = updateItemLiveData();
	}

/*	public LiveData<List<TransactionModel>> getTransactionLiveData() {
		if (transactionLiveData.getValue() == null) transactionLiveData = setTransactionLiveData();
		return transactionLiveData;
	}

	private LiveData<List<TransactionModel>> setTransactionLiveData() {

		return transactionsRepo.getLocalTransactionsLab().getItems();
	}*/

	@Override
	protected LiveData<Resource<List<TransactionModel>>> updateItemLiveData() {
		return transactionsRepo.loadItems();
	}

	@Override
	protected LiveData<Resource<List<TransactionModel>>> getItemList() {
		if (transactionLivedataDual.getValue() == null || transactionLivedataDual.getValue().data == null) transactionLivedataDual = updateItemLiveData();
		return transactionLivedataDual;
	}
}
