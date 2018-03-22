package com.scleroid.financematic.model;

/**
 * Created by scleroid on 6/3/18.
 */


public class List_all_peoples {
    int list_received_amoun;
    int list_total_loan;
    private String list_person_name;

    public List_all_peoples() {
    }

    public List_all_peoples(String list_person_name, int list_received_amoun, int list_total_loan) {
        this.list_person_name = list_person_name;
        this.list_received_amoun = list_received_amoun;
        this.list_total_loan = list_total_loan;
    }

    public String getList_person_name() {
        return list_person_name;
    }

    public void setList_person_name(String list_person_name) {
        this.list_person_name = list_person_name;
    }

    public int getList_received_amoun() {
        return list_received_amoun;
    }

    public void setList_received_amoun(int list_received_amoun) {
        this.list_received_amoun = list_received_amoun;
    }

    public int getList_total_loan() {
        return list_total_loan;
    }

    public void setList_total_loan(int list_total_loan) {
        this.list_total_loan = list_total_loan;
    }
}
