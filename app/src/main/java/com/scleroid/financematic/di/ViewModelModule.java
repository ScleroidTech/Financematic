package com.scleroid.financematic.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.scleroid.financematic.ViewModelFactory;
import com.scleroid.financematic.fragments.customer.CustomerViewModel;
import com.scleroid.financematic.fragments.dashboard.DashboardViewModel;
import com.scleroid.financematic.fragments.expense.ExpenseViewModel;
import com.scleroid.financematic.fragments.loanDetails.LoanDetailsViewModel;
import com.scleroid.financematic.fragments.passbook.PassbookViewModel;
import com.scleroid.financematic.fragments.people.PeopleViewModel;

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
    @Binds
    @IntoMap
    @ViewModelKey(CustomerViewModel.class)
    abstract ViewModel bindCustomerViewModel(CustomerViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseViewModel.class)
    abstract ViewModel bindExpenseViewModel(ExpenseViewModel expenseViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoanDetailsViewModel.class)
    abstract ViewModel bindLoanDetailsViewModel(LoanDetailsViewModel loanDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PeopleViewModel.class)
    abstract ViewModel bindPeopleViewModel(PeopleViewModel peopleViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel.class)
    abstract ViewModel bindDashboardViewModel(DashboardViewModel dashboardViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PassbookViewModel.class)
    abstract ViewModel bindPassbookViewModel(PassbookViewModel passbookViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
