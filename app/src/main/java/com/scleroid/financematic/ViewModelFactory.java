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

import com.scleroid.financematic.fragments.customer.CustomerViewModel;
import com.scleroid.financematic.fragments.dashboard.DashboardViewModel;
import com.scleroid.financematic.fragments.expense.ExpenseViewModel;
import com.scleroid.financematic.fragments.loanDetails.LoanDetailsViewModel;
import com.scleroid.financematic.fragments.passbook.PassbookViewModel;
import com.scleroid.financematic.fragments.people.PeopleViewModel;

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



    public ViewModelFactory(Application application) {
        mApplication = application;
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
            return (T) new CustomerViewModel();
        } else if (modelClass.isAssignableFrom(ExpenseViewModel.class)) {
            //noinspection unchecked
            return (T) new ExpenseViewModel();

        } else if (modelClass.isAssignableFrom(LoanDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new LoanDetailsViewModel();
        } else if (modelClass.isAssignableFrom(PeopleViewModel.class)) {
            //noinspection unchecked
            return (T) new PeopleViewModel();
        } else if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            //noinspection unchecked
            return (T) new DashboardViewModel();
        } else if (modelClass.isAssignableFrom(PassbookViewModel.class)) {
            //noinspection unchecked
            return (T) new PassbookViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}