package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scleroid.financematic.utils.roomConverters.DateConverter;
import com.scleroid.financematic.utils.roomConverters.MoneyConverter;

import java.math.BigDecimal;
import java.util.Date;

@Entity(indices = {@Index(value = "expenseId", unique = true)})
public class Expense {
	@TypeConverters(MoneyConverter.class)
	private BigDecimal expenseAmount;
	private String expenseType;
	@TypeConverters(DateConverter.class)
	private Date expenseDate;
	@PrimaryKey(autoGenerate = true)
	private int expenseId;

	public Expense(BigDecimal expenseAmount, String expenseType, Date expenseDate) {

		this.expenseAmount = expenseAmount;
		this.expenseType = expenseType;
		this.expenseDate = expenseDate;

	}

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
