package com.scleroid.financematic.viewmodels;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;


/**
 * Created by Ganesh on 21-12-2017.
 */

public class TransactionViewModel extends BaseViewModel {

    private LiveData<List<TransactionModel>> transactionList;

    public TransactionViewModel(@NonNull AppDatabase application) {
        super(application);

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
