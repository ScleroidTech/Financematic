package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.todoCode.LoanRepository;

import java.util.List;


/**
 * @author Ganesh Kaple
 * @since 09-01-2018
 */

public class ExpenseViewModel extends AndroidViewModel {
    //  private Parcel parcel;
    private List<Expense> expenses;

    private LiveData<List<Expense>> expenseList;

    private AppDatabase appDatabase;

    public ExpenseViewModel(@NonNull Application application, LoanRepository mLoanRepository) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());

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
