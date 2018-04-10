package com.scleroid.financematic.fragments.dashboard;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
public class DashBoardModel {
    private int customerNo;
    private int loanAcNo;
    private int installationNo;
    private String customerName;
    private String mobileNo;
    private BigDecimal amtDue;
    private Date installmentDate;

    public DashBoardModel(final int customerNo, final int loanAcNo, final int installationNo,
                          final String customerName, final String mobileNo, final BigDecimal amtDue,
                          final Date installmentDate) {
        this.customerNo = customerNo;
        this.loanAcNo = loanAcNo;
        this.installationNo = installationNo;
        this.customerName = customerName;
        this.mobileNo = mobileNo;
        this.amtDue = amtDue;
        this.installmentDate = installmentDate;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(final int customerNo) {
        this.customerNo = customerNo;
    }

    public int getLoanAcNo() {
        return loanAcNo;
    }

    public void setLoanAcNo(final int loanAcNo) {
        this.loanAcNo = loanAcNo;
    }

    public int getInstallationNo() {
        return installationNo;
    }

    public void setInstallationNo(final int installationNo) {
        this.installationNo = installationNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmtDue() {
        return amtDue;
    }

    public void setAmtDue(final BigDecimal amtDue) {
        this.amtDue = amtDue;
    }

    public Date getInstallmentDate() {
        return installmentDate;
    }

    public void setInstallmentDate(final Date installmentDate) {
        this.installmentDate = installmentDate;
    }
}
