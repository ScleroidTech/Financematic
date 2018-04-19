package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scleroid.financematic.utils.roomConverters.DateConverter;
import com.scleroid.financematic.utils.roomConverters.MoneyConverter;

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
public class Loan {
	/* private String title, genre, year;
	 */

	@Ignore
	private Customer customer;
	@TypeConverters(MoneyConverter.class)
	private BigDecimal receivedAmt;
	@TypeConverters(MoneyConverter.class)
	private BigDecimal loanAmt;
	@TypeConverters(DateConverter.class)
	private Date startDate;
	@TypeConverters(DateConverter.class)
	private Date endDate;
	private float rateOfInterest;
	@TypeConverters(MoneyConverter.class)
	private BigDecimal installmentAmt;
	private int noOfInstallments;
	private String installmentType;
	@TypeConverters(MoneyConverter.class)
	private BigDecimal repayAmt;
	@PrimaryKey
	private int accountNo;
	private int custId;


	@Ignore
	public Loan(int accountNo, BigDecimal loanAmt, Date startDate, Date endDate,
	            float rateOfInterest,
	            BigDecimal installmentAmt, int noOfInstallments,
	            String installmentType,
	            BigDecimal repayAmt, int custId) {
		this(accountNo, loanAmt, startDate, endDate, rateOfInterest, installmentAmt,
				noOfInstallments, installmentType, repayAmt, custId, null);

	}

	public Loan(final int accountNo, final BigDecimal loanAmt, final Date startDate,
	            final Date endDate,
	            final float rateOfInterest,
	            final BigDecimal installmentAmt, final int noOfInstallments,
	            final String installmentType,
	            final BigDecimal repayAmt,
	            final int custId, final BigDecimal receivedAmt) {
		this.loanAmt = loanAmt;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rateOfInterest = rateOfInterest;
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

	@Override
	public String toString() {
		return "Loan{" +
				"loanAmt=" + loanAmt.intValue() +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", rateOfInterest=" + rateOfInterest +
				", installmentAmt=" + installmentAmt.intValue() +
				", noOfInstallments=" + noOfInstallments +

				", installmentType=" + installmentType +
				", repayAmt=" + repayAmt.intValue() +
				", accountNo=" + accountNo +
				", custId=" + custId +
				", receivedAmt=" + receivedAmt.intValue() +
				'}';
	}


 /*   public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }
*/
  /*  @Ignore
    private List<TransactionModel> transactions ;
*/

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

	public float getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(float rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
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
/*  public Loan(String title, String genre, String year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getCustomerName() {
        return title;
    }

    public void setCustomerName(String name) {
        this.title = name;
    }

    public String getInstallmentDate() {
        return year;
    }

    public void setInstallmentDate(String year) {
        this.year = year;
    }

    public String getAmtDue() {
        return genre;
    }

    public void setAmtDue(String genre) {
        this.genre = genre;
    }*/
}
