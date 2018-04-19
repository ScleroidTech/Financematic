package com.scleroid.financematic.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.scleroid.financematic.utils.network.Resource;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public abstract class BaseViewModel<N> extends ViewModel {

	//TODO Bad Practice, forgot all standards
	/*@Inject
	LocalCustomerLab localCustomerLab;

	@Inject
	LocalLoanLab localLoansLab;

	@Inject
	LocalExpenseLab localExpenseLab;
	@Inject
	LocalInstallmentsLab localInstallmentLab;
	@Inject
	LocalTransactionsLab localTransactionsLab;*/
	/*private LiveData<List<Customer>> customerList;
	private LiveData<List<TransactionModel>> transactionList;
	private LiveData<List<Loan>> loanList;
	private LiveData<List<Installment>> installmentList;
	private LiveData<List<Expense>> expenseList;*/
	/*protected BaseViewModel() {
		this.customerList = getCustomerList();
		this.transactionList = getTransactionList();
		this.loanList = getInstallmentList();
		this.installmentList = getInstallmentList();
		this.expenseList = getExpenseList();
	}*/
/*

	protected LiveData<List<Customer>> getCustomerList() {
		if (customerList == null) customerList = updateCustomerList();
		return customerList;
	}

	private LiveData<List<Customer>> updateCustomerList() {
		return localCustomerLab.getItems();
	}

	protected LiveData<List<TransactionModel>> getTransactionList() {
		if (transactionList == null) transactionList = updateTransactionList();
		return transactionList;
	}

	private LiveData<List<TransactionModel>> updateTransactionList() {
		return localTransactionsLab.getItems();
	}

	protected LiveData<List<Loan>> getInstallmentList() {
		if (loanList == null) loanList = updateLoanList();
		return loanList;
	}

	private LiveData<List<Loan>> updateLoanList() {
		return localLoansLab.getItems();

	}

	protected LiveData<List<Installment>> getInstallmentList() {
		if (installmentList == null) installmentList = updateInstallmentList();
		return installmentList;
	}

	private LiveData<List<Installment>> updateInstallmentList() {
		return localInstallmentLab.getItems();
	}

	protected LiveData<List<Expense>> getExpenseList() {
		if (expenseList == null) expenseList = updateExpenseList();
		return expenseList;
	}

	private LiveData<List<Expense>> updateExpenseList() {
		return localExpenseLab.getItems();
	}
*/
	protected abstract LiveData<Resource<List<N>>> updateItemLiveData();

	protected abstract LiveData<Resource<List<N>>> getItemList();

/* @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }
*//*


	 */

}
