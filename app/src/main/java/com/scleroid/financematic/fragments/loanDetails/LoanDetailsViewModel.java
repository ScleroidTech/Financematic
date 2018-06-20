package com.scleroid.financematic.fragments.loanDetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

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

import timber.log.Timber;

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
	LiveData<Resource<List<TransactionModel>>>
			transactionLiveData = new MutableLiveData<>();
	LiveData<Resource<Loan>> loanLiveData = new MutableLiveData<>();
	int currentAccountNo = 0;
	private LiveData<Resource<List<Installment>>> installmentLiveData = new MutableLiveData<>();

	public LoanDetailsViewModel(final TransactionsRepo transactionRepo,
	                            final LoanRepo loanRepo,
	                            final InstallmentRepo installmentRepo) {
		this.transactionRepo = transactionRepo;
		this.loanRepo = loanRepo;
		this.installmentRepo = installmentRepo;
	}

	public LiveData<Resource<Loan>> getLoanLiveData() {
		if (loanLiveData.getValue() == null) { loanLiveData = setLoanLiveData(); }
		return loanLiveData;
	}

	public LiveData<Resource<Loan>> setLoanLiveData(
	) {
		return loanRepo.loadItem(currentAccountNo);
	}

	public int getCurrentAccountNo() {
		return currentAccountNo;
	}

	public void setCurrentAccountNo(final int currentAccountNo) {
		this.currentAccountNo = currentAccountNo;
	}

	protected LiveData<Resource<List<Installment>>> getInstallmentList() {
		if (installmentLiveData.getValue() == null || installmentLiveData.getValue().data ==
				null) {
			installmentLiveData = updateInstallmentLiveData();
		}
		return installmentLiveData;
	}

	protected LiveData<Resource<List<Installment>>> updateInstallmentLiveData() {
		installmentLiveData =
				installmentRepo.loadInstallmentsForLoan(currentAccountNo);
		return installmentLiveData;
	}

	protected LiveData<Resource<List<TransactionModel>>> getTransactionList() {
		if (transactionLiveData.getValue() == null || transactionLiveData.getValue().data ==
				null) {
			transactionLiveData = updateTransactionLiveData();
		}
		return transactionLiveData;
	}

	protected LiveData<Resource<List<TransactionModel>>> updateTransactionLiveData() {
		transactionLiveData =
				transactionRepo.loadTransactionsForLoan(currentAccountNo);
		return transactionLiveData;
	}

	protected void saveInstallmentsList(List<Installment> installments) {
		/*for (Installment installment : installments
				) {
			installmentRepo.updateItem(installment);

		}*/
		Timber.d("saving installments");
		installmentRepo.saveItems(installments);
	}

	@Nullable
	@Override
	protected LiveData<Resource<List<TransactionModel>>> updateItemLiveData() {
		return null;
	}

	@Nullable
	@Override
	protected LiveData<Resource<List<TransactionModel>>> getItemList() {
		return null;
	}
}
