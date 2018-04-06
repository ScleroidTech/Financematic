package com.scleroid.financematic.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.scleroid.financematic.ViewModelFactory;
import com.scleroid.financematic.viewmodels.CustomerViewModel;
import com.scleroid.financematic.viewmodels.ExpenseViewModel;
import com.scleroid.financematic.viewmodels.LoanViewModel;
import com.scleroid.financematic.viewmodels.TransactionViewModel;

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
abstract class ViewModelModule {
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
    @ViewModelKey(LoanViewModel.class)
    abstract ViewModel bindLoanViewModel(LoanViewModel loanViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel.class)
    abstract ViewModel bindTransactionViewModel(TransactionViewModel transactionViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
