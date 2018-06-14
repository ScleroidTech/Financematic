package com.scleroid.financematic.fragments.customer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.network.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class CustomerViewModel extends BaseViewModel
		implements com.scleroid.financematic.viewmodels.CustomerViewModel {
	private final CustomerRepo customerRepo;
	private final LoanRepo loanRepo;
	int currentCustomerId = 0;
	private LiveData<Resource<List<Loan>>> loanLiveData = new MutableLiveData<>();
	private LiveData<Resource<Customer>> customerLiveData = new MutableLiveData<>();

	@Inject
	public CustomerViewModel(CustomerRepo customerRepo, LoanRepo loanRepo) {

		super();
		this.customerRepo = customerRepo;
		this.loanRepo = loanRepo;

	}

	public LiveData<Resource<Customer>> getCustomerLiveData() {
		if (customerLiveData.getValue() == null) { customerLiveData = setCustomerLiveData(); }
		return customerLiveData;
	}

	public LiveData<Resource<Customer>> setCustomerLiveData() {

		return customerRepo.loadItem(currentCustomerId);
	}    //TODO add  data in it

	protected LiveData<Resource<List<Loan>>> getLoanList() {
		if (loanLiveData.getValue() == null || loanLiveData.getValue().data == null) {
			loanLiveData = updateLoanLiveData();
		}
		return loanLiveData;

	}

	protected LiveData<Resource<List<Loan>>> updateLoanLiveData() {
		loanLiveData = loanRepo.loadLoansForCustomer(currentCustomerId);
		return loanLiveData;
	}

	public int getCurrentCustomerId() {
		return currentCustomerId;
	}

	public void setCurrentCustomerId(final int currentCustomerId) {
		this.currentCustomerId = currentCustomerId;
	}

	@Nullable
	@Override
	protected LiveData<Resource<List>> updateItemLiveData() {
		return null;
	}

	@Nullable
	@Override
	protected LiveData<Resource<List>> getItemList() {
		return null;
	}
}
