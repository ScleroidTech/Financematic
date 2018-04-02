package com.scleroid.financematic.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Lincoln on 15/01/16.
 */

@Entity(foreignKeys = @ForeignKey(entity = Customer.class,
        parentColumns = "_id",
        childColumns = "custId",
        onDelete = CASCADE))
public class Loan {
    /* private String title, genre, year;
     */
    private int loanAmt;
    private Date startDate;
    private Date endDate;
    private float rateOfInterest;
    private int amtOfInterest;
    private int noOfInstallments;
    private int duration;
    private String installmentType;
    private int repayAmt;
    @PrimaryKey
    private int accountNo;

    private int custId;

    public Loan(int loanAmt, Date startDate, Date endDate, float rateOfInterest, int amtOfInterest, int noOfInstallments, int duration, String installmentType, int repayAmt, int custId) {
        this.loanAmt = loanAmt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rateOfInterest = rateOfInterest;
        this.amtOfInterest = amtOfInterest;
        this.noOfInstallments = noOfInstallments;
        this.duration = duration;
        this.installmentType = installmentType;
        this.repayAmt = repayAmt;
        this.custId = custId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }



    public int getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(int loanAmt) {
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

    public int getAmtOfInterest() {
        return amtOfInterest;
    }

    public void setAmtOfInterest(int amtOfInterest) {
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

    public String getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(String installmentType) {
        this.installmentType = installmentType;
    }

    public int getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(int repayAmt) {
        this.repayAmt = repayAmt;
    }

    public int getAccountNo() {

        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }



  /*  public Loan(String title, String genre, String year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }*/
}
