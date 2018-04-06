package com.scleroid.financematic.data.remote;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;

import java.util.List;

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
    LiveData<ApiResponse<Customer>> getCustomer(@Path("customer") int customerId);

    @GET("/users/{customer}")
    LiveData<ApiResponse<List<Customer>>> getCustomers();

    @GET("/users/{loan}")
    LiveData<ApiResponse<Loan>> getLoan(@Path("loan") int loanId);

    @GET("/users/")
    LiveData<ApiResponse<List<Loan>>> getLoans();

    @GET("/users/")
    LiveData<ApiResponse<List<Loan>>> getLoans(int customerId);

}
