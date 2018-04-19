package com.scleroid.financematic.fragments.loanDetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.utils.network.Resource;
import com.scleroid.financematic.viewmodels.LoanViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class LoanDetailsViewModel extends BaseViewModel<TransactionModel> implements LoanViewModel {

	private final TransactionsRepo transactionRepo;
	private final LoanRepo loanRepo;
	private final InstallmentRepo installmentRepo;
	LiveData<List<TransactionModel>>
			transactionLiveData = new MutableLiveData<>();
	LiveData<Loan> loanLiveData = new MutableLiveData<>();
	int currentAccountNo = 0;
	private LiveData<List<Installment>> installmentLiveData = new MutableLiveData<>();

	public LoanDetailsViewModel(final TransactionsRepo transactionRepo,
	                            final LoanRepo loanRepo,
	                            final InstallmentRepo installmentRepo) {
		this.transactionRepo = transactionRepo;
		this.loanRepo = loanRepo;
		this.installmentRepo = installmentRepo;
	}

	public LiveData<Loan> getLoanLiveData() {
		if (loanLiveData.getValue() == null) { loanLiveData = setLoanLiveData(); }
		return loanLiveData;
	}

	public LiveData<Loan> setLoanLiveData(
	) {
		return loanRepo.getLocalLoanLab().getItem(currentAccountNo);
	}

	public int getCurrentAccountNo() {
		return currentAccountNo;
	}

	public void setCurrentAccountNo(final int currentAccountNo) {
		this.currentAccountNo = currentAccountNo;
	}

	protected LiveData<List<Installment>> getInstallmentList() {
		if (installmentLiveData.getValue() == null || installmentLiveData.getValue().isEmpty()) {
			installmentLiveData = updateInstallmentLiveData();
		}
		return installmentLiveData;
	}

	protected LiveData<List<Installment>> updateInstallmentLiveData() {
		installmentLiveData =
				installmentRepo.getLocalInstallmentsLab().getItemsForLoan(currentAccountNo);
		return installmentLiveData;
	}

	protected LiveData<List<TransactionModel>> getTransactionList() {
		if (transactionLiveData.getValue() == null || transactionLiveData.getValue().isEmpty()) {
			transactionLiveData = updateTransactionLiveData();
		}
		return transactionLiveData;
	}

	protected LiveData<List<TransactionModel>> updateTransactionLiveData() {
		transactionLiveData =
				transactionRepo.getLocalTransactionsLab().getItemsForLoan(currentAccountNo);
		return transactionLiveData;
	}


	@Override
	protected LiveData<Resource<List<TransactionModel>>> updateItemLiveData() {
		return null;
	}

	@Override
	protected LiveData<Resource<List<TransactionModel>>> getItemList() {
		return null;
	}
}
