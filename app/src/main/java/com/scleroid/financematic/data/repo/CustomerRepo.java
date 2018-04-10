package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.Resource;
import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.utils.NetworkBoundResource;
import com.scleroid.financematic.utils.RateLimiter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class CustomerRepo implements Repo<Customer> {


    private final AppDatabase db;

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    private final CustomerDao customerDao;

    private final WebService webService;

    private final AppExecutors appExecutors;
    private RateLimiter<String> customerListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    CustomerRepo(final AppDatabase db, final CustomerDao customerDao, final WebService webService, final AppExecutors appExecutors) {
        this.db = db;
        this.customerDao = customerDao;
        this.webService = webService;
        this.appExecutors = appExecutors;
    }


	@Override
	public LiveData<Resource<List<Customer>>> loadItems() {
		return new NetworkBoundResource<List<Customer>, List<Customer>>(appExecutors) {
			String key = Math.random() + "";

			@Override
			protected void onFetchFailed() {
				customerListRateLimit.reset(key);
			}

			@Override
			protected void saveCallResult(@NonNull List<Customer> item) {
				customerDao.saveCustomers(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<Customer> data) {

				return data == null || data.isEmpty() || customerListRateLimit.shouldFetch(key);
			}

			@NonNull
			@Override
			protected LiveData<List<Customer>> loadFromDb() {
				return customerDao.getAllCustomerLive();
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<List<Customer>>> createCall() {
				return webService.getCustomers();
			}


		}.asLiveData();
	}

	@Override
	public LiveData<Resource<Customer>> loadItem(final int customerNo) {
		return new NetworkBoundResource<Customer, Customer>(appExecutors) {
			@Override
			protected void saveCallResult(@NonNull Customer item) {
				customerDao.saveCustomer(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable Customer data) {
				return data == null;//TODO Why this ?
			}

			@NonNull
			@Override
			protected LiveData<Customer> loadFromDb() {
				return customerDao.getCustomerLive(customerNo);
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<Customer>> createCall() {
				return webService.getCustomer(customerNo);
			}
		}.asLiveData();
	}

	@Override
	public void saveItems(final List<Customer> items) {
		//TODO save this onRemote Source later
		Observable.fromCallable(() -> customerDao.saveCustomers(items));

	}

	@Override
	public void saveItem(final Customer customer) {
		Observable.fromCallable(() -> customerDao.saveCustomer(customer));
	}
}
