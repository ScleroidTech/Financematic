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
public class LoansWithCustomer {
	//TODO add later
	@Embedded
	public IdProofType idProofType;

	@Relation(parentColumn = "customerId",
			entityColumn = "custId")
	public List<LoanDurationType> loanList;

	@Relation(parentColumn = "accountNo",
			entityColumn = "loanAcNo")
	public List<List<TransactionModel>> transactionsList;
}
