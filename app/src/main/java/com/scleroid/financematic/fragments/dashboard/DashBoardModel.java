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
    private String customerName;
    private BigDecimal amtDue;
    private Date installmentDate;

    public DashBoardModel(final String customerName, final BigDecimal amtDue, final Date installmentDate) {
        this.customerName = customerName;
        this.amtDue = amtDue;
        this.installmentDate = installmentDate;
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
