package com.scleroid.financematic.data.remote;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public interface WebService {
    //TODO Replace with actual api
    @GET("/users/{customer}")
    Call<Customer> getCustomer(@Path("customer") String customerId);

    @GET("/users/{loan}")
    Call<Loan> getLoan(@Path("loan") String loanId);

    @GET("/users/")
    Call<Loan> getLoans();

}
