package com.scleroid.financematic.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scleroid.financematic.utils.DateConverter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
@Entity(foreignKeys = @ForeignKey(entity = Loan.class,
        parentColumns = "accountNo",
        childColumns = "loanAcNo",
        onDelete = CASCADE))
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int transactionId;
    @TypeConverters(DateConverter.class)
    private Date transactionDate;
    private int lentAmt;
    private int gainedAmt;
    private int receivedAmt;

    private int loanAcNo;

    public Transaction(Date transactionDate, int lentAmt, int gainedAmt, int receivedAmt, int loanAcNo) {
        this.transactionDate = transactionDate;
        this.lentAmt = lentAmt;
        this.gainedAmt = gainedAmt;
        this.receivedAmt = receivedAmt;
        this.loanAcNo = loanAcNo;
    }

    public int getLoanAcNo() {
        return loanAcNo;
    }

    public void setLoanAcNo(int loanAcNo) {
        this.loanAcNo = loanAcNo;
    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getLentAmt() {
        return lentAmt;
    }

    public void setLentAmt(int lentAmt) {
        this.lentAmt = lentAmt;
    }

    public int getGainedAmt() {
        return gainedAmt;
    }

    public void setGainedAmt(int gainedAmt) {
        this.gainedAmt = gainedAmt;
    }

    public int getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(int receivedAmt) {
        this.receivedAmt = receivedAmt;
    }
}
