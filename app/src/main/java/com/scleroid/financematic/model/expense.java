package com.scleroid.financematic.model;

/**
 * Created by scleroid on 27/3/18.
 */

public class expense {


    private String expense_amount,expense_image ,expense_type;

    public expense() {
    }


    expense(String expense_amount, String expense_image, String expense_type) {
        this.expense_amount = expense_amount;
        this.expense_image = expense_image;
        this.expense_type = expense_type;
    }

    String getExpense_amount() {
        return expense_amount;
    }

    void setExpense_amount(String expense_amount) {
        this.expense_amount = expense_amount;
    }

    String getExpense_image() {
        return expense_image;
    }

    void setExpense_image(String expense_image) {
        this.expense_image = expense_image;
    }

    String getExpense_type() {
        return expense_type;
    }

    void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }
}
