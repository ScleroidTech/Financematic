package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scleroid.financematic.utils.roomConverters.DateConverter;
import com.scleroid.financematic.utils.roomConverters.MoneyConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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
public class TransactionModel implements Serializable {

	@SerializedName("transaction_id")
	@PrimaryKey(autoGenerate = false)
	private int transactionId;
	@Ignore
	@SerializedName("userid")
	@Expose
	private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
	@SerializedName("mydate")
	@TypeConverters(DateConverter.class)
	private Date transactionDate;
	@TypeConverters(MoneyConverter.class)
	private BigDecimal lentAmt;
	@TypeConverters(MoneyConverter.class)
	private BigDecimal gainedAmt;
	@Ignore
	private LoanDurationType loan;
	@SerializedName("installment_amount")
	@TypeConverters(MoneyConverter.class)
	private BigDecimal receivedAmt;
	@SerializedName("description")
	private String description;
	@SerializedName("loan_id")
	private int loanAcNo;

	public TransactionModel(final int transactionId, final Date transactionDate,
	                        final BigDecimal lentAmt, final BigDecimal gainedAmt,
	                        final BigDecimal receivedAmt, final String description,
	                        final int loanAcNo) {
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.lentAmt = lentAmt;
		this.gainedAmt = gainedAmt;
		this.receivedAmt = receivedAmt;
		this.description = description;
		this.loanAcNo = loanAcNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public LoanDurationType getLoan() {
		return loan;
	}

	public void setLoan(final LoanDurationType loan) {
		this.loan = loan;
	}

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

	@Override
	public int hashCode() {

		return Objects.hash(transactionId, transactionDate, lentAmt, gainedAmt, receivedAmt,
				description, loanAcNo);
	}

	@Override
	public boolean equals(@Nullable final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final TransactionModel that = (TransactionModel) o;
		return transactionId == that.transactionId &&
				loanAcNo == that.loanAcNo &&
				Objects.equals(transactionDate, that.transactionDate) &&
				Objects.equals(lentAmt, that.lentAmt) &&
				Objects.equals(gainedAmt, that.gainedAmt) &&
				Objects.equals(receivedAmt, that.receivedAmt) &&
				Objects.equals(description, that.description);
	}

}
