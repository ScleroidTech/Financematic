package com.scleroid.financematic.model;



/**
 * Created by scleroid on 3/3/18.
 */

/**
 * Created by Lincoln on 15/01/16.
 */
public class Report {
    private String report_Acc_no, report_Lent, report_Interest,report_Earned,report_Balance;

    public Report() {
    }

    public Report(String report_Acc_no, String report_Lent, String report_Interest,String report_Earned, String report_Balance) {
        this.report_Acc_no= report_Acc_no;
        this.report_Lent= report_Lent;
        this.report_Interest = report_Interest;
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

    public String getReport_Interest() {
        return report_Interest;
    }

    void setReport_Interest(String report_Interest) {
        this.report_Interest = report_Interest;
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
