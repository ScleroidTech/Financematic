package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.todoCode.LoanRepository;

import java.util.List;


/**
 * Created by Ganesh on 21-12-2017.
 */

public class TransactionViewModel extends AndroidViewModel {
    private final AppDatabase appDatabase;
    private LiveData<List<TransactionModel>> transactionList;

    public TransactionViewModel(@NonNull Application application, LoanRepository mLoanRepository) {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        transactionList = updateTransactionLiveData();
    }

    private LiveData<List<TransactionModel>> updateTransactionLiveData() {
        return appDatabase.transactionDao().getAllTransactionsLive();
    }

    public LiveData<List<TransactionModel>> getTransactionList() {
        if (transactionList == null) transactionList = updateTransactionLiveData();
        return transactionList;
    }
}
