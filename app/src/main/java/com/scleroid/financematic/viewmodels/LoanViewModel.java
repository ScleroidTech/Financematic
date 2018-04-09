package com.scleroid.financematic.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.todoCode.LoanRepository;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by Ganesh on 13-12-2017.
 */

public class LoanViewModel extends ViewModel {
    private LiveData<List<Loan>> loanList;
    private AppDatabase appDatabase;

    private final LoanRepository loanRepository;
    private LiveData<Loan> loanLiveData;

    @Inject
    public LoanViewModel(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;



        loanList = updateLoanLiveData();
    }

    private LiveData<List<Loan>> updateLoanLiveData() {

        LiveData<List<Loan>> loanList = loanRepository.getLoans();
        return loanList;
    }

    public LiveData<List<Loan>> getLoanList() {
        if (loanList == null) {
            loanList = updateLoanLiveData();
        }

        return loanList;
    }

}
