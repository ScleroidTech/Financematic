package com.scleroid.financematic.data.tempModels;

/**
 * Created by scleroid on 5/3/18.
 */

@Deprecated
public class Passbook {
    private String passbook_date, passbook_name, passbook_taken_money, passbook_received_money;

    public Passbook() {
    }

    public Passbook(String passbook_date, String passbook_name, String passbook_taken_money, String passbook_received_money) {
        this.passbook_date = passbook_date;
        this.passbook_name = passbook_name;
        this.passbook_taken_money = passbook_taken_money;
        this.passbook_received_money = passbook_received_money;
    }

    public String getPassbook_date() {
        return passbook_date;
    }

    void setPassbook_date(String passbook_date) {
        this.passbook_date = passbook_date;
    }

    public String getPassbook_name() {
        return passbook_name;
    }

    void setPassbook_name(String passbook_name) {
        this.passbook_name = passbook_name;
    }

    public String getPassbook_taken_money() {
        return passbook_taken_money;
    }

    void setPassbook_taken_money(String passbook_taken_money) {
        this.passbook_taken_money = passbook_taken_money;
    }

    public String getPassbook_received_money() {
        return passbook_received_money;
    }

    void setPassbook_received_money(String passbook_received_money) {
        this.passbook_received_money = passbook_received_money;
    }
}
