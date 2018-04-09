package com.scleroid.financematic.data.local;

import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.dao.TransactionDao;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/3/18
 */
public class DatabaseHelperLoan {
    private LoanDao dao;
    private TransactionDao transactionDao;

    @Inject
    public DatabaseHelperLoan(AppDatabase database) {
        dao = database.loanDao();
        transactionDao = database.transactionDao();
    }

 /*   public Loan getLoanLive(int id) {
        Loan loan = dao.getLoanLive(id);
        loan.setTransactions(dao.getTransactions(id));
        return loan;
    }

    public List<Loan> getLoans() {
        List<Loan> loans = dao.getLoans();
        for (Loan loan : loans) {
            loan.setTransactions(dao.getTransactions(loan.getAccountNo()));
        }
        return loans;
    }

    public void saveLoan(Loan loan) {
        dao.saveLoan(loan);
        transactionDao.saveTransactions(loan.getTransactions());
    }

    public void saveLoans(List<Loan> loans) {
        dao.saveLoans(loans);
        for (Loan loan : loans) {
            transactionDao.saveTransactions(loan.getTransactions());
        }
    }*/

}
