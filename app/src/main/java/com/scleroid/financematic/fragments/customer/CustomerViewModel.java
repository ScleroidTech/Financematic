package com.scleroid.financematic.fragments.customer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

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
	LiveData<List<Loan>> loanLiveData = new MutableLiveData<>();
	LiveData<Customer> customerLiveData = new MutableLiveData<>();
	int currentCustomerId = 0;

	@Inject
	public CustomerViewModel(CustomerRepo customerRepo, LoanRepo loanRepo) {

		super();
		this.customerRepo = customerRepo;
		this.loanRepo = loanRepo;

	}

	public LiveData<Customer> getCustomerLiveData() {
		if (customerLiveData.getValue() == null) { customerLiveData = setCustomerLiveData(); }
		return customerLiveData;
	}

	public LiveData<Customer> setCustomerLiveData() {

		return customerRepo.getLocalCustomerLab().getItem(currentCustomerId);
	}    //TODO add  data in it

	protected LiveData<List<Loan>> getLoanList() {
		if (loanLiveData.getValue() == null || loanLiveData.getValue().isEmpty()) {
			loanLiveData = updateLoanLiveData();
		}
		return loanLiveData;

	}

	public int getCurrentCustomerId() {
		return currentCustomerId;
	}

	protected LiveData<List<Loan>> updateLoanLiveData() {
		loanLiveData = loanRepo.getLocalLoanLab().getItemWithCustomerId(currentCustomerId);
		return loanLiveData;
	}

	public void setCurrentCustomerId(final int currentCustomerId) {
		this.currentCustomerId = currentCustomerId;
	}

	@Override
	protected LiveData<Resource<List>> updateItemLiveData() {
		return null;
	}

	@Override
	protected LiveData<Resource<List>> getItemList() {
		return null;
	}
}
