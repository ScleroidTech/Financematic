package com.scleroid.financematic.utils;

/**
 * Copyright (C) 2018
 *
 * @since 4/4/18
 */

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.ExpenseRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.fragments.customer.CustomerViewModel;
import com.scleroid.financematic.fragments.dashboard.DashboardViewModel;
import com.scleroid.financematic.fragments.expense.ExpenseViewModel;
import com.scleroid.financematic.fragments.loanDetails.LoanDetailsViewModel;
import com.scleroid.financematic.fragments.passbook.PassbookViewModel;
import com.scleroid.financematic.fragments.people.PeopleViewModel;

import javax.inject.Inject;

/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not actually
 * necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

	@SuppressLint("StaticFieldLeak")
	private static volatile ViewModelFactory INSTANCE;

	private final Application mApplication;
	private CustomerRepo customerRepo;
	private LoanRepo loanRepo;


	private InstallmentRepo installmentRepo;

	private TransactionsRepo transactionsRepo;
	private ExpenseRepo expenseRepo;

	@Inject
	public ViewModelFactory(final Application mApplication, final CustomerRepo customerRepo,
	                        final LoanRepo loanRepo, final InstallmentRepo installmentRepo,
	                        final TransactionsRepo transactionsRepo,
	                        final ExpenseRepo expenseRepo) {
		this.mApplication = mApplication;
		this.customerRepo = customerRepo;
		this.loanRepo = loanRepo;
		this.installmentRepo = installmentRepo;
		this.transactionsRepo = transactionsRepo;
		this.expenseRepo = expenseRepo;
	}


	@Override
	public <T extends ViewModel> T create(Class<T> modelClass) {
		if (modelClass.isAssignableFrom(CustomerViewModel.class)) {
			//noinspection unchecked
			return (T) new CustomerViewModel(customerRepo, loanRepo);
		} else if (modelClass.isAssignableFrom(ExpenseViewModel.class)) {
			//noinspection unchecked
			return (T) new ExpenseViewModel();

		} else if (modelClass.isAssignableFrom(LoanDetailsViewModel.class)) {
			//noinspection unchecked
			return (T) new LoanDetailsViewModel();
		} else if (modelClass.isAssignableFrom(PeopleViewModel.class)) {
			//noinspection unchecked
			return (T) new PeopleViewModel(customerRepo, loanRepo);
		} else if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
			//noinspection unchecked
			return (T) new DashboardViewModel(customerRepo, loanRepo, installmentRepo);
		} else if (modelClass.isAssignableFrom(PassbookViewModel.class)) {
			//noinspection unchecked
			return (T) new PassbookViewModel();
		}
		throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
	}
}