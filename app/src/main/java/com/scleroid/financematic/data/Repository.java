package com.scleroid.financematic.data;

import android.support.annotation.NonNull;

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

	@NonNull
	LocalCustomerLab customerData();

	@NonNull
	LocalExpenseLab expenseData();

	@NonNull
	LocalLoanLab loanData();
}
