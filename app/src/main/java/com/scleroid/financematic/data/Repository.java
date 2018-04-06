package com.scleroid.financematic.data;

import com.scleroid.financematic.data.local.repo.LocalCustomerRepo;
import com.scleroid.financematic.data.local.repo.LocalExpenseRepo;
import com.scleroid.financematic.data.local.repo.LocalLoanRepo;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public interface Repository {

    LocalCustomerRepo customerData();

    LocalExpenseRepo expenseData();

    LocalLoanRepo loanData();
}
