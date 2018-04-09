package com.scleroid.financematic.viewmodels;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Expense;

import java.util.List;


/**
 * @author Ganesh Kaple
 * @since 09-01-2018
 */

public class ExpenseViewModel extends BaseViewModel {
    //  private Parcel parcel;
    private List<Expense> expenses;

    private LiveData<List<Expense>> expenseList;

    private AppDatabase appDatabase;

    public ExpenseViewModel(@NonNull AppDatabase application) {
        super(application);

        expenseList = updateExpenseLiveData();
    }

    private LiveData<List<Expense>> updateExpenseLiveData() {
        //TODO use Network based data source here
        LiveData<List<Expense>> expenseList = appDatabase.expenseDao().getAllExpenseLive();
        return expenseList;
    }

    public LiveData<List<Expense>> getExpenseList() {
        if (expenseList == null) {
            expenseList = updateExpenseLiveData();
        }

        return expenseList;
    }

}
