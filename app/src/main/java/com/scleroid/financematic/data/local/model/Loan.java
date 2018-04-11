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
    public static final byte DAILY = 0;
    @Ignore
    public static final byte WEEKLY = 1;
    @Ignore
    public static final byte BIWEEKLY = 2;
    @Ignore
    public static final byte MONTHLY = 3;

	@Ignore
	private Customer customer;

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
                ", amtOfInterest=" + amtOfInterest.intValue() +
                ", noOfInstallments=" + noOfInstallments +
                ", duration=" + duration +
                ", installmentType=" + installmentType +
                ", repayAmt=" + repayAmt.intValue() +
                ", accountNo=" + accountNo +
                ", custId=" + custId +
                '}';
    }

    @Ignore
    public static final byte BIMONTHLY = 4;
    @Ignore
    public static final byte QUARTERLY = 5;
    @Ignore
    public static final byte HALF_YEALY = 6;
    @Ignore
    public static final byte YEARLY = 7;
    @Ignore
    public static final byte ONE_TIME = 8;

    @TypeConverters(MoneyConverter.class)
    private BigDecimal loanAmt;
    @TypeConverters(DateConverter.class)
    private Date startDate;
    @TypeConverters(DateConverter.class)
    private Date endDate;
    private float rateOfInterest;
    @TypeConverters(MoneyConverter.class)
    private BigDecimal amtOfInterest;
    private int noOfInstallments;
    private int duration;
    private byte installmentType;
    @TypeConverters(MoneyConverter.class)
    private BigDecimal repayAmt;
    @PrimaryKey
    private int accountNo;

    private int custId;


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

    public Loan(BigDecimal loanAmt, Date startDate, Date endDate, float rateOfInterest, BigDecimal amtOfInterest, int noOfInstallments, int duration, byte installmentType, BigDecimal repayAmt, int accountNo, int custId) {
        this.loanAmt = loanAmt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rateOfInterest = rateOfInterest;
        this.amtOfInterest = amtOfInterest;
        this.noOfInstallments = noOfInstallments;
        this.duration = duration;
        this.installmentType = installmentType;
        this.repayAmt = repayAmt;
        this.accountNo = accountNo;
        this.custId = custId;
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

    public BigDecimal getAmtOfInterest() {
        return amtOfInterest;
    }

    public void setAmtOfInterest(BigDecimal amtOfInterest) {
        this.amtOfInterest = amtOfInterest;
    }

    public int getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(int noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public byte getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(byte installmentType) {
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
