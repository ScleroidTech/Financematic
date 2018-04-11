package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/10/18
 */
public class CustomerWithLoans {
	//TODO add later
	@Embedded
	public Customer customer;

	@Relation(parentColumn = "customerId",
			entityColumn = "custId")
	public List<Loan> loanList;

	@Relation(parentColumn = "accountNo",
			entityColumn = "loanAcNo")
	public List<List<TransactionModel>> transactionsList;
}
