package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scleroid.financematic.utils.DateConverter;

import java.util.Date;

@Entity(indices = {@Index(value = "expenseId", unique = true)})
public class Expense {
    public static final byte OTHER = 0;
    public static final byte ROOM_RENT = 1;
    public static final byte LIGHT_BILL = 2;
    public static final byte PHONE_BILL = 3;
    public static final byte PAID_SALARIES = 4;
    public static final byte FUEL = 5;

    private int expenseAmount;
    private byte expenseType;
    @TypeConverters(DateConverter.class)
    private Date expenseDate;

    @PrimaryKey(autoGenerate = true)
    private int expenseId;

    public Expense(int expenseAmount, byte expenseType, Date expenseDate) {

        this.expenseAmount = expenseAmount;
        this.expenseType = expenseType;
        this.expenseDate = expenseDate;

    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
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
