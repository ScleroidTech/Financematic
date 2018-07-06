package com.scleroid.financematic.data.remote;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;

import hugo.weaving.DebugLog;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RemotePostEndpoint {
	@NonNull
	@DebugLog
	@POST("/mobile/newregister")
	Call<Customer> addCustomer(@Body final String firebaseUser,
	                           @Body Customer customer);

	@NonNull
	@DebugLog
	@POST("/mobile/newloanusers")
	Call<Loan> addLoan(@Body final String firebaseUser, @Body Loan loan);

	@NonNull
	@DebugLog
	@POST("/mobile/posts")
	Call<TransactionModel> addTransaction(@Body final String firebaseUser, @Body TransactionModel transaction);

	@NonNull
	@DebugLog
	@POST("/mobile/insertmydate")
	Call<Installment> addInstallment(@Body final String firebaseUser, @Body Installment installment);

	@NonNull
	@DebugLog
	@POST("/mobile/expand")
	Call<Expense> addExpense(@Body final String firebaseUser, @Body Expense expense);

	@NonNull
	@DebugLog
	@POST("/posts")
	Call<Customer> addCustomer(@Body List<Customer> customer);

	@NonNull
	@DebugLog
	@POST("/posts")
	Call<Loan> addLoan(@Body List<Loan> loan);

	@NonNull
	@DebugLog
	@POST("/posts")
	Call<TransactionModel> addTransaction(@Body List<TransactionModel> transaction);

	@NonNull
	@DebugLog
	@POST("/posts")
	Call<Installment> addInstallment(@Body List<Installment> installment);

	@NonNull
	@DebugLog
	@POST("/posts")
	Call<Expense> addExpense(@Body List<Expense> expense);


	@DebugLog
	//Done
	@FormUrlEncoded
	@POST("delete/{installment_id}")
	void deleteInstallment(@Body int installment);

	@DebugLog
	@FormUrlEncoded
	void deleteCustomer(int customerId);

	@DebugLog
	@FormUrlEncoded
	void deleteExpense(int expenseId);

	@DebugLog
	@FormUrlEncoded
	void deleteLoan(int accountNo);

	@DebugLog
	@FormUrlEncoded
	void deleteTransaction(int transactionId);
}
