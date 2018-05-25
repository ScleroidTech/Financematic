package com.scleroid.financematic.data.remote;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
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
	//TODO Replace with actual api
	@GET("/table1/{customer_id}")
	LiveData<ApiResponse<Customer>> getCustomer(@Path("customer_id") int customerId);

	@GET("/users/{customer}")
	LiveData<ApiResponse<List<Customer>>> getCustomers();

	@GET("/users/{loan}")
	LiveData<ApiResponse<Loan>> getLoan(@Path("loan") int loanId);

	@GET("/users/")
	LiveData<ApiResponse<List<Loan>>> getLoans();

	//done
	@GET("/table1/{customer_id}")
	LiveData<ApiResponse<List<Loan>>> getLoans(@Path("customer_id") int customerId);

	@GET("/users/")
	LiveData<ApiResponse<List<Expense>>> getExpenses();

	@GET("/users/")
	LiveData<ApiResponse<Expense>> getExpense(@Body int expenseNo);

	@POST("mobile/transaction/")
	LiveData<ApiResponse<List<TransactionModel>>> getTransactionsForLoan(
			@Field("loan_id") int loanAcNo);

	@GET("/users/")
	LiveData<ApiResponse<List<TransactionModel>>> getTransactions();

	LiveData<ApiResponse<TransactionModel>> getTransaction(@Body int transactionNo);

	@GET("/users/")
	LiveData<ApiResponse<Installment>> getInstallment(@Body int installmentNo);

	@GET("/users/")
	LiveData<ApiResponse<List<Installment>>> getInstallments();

	//Done
	@GET("/installid/{loan_id}")
	LiveData<ApiResponse<List<Installment>>> getInstallmentsForLoan(@Path("loan_id") int loanAcNo);
}
