package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scleroid.financematic.utils.DateConverter;

import java.math.BigDecimal;
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
        onDelete = CASCADE),
        indices = {@Index(value = "transactionId", unique = true)})
public class TransactionModel {
    @PrimaryKey(autoGenerate = false)
    private int transactionId;
    @TypeConverters(DateConverter.class)
    private Date transactionDate;
    private BigDecimal lentAmt;
    private BigDecimal gainedAmt;
    private BigDecimal receivedAmt;

    private int loanAcNo;

    public TransactionModel(int transactionId, Date transactionDate, BigDecimal lentAmt, BigDecimal gainedAmt, BigDecimal receivedAmt, int loanAcNo) {
        this.transactionId = transactionId;
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

    public BigDecimal getLentAmt() {
        return lentAmt;
    }

    public void setLentAmt(BigDecimal lentAmt) {
        this.lentAmt = lentAmt;
    }

    public BigDecimal getGainedAmt() {
        return gainedAmt;
    }

    public void setGainedAmt(BigDecimal gainedAmt) {
        this.gainedAmt = gainedAmt;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }
}
