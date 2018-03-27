package com.scleroid.financematic.model;

import java.util.Date;

/**
 * Created by scleroid on 27/3/18.
 */

public class Expense {
    public static final byte OTHER = 0;
    public static final byte ROOM_RENT = 1;
    public static final byte LIGHT_BILL = 2;
    public static final byte PHONE_BILL = 3;
    public static final byte PAID_SALARIES = 4;
    public static final byte FUEL = 5;

    private int expenseAmount;
    private byte expenseType;
    private Date expenseDate;

    public Expense(int expenseAmount, byte expenseType, Date expenseDate) {

        this.expenseAmount = expenseAmount;
        this.expenseType = expenseType;
        this.expenseDate = expenseDate;

    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public byte getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(byte expenseType) {
        if (expenseType > 6 && expenseType < 0) {
            throw new IllegalStateException("The value is not permitted");
        }
        this.expenseType = expenseType;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }
}
