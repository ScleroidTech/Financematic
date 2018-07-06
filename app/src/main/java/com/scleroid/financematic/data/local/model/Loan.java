package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scleroid.financematic.utils.roomConverters.DateConverter;
import com.scleroid.financematic.utils.roomConverters.MoneyConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Lincoln on 15/01/16.
 */

@Entity(foreignKeys = @ForeignKey(entity = Customer.class,
		parentColumns = "customerId",
		childColumns = "custId",
		onDelete = CASCADE),
		indices = {@Index(value = "accountNo", unique = true)})
public class Loan implements Serializable {
	/* private String title, genre, year;
	 */


	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	@Ignore
	@SerializedName("userid")
	@Expose
	private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

	@Ignore
	private Customer customer;
	@SerializedName("receivedamt")
	@TypeConverters(MoneyConverter.class)
	private BigDecimal receivedAmt;
	@SerializedName("loan_amount")
	@TypeConverters(MoneyConverter.class)
	private BigDecimal loanAmt;

	@SerializedName("startdate")
	@TypeConverters(DateConverter.class)
	private Date startDate;

	@SerializedName("enddate")
	@TypeConverters(DateConverter.class)
	private Date endDate;
	@SerializedName("interest")
	@TypeConverters(MoneyConverter.class)
	private BigDecimal interestAmt;

	@SerializedName("installement_amount")
	@TypeConverters(MoneyConverter.class)
	private BigDecimal installmentAmt;

	@SerializedName("noofinstallement")
	private int noOfInstallments;

	@SerializedName("installementtype")
	private String installmentType;
	@SerializedName("trepayamount")
	@TypeConverters(MoneyConverter.class)
	private BigDecimal repayAmt;
	@SerializedName("loan_id")
	@PrimaryKey
	private int accountNo;
	@SerializedName("cid")
	private int custId;


	@Ignore
	public Loan(int accountNo, BigDecimal loanAmt, Date startDate, Date endDate,
	            BigDecimal interestAmt,
	            BigDecimal installmentAmt, int noOfInstallments,
	            String installmentType, int custId) {
		this(accountNo, loanAmt, startDate, endDate, interestAmt, installmentAmt,
				noOfInstallments, installmentType, new BigDecimal(0), custId, new BigDecimal(0));

	}

	public Loan(final int accountNo, final BigDecimal loanAmt, final Date startDate,
	            final Date endDate,
	            final BigDecimal interestAmt,
	            final BigDecimal installmentAmt, final int noOfInstallments,
	            final String installmentType,
	            final BigDecimal repayAmt,
	            final int custId, final BigDecimal receivedAmt) {
		this.loanAmt = loanAmt;
		this.startDate = startDate;
		this.endDate = endDate;
		this.interestAmt = interestAmt;
		this.installmentAmt = installmentAmt;
		this.noOfInstallments = noOfInstallments;
		this.installmentType = installmentType;
		this.repayAmt = repayAmt;
		this.receivedAmt = receivedAmt;
		this.accountNo = accountNo;
		this.custId = custId;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}


	@NonNull
	@Override
	public String toString() {
		return "Loan{" +
				"loanAmt=" + loanAmt +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", interestAmt=" + interestAmt +
				", installmentAmt=" + installmentAmt +
				", noOfInstallments=" + noOfInstallments +

				", installmentType=" + installmentType +
				", repayAmt=" + repayAmt +
				", accountNo=" + accountNo +
				", custId=" + custId +
				", receivedAmt=" + receivedAmt +
				'}';
	}


	public BigDecimal getReceivedAmt() {
		return receivedAmt;
	}

	public void setReceivedAmt(final BigDecimal receivedAmt) {
		this.receivedAmt = receivedAmt;
	}

	public BigDecimal getLoanAmt() {
		return loanAmt;
	}

	public void setLoanAmt(BigDecimal loanAmt) {
		this.loanAmt = loanAmt;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getInterestAmt() {
		return interestAmt;
	}

	public void setInterestAmt(BigDecimal interestAmt) {
		this.interestAmt = interestAmt;
	}

	public BigDecimal getInstallmentAmt() {
		return installmentAmt;
	}

	public void setInstallmentAmt(BigDecimal installmentAmt) {
		this.installmentAmt = installmentAmt;
	}

	public int getNoOfInstallments() {
		return noOfInstallments;
	}

	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}

	public String getInstallmentType() {
		return installmentType;
	}

	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}

	public BigDecimal getRepayAmt() {
		return repayAmt;
	}

	public void setRepayAmt(BigDecimal repayAmt) {
		this.repayAmt = repayAmt;
	}

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

}
