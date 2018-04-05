package com.scleroid.financematic.data;

import com.scleroid.financematic.data.local.LocalCustomerData;
import com.scleroid.financematic.data.local.LocalExpenseData;
import com.scleroid.financematic.data.local.LocalLoanData;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public interface Repository {

    LocalCustomerData customerData();

    LocalExpenseData expenseData();

    LocalLoanData loanData();
}
