package com.scleroid.financematic;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/4/18
 */

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import com.scleroid.financematic.data.todoCode.LoanRepository;
import com.scleroid.financematic.viewmodels.CustomerViewModel;
import com.scleroid.financematic.viewmodels.ExpenseViewModel;
import com.scleroid.financematic.viewmodels.LoanViewModel;
import com.scleroid.financematic.viewmodels.TransactionViewModel;

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

    private final LoanRepository mLoanRepository;

    public ViewModelFactory(Application application) {
        mApplication = application;
    }

    private ViewModelFactory(Application application, LoanRepository loanRepository) {
        mApplication = application;
        mLoanRepository = loanRepository;
    }

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CustomerViewModel.class)) {
            //noinspection unchecked
            return (T) new CustomerViewModel(mApplication, mLoanRepository);
        } else if (modelClass.isAssignableFrom(ExpenseViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpenseViewModel(mApplication, mLoanRepository);
        } else if (modelClass.isAssignableFrom(LoanViewModel.class)) {
            //noinspection unchecked
            return (T) new LoanViewModel(mApplication, mLoanRepository);
        } else if (modelClass.isAssignableFrom(TransactionViewModel.class)) {
            //noinspection unchecked
            return (T) new TransactionViewModel(mApplication, mLoanRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}