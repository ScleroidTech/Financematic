package com.scleroid.financematic.data.remote.services.networking;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemotePostEndpoint {

	@POST("/posts")
	Call<Customer> addCustomer(@Body Customer customer);

	@POST("/posts")
	Call<Loan> addLoan(@Body Loan loan);

	@POST("/posts")
	Call<TransactionModel> addTransaction(@Body TransactionModel transaction);

	@POST("/posts")
	Call<Installment> addInstallment(@Body Installment installment);

	@POST("/posts")
	Call<Expense> addExpense(@Body Expense expense);

}
