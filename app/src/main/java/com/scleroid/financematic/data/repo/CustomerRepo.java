package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.remote.lab.RemoteCustomerLab;
import com.scleroid.financematic.data.remote.services.networking.RemotePostEndpoint;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.network.NetworkBoundResource;
import com.scleroid.financematic.utils.network.RateLimiter;
import com.scleroid.financematic.utils.network.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class CustomerRepo implements Repo<Customer> {


	private final LocalCustomerLab localCustomerLab;
	private RemoteCustomerLab remoteCustomerLab;
	//TODO remove direct access to this
	private final WebService webService;
	private RemotePostEndpoint postEndpoint;
	private final AppExecutors appExecutors;
	private RateLimiter<String> customerListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
	@Inject
	public CustomerRepo(final LocalCustomerLab localCustomerLab,
	                    RemoteCustomerLab remoteCustomerLab,
	                    final WebService webService,
	                    final RemotePostEndpoint postEndpoint,
	                    final AppExecutors appExecutors) {
		this.localCustomerLab = localCustomerLab;
		this.remoteCustomerLab = remoteCustomerLab;
		this.webService = webService;
		this.postEndpoint = postEndpoint;
		this.appExecutors = appExecutors;
	}

	public LocalCustomerLab getLocalCustomerLab() {
		return localCustomerLab;
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
			protected void saveCallResult(@NonNull List<Customer> items) {
				localCustomerLab.addItems(items);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<Customer> data) {

				return data == null || data.isEmpty() || customerListRateLimit.shouldFetch(key);
			}

			@NonNull
			@Override
			protected LiveData<List<Customer>> loadFromDb() {
				return localCustomerLab.getItems();
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
				localCustomerLab.saveItem(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable Customer data) {
				return data == null;//TODO Why this ?
			}

			@NonNull
			@Override
			protected LiveData<Customer> loadFromDb() {
				return localCustomerLab.getItem(customerNo);
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<Customer>> createCall() {
				return webService.getCustomer(customerNo);
			}
		}.asLiveData();
	}

	@Override
	public Completable saveItems(final List<Customer> items) {
		//TODO save this onRemote Source later
		//Observable.fromCallable(() -> customerDao.saveCustomers(items));

		return localCustomerLab.addItems(items);


	}

	@Override
	public Completable saveItem(final Customer customer) {
		//Observable.fromCallable(() -> customerDao.saveCustomer(customer));

		return localCustomerLab.saveItem(customer).flatMapCompletable(remoteCustomerLab::sync);

	}

	@Override
	public Completable updateItem(final Customer customer) {
		return localCustomerLab.updateItem(customer).flatMapCompletable(remoteCustomerLab::sync);
	}

	@Override
	public Completable deleteItem(final Customer customer) {

		//TODO update Remote
		return localCustomerLab.deleteItem(customer);
	}
}
