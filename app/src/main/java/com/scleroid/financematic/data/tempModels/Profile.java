package com.scleroid.financematic.data.tempModels;

/**
 * Created by scleroid on 5/4/18.
 */

public class Profile {

    private String title;
    private String startDate1;
    private String Total_loan;
    private String ReceivedAmt;
    private String endDate1;

    public  Profile() {}
    public Profile(String title, String startDate1, String Total_loan,String ReceivedAmt,String endDate1) {
        this.title = title;
        this.startDate1 = startDate1;
        this.Total_loan = Total_loan;
        this.endDate1 =endDate1;
        this.ReceivedAmt =ReceivedAmt;
    }

    public String getTitle() {return title;}
    void setTitle(String title) {this.title = title;}

    public String getStartDate1() {return startDate1;}
    void setStartDate1(String startDate1) {this.startDate1 = startDate1;}

    public String getTotal_loan() {return Total_loan;}
    void setTotal_loan(String total_loan) {Total_loan = total_loan;}

    public String getReceivedAmt() {
        return ReceivedAmt;
    }

    void setReceivedAmt(String receivedAmt) {
        ReceivedAmt = receivedAmt;
    }

    public String getEndDate1() {
        return endDate1;
    }

    void setEndDate1(String endDate1) {
        this.endDate1 = endDate1;
    }
}
