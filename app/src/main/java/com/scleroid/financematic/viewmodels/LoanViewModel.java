package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Loan;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by Ganesh on 13-12-2017.
 */

public class LoanViewModel extends BaseViewModel {
    private LiveData<List<Loan>> loanList;



    private LiveData<Loan> loanLiveData;

    @Inject
    public LoanViewModel(Application appDatabase) {




        loanList = updateLoanLiveData();
    }

    private LiveData<List<Loan>> updateLoanLiveData() {
//TODO This doesnt work
        final Object loanRepository = null;
        //  LiveData<List<Loan>> loanList = loanRepository.getLoans();
        return loanList;
    }

    public LiveData<List<Loan>> getLoanList() {
        if (loanList == null) {
            loanList = updateLoanLiveData();
        }

        return loanList;
    }

}
