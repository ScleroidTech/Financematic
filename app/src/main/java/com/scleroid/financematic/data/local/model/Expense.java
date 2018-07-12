package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scleroid.financematic.utils.roomconverters.DateConverter;
import com.scleroid.financematic.utils.roomconverters.MoneyConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(indices = {@Index(value = "expenseId", unique = true)})
public class Expense implements Serializable {
	@TypeConverters(MoneyConverter.class)
	private BigDecimal expenseAmount;
	private String expenseType;
	@TypeConverters(DateConverter.class)
	private Date expenseDate;
	@PrimaryKey(autoGenerate = true)
	private int expenseId;
	@Ignore
	@SerializedName("userid")
	@Expose
	private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

	public Expense(BigDecimal expenseAmount, String expenseType, Date expenseDate) {

		this.expenseAmount = expenseAmount;
		this.expenseType = expenseType;
		this.expenseDate = expenseDate;

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	@NonNull
	@Override
	public String toString() {
		return "Expense{" +
				"expenseAmount=" + expenseAmount.intValue() +
				", expenseType=" + expenseType +
				", expenseDate=" + expenseDate +
				", expenseId=" + expenseId +
				'}';
	}

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {

		this.expenseType = expenseType;
	}

	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}
}
