package com.scleroid.financematic.fragments.people;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.viewmodels.CustomerViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class PeopleViewModel extends BaseViewModel implements CustomerViewModel {
	private final CustomerRepo customerRepo;
	private final LoanRepo loanRepo;
	LiveData<List<Customer>> customers;


	@Inject
	public PeopleViewModel(CustomerRepo customerRepo, LoanRepo loanRepo) {

		super();
		this.customerRepo = customerRepo;
		this.loanRepo = loanRepo;
		//	this.installmentRepo = installmentRepo;

		// installments = ;
//        Timber.d(installments.getValue().data.isEmpty() + "" );
		customers = getItemList();
		//   Timber.d(upcomingInstallments.getValue().isEmpty() + "" );
		//   setUpcomingInstallmentsTransformed(getTransformedUpcomingData());
	}

	//TODO add  data in it
	@Override
	protected LiveData<List> updateItemLiveData() {
		return null;
	}

	@Override
	protected LiveData<List<Customer>> getItemList() {

		//TODO Everything is local currently, put it on remote later
		if (customers.getValue() == null || customers.getValue().isEmpty()) {
			customers = customerRepo.getLocalCustomerLab()
					.getCustomersWithLoans(); /*filterResults(installmentRepo
					.getLocalInstallmentsLab().getItems());*/
		}
		return customers;
	}
}
