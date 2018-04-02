package com.scleroid.financematic.model;



/**
 * Created by scleroid on 3/3/18.
 */

import java.util.Date;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Report {
    private String report_Acc_no, report_Lent, report_Earned, report_Balance;
    private Date transactionDate;

    public Report() {
    }

    public Report(String report_Acc_no, String report_Lent, Date transactionDate, String report_Earned, String report_Balance) {
        this.report_Acc_no= report_Acc_no;
        this.report_Lent= report_Lent;
        this.transactionDate = transactionDate;
        this.report_Earned = report_Earned;
        this.report_Balance=report_Balance;

    }

    public String getReport_Acc_no() {
        return report_Acc_no;
    }

    void setReport_Acc_no(String report_Acc_no) {
        this.report_Acc_no = report_Acc_no;
    }

    public String getReport_Lent() {
        return report_Lent;
    }

    void setReport_Lent(String report_Lent) {
        this.report_Lent = report_Lent;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReport_Earned() {
        return report_Earned;
    }

    void setReport_Earned(String report_Earned) {
        this.report_Earned = report_Earned;
    }

    public String getReport_Balance() {
        return report_Balance;
    }

    void setReport_Balance(String report_Balance) {
        this.report_Balance = report_Balance;
    }
}
