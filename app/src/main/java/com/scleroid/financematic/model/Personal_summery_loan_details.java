package com.scleroid.financematic.model;

/**
 * Created by scleroid on 6/3/18.
 */




public class Personal_summery_loan_details {
    private String summery_date,summery_descpription ,summery_amount;

    public Personal_summery_loan_details() {
    }

    public Personal_summery_loan_details(String summery_date, String summery_descpription, String summery_amount) {
        this.summery_date = summery_date;
        this.summery_descpription= summery_descpription;
        this.summery_amount= summery_amount;

    }

    public String getSummery_date() {
        return summery_date;
    }

    void setSummery_date(String summery_date) {
        this.summery_date = summery_date;
    }

    public String getSummery_descpription() {
        return summery_descpription;
    }

    void setSummery_descpription(String summery_descpription) {
        this.summery_descpription = summery_descpription;
    }

    public String getSummery_amount() {
        return summery_amount;
    }

    void setSummery_amount(String summery_amount) {
        this.summery_amount = summery_amount;
    }
}
