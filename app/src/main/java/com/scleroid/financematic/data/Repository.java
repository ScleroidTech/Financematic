package com.scleroid.financematic.data;

import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalExpenseLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public interface Repository {

    LocalCustomerLab customerData();

    LocalExpenseLab expenseData();

    LocalLoanLab loanData();
}
