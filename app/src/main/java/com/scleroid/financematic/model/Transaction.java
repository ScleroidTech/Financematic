package com.scleroid.financematic.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
@Entity
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int transactionId;
    private Date transactionDate;
    private int lentAmt;
    private int gainedAmt;
    private int receivedAmt;

    public Transaction(Date transactionDate, int lentAmt, int gainedAmt, int receivedAmt) {


        this.transactionDate = transactionDate;
        this.lentAmt = lentAmt;
        this.gainedAmt = gainedAmt;
        this.receivedAmt = receivedAmt;
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
