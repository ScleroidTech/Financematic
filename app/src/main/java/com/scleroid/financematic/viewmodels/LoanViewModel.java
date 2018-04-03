package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.data.local.model.Loan;

import java.util.List;


/**
 * Created by Ganesh on 13-12-2017.
 */

public class LoanViewModel extends AndroidViewModel {
    private LiveData<List<Loan>> loanList;
    private AppDatabase appDatabase;

    public LoanViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
        loanList = updateLoanLiveData();
    }

    private LiveData<List<Loan>> updateLoanLiveData() {

        LiveData<List<Loan>> loanList = appDatabase.loanDao().getAllLoansLive();
        return loanList;
    }

    public LiveData<List<Loan>> getLoanList() {
        if (loanList == null) {
            loanList = updateLoanLiveData();
        }

        return loanList;
    }

}
