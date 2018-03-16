package com.scleroid.financematic;

/**
 * Created by scleroid on 6/3/18.
 */


public class List_all_peoples {
    private String list_person_name, list_received_amoun,list_total_loan;

    public List_all_peoples() {
    }

    public List_all_peoples(String list_person_name, String list_received_amoun, String list_total_loan) {
        this.list_person_name = list_person_name;
        this.list_received_amoun = list_received_amoun;
        this.list_total_loan = list_total_loan;
    }

    String getList_person_name() {
        return list_person_name;
    }

    void setList_person_name(String list_person_name) {
        this.list_person_name = list_person_name;
    }

    String getList_received_amoun() {
        return list_received_amoun;
    }

    void setList_received_amoun(String list_received_amoun) {
        this.list_received_amoun = list_received_amoun;
    }

    String getList_total_loan() {
        return list_total_loan;
    }

    void setList_total_loan(String list_total_loan) {
        this.list_total_loan = list_total_loan;
    }
}
