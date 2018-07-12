package com.scleroid.financematic.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.scleroid.financematic.fragments.customer.CustomerViewModel;
import com.scleroid.financematic.fragments.dashboard.DashboardViewModel;
import com.scleroid.financematic.fragments.expense.ExpenseViewModel;
import com.scleroid.financematic.fragments.loandetails.LoanDetailsViewModel;
import com.scleroid.financematic.fragments.people.PeopleViewModel;
import com.scleroid.financematic.utils.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */

@Module
public abstract class ViewModelModule {
	@NonNull
	@Binds
	@IntoMap
	@ViewModelKey(CustomerViewModel.class)
	abstract ViewModel bindCustomerViewModel(CustomerViewModel userViewModel);

	@NonNull
	@Binds
	@IntoMap
	@ViewModelKey(ExpenseViewModel.class)
	abstract ViewModel bindExpenseViewModel(ExpenseViewModel expenseViewModel);

	@NonNull
	@Binds
	@IntoMap
	@ViewModelKey(LoanDetailsViewModel.class)
	abstract ViewModel bindLoanDetailsViewModel(LoanDetailsViewModel loanDetailsViewModel);

	@NonNull
	@Binds
	@IntoMap
	@ViewModelKey(PeopleViewModel.class)
	abstract ViewModel bindPeopleViewModel(PeopleViewModel peopleViewModel);

	@NonNull
	@Binds
	@IntoMap
	@ViewModelKey(DashboardViewModel.class)
	abstract ViewModel bindDashboardViewModel(DashboardViewModel dashboardViewModel);


	@NonNull
	@Binds
	abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
