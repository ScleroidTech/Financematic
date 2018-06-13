package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.remote.lab.RemoteCustomerLab;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.network.NetworkBoundResource;
import com.scleroid.financematic.utils.network.RateLimiter;
import com.scleroid.financematic.utils.network.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class CustomerRepo implements Repo<Customer> {


	private final LocalCustomerLab localCustomerLab;
	private RemoteCustomerLab remoteCustomerLab;

	private final LoanRepo loanRepo;
	//TODO remove direct access to this
	private final WebService webService;
	private RemotePostEndpoint postEndpoint;
	private final AppExecutors appExecutors;
	@NonNull
	private RateLimiter<String> customerListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
	@Inject
	public CustomerRepo(final LocalCustomerLab localCustomerLab,
	                    RemoteCustomerLab remoteCustomerLab,
	                    final LoanRepo loanRepo,
	                    final WebService webService,
	                    final RemotePostEndpoint postEndpoint,
	                    final AppExecutors appExecutors) {
		this.localCustomerLab = localCustomerLab;
		this.remoteCustomerLab = remoteCustomerLab;
		this.loanRepo = loanRepo;
		this.webService = webService;
		this.postEndpoint = postEndpoint;
		this.appExecutors = appExecutors;
	}


	@Override
	public LiveData<Resource<List<Customer>>> loadItems() {
		return new NetworkBoundResource<List<Customer>, List<Customer>>(appExecutors) {
			@NonNull
			String key = Math.random() + "";

			@Override
			protected void onFetchFailed() {
				customerListRateLimit.reset(key);
			}

			@Override
			protected void saveCallResult(@NonNull List<Customer> items) {
				Timber.d("Save call result Is the called date is being stored? ");
				localCustomerLab.addNetworkItems(items);
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

	public LiveData<Resource<List<Customer>>> getCustomersWithLoans() {
		LiveData<Resource<List<Customer>>> customerLiveData = loadItems();

		// TODO Test this, if works remove below code, this part has performance issues
		customerLiveData = Transformations.switchMap(customerLiveData, inputCustomers -> {
			MediatorLiveData<Resource<List<Customer>>> customerMediatorLiveData =
					new MediatorLiveData<>();


			if (inputCustomers.data != null) {
				for (Customer customer : inputCustomers.data) {

					customerMediatorLiveData.addSource(
							loanRepo.loadLoansForCustomer(customer.getCustomerId()), loan -> {

								customer.setLoans(loan != null ? loan.data : null);
								customerMediatorLiveData.postValue(inputCustomers);

							});
				}
			}
			return customerMediatorLiveData;
		});
		return customerLiveData;
       /* customerLiveData = Transformations.map(customerLiveData, inputStates -> {
            for (Customer state : inputStates) {
                state.setLoans(loanDao.getLoans(state.getCustomerId()));
            }
            return inputStates;
        });
        return customerLiveData;*/
	}

	@Override
	public LiveData<Resource<Customer>> loadItem(final int customerNo) {
		return new NetworkBoundResource<Customer, Customer>(appExecutors) {
			@Override
			protected void saveCallResult(@NonNull Customer item) {
				localCustomerLab.addNetworkItem(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable Customer data) {
				return data == null;
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
	public Completable saveItems(@NonNull final List<Customer> items) {
		//TODO save this onRemote Source later
		//Observable.fromCallable(() -> customerDao.saveCustomers(items));

		return localCustomerLab.addItems(items);


	}

	@Override
	public Completable saveItem(@NonNull final Customer customer) {
		//Observable.fromCallable(() -> customerDao.saveCustomer(customer));

		return localCustomerLab.saveItem(customer).flatMapCompletable(remoteCustomerLab::sync);

	}

	@Override
	public Completable updateItem(final Customer customer) {
		return localCustomerLab.updateItem(customer).flatMapCompletable(remoteCustomerLab::sync);
	}

	@Override
	public Completable deleteItem(@NonNull final Customer customer) {

		//TODO update Remote
		return localCustomerLab.deleteItem(customer).flatMapCompletable(remoteCustomerLab::delete);
	}
}
