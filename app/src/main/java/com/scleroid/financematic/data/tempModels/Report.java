package com.scleroid.financematic.data.tempModels;


/**
 * Created by scleroid on 3/3/18.
 */

import java.util.Date;


public class Report {
    private String accNo, lentAmt, earnedAmt, balanceAmt;
    private Date transactionDate;

    public Report() {
    }

    public Report(String accNo, String lentAmt, Date transactionDate, String earnedAmt, String balanceAmt) {
        this.accNo = accNo;
        this.lentAmt = lentAmt;
        this.transactionDate = transactionDate;
        this.earnedAmt = earnedAmt;
        this.balanceAmt = balanceAmt;

    }

    public String getAccNo() {
        return accNo;
    }

    void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getLentAmt() {
        return lentAmt;
    }

    void setLentAmt(String lentAmt) {
        this.lentAmt = lentAmt;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getEarnedAmt() {
        return earnedAmt;
    }

    void setEarnedAmt(String earnedAmt) {
        this.earnedAmt = earnedAmt;
    }

    public String getBalanceAmt() {
        return balanceAmt;
    }

    void setBalanceAmt(String balanceAmt) {
        this.balanceAmt = balanceAmt;
    }
}
