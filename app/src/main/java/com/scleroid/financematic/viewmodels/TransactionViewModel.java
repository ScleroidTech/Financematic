package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.model.TransactionModel;

import java.util.List;


/**
 * Created by Ganesh on 21-12-2017.
 */

public class TransactionViewModel extends AndroidViewModel {
    private final AppDatabase appDatabase;
    private LiveData<List<TransactionModel>> transactionList;

    public TransactionViewModel(@NonNull Application application) {
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
