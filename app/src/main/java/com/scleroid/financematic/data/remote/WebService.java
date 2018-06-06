package com.scleroid.financematic.data.remote;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;

import hugo.weaving.DebugLog;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public interface WebService {
	@FormUrlEncoded
	@DebugLog
	@POST("/mobile/registerusercid")
	LiveData<ApiResponse<Customer>> getCustomer(@Field("customer_id") int customerId);


	@DebugLog
	@GET("/mobile/registerusers")
	LiveData<ApiResponse<List<Customer>>> getCustomers();

	@DebugLog
	@POST("/mobile/lastinsertnewuserloan_id")
	LiveData<ApiResponse<Loan>> getLoan(@Field("loan") int loanId);

	@DebugLog
	@GET("/mobile/loandetaillist")
	LiveData<ApiResponse<List<Loan>>> getLoans();

	//done
	@DebugLog
	@GET("/mobile/loandetailloanact/{customer_id}")
	LiveData<ApiResponse<List<Loan>>> getLoans(@Path("customer_id") int customerId);

	@DebugLog
	@GET("/mobile/getexpenditure")
	LiveData<ApiResponse<List<Expense>>> getExpenses();

	@DebugLog
	@GET("/mobile/getexpenditureid/{id}")
	LiveData<ApiResponse<Expense>> getExpense(@Path("id") int expenseNo);

	@FormUrlEncoded
	@DebugLog
	@POST("/mobile/transactionloan_id/")
	LiveData<ApiResponse<List<TransactionModel>>> getTransactionsForLoan(
			@Field("loan_id") int loanAcNo);

	//Done
	@DebugLog
	@GET("/mobile/transaction/")
	LiveData<ApiResponse<List<TransactionModel>>> getTransactions();

	@DebugLog
	@FormUrlEncoded
	@POST("/mobile/transactiontransaction_id/")
	LiveData<ApiResponse<TransactionModel>> getTransaction(
			@Field("transaction_id") int transactionNo);

	@DebugLog
	@GET("/mobile/installmentloan/{installmentNo}")
	LiveData<ApiResponse<Installment>> getInstallment(@Path("installmentNo") int installmentNo);

	@DebugLog
	@GET("/mobile/installmentlist/")
	LiveData<ApiResponse<List<Installment>>> getInstallments();

	//Done
	@DebugLog
	@GET("/mobile/installementloan_id/{loan_id}")
	LiveData<ApiResponse<List<Installment>>> getInstallmentsForLoan(@Path("loan_id") int loanAcNo);
}
