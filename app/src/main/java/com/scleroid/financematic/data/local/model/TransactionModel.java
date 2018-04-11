package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scleroid.financematic.utils.roomConverters.DateConverter;
import com.scleroid.financematic.utils.roomConverters.MoneyConverter;

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
    @TypeConverters(MoneyConverter.class)
    private BigDecimal lentAmt;
    @TypeConverters(MoneyConverter.class)
    private BigDecimal gainedAmt;

    @Override
    public String toString() {
        return "TransactionModel{" +
                "transactionId=" + transactionId +
                ", transactionDate=" + transactionDate +
                ", lentAmt=" + lentAmt.intValue() +
                ", gainedAmt=" + gainedAmt.intValue() +
                ", receivedAmt=" + receivedAmt.intValue() +
                ", description='" + description + '\'' +
                ", loanAcNo=" + loanAcNo +
                '}';
    }

    @TypeConverters(MoneyConverter.class)
    private BigDecimal receivedAmt;

    private String description;
    private int loanAcNo;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getLentAmt() {
        return lentAmt;
    }

    public void setLentAmt(final BigDecimal lentAmt) {
        this.lentAmt = lentAmt;
    }

    public BigDecimal getGainedAmt() {
        return gainedAmt;
    }

    public void setGainedAmt(final BigDecimal gainedAmt) {
        this.gainedAmt = gainedAmt;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(final BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getLoanAcNo() {
        return loanAcNo;
    }

    public void setLoanAcNo(final int loanAcNo) {
        this.loanAcNo = loanAcNo;
    }

    public TransactionModel(final int transactionId, final Date transactionDate, final BigDecimal lentAmt, final BigDecimal gainedAmt, final BigDecimal receivedAmt, final String description, final int loanAcNo) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.lentAmt = lentAmt;
        this.gainedAmt = gainedAmt;
        this.receivedAmt = receivedAmt;
        this.description = description;
        this.loanAcNo = loanAcNo;
    }

}
