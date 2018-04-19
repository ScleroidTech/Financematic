package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.network.NetworkBoundResource;
import com.scleroid.financematic.utils.network.RateLimiter;
import com.scleroid.financematic.utils.network.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;


/**
 * Copyright (C) 2018
 *
 * @since 4/4/18
 */

/**
 * Concrete implementation to load Loans from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p/>
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the LoansRespository (if it fails, it
 * emits a compiler error). It uses {@link com.scleroid.financematic.di.RepositoryModule} to do so,
 * and the constructed instance is available in {@link com.scleroid.financematic.di.AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and therefore,
 * to ensure the developer doesn't instantiate the class manually and bypasses Dagger, it's good
 * practice minimise the visibility of the class/constructor as much as possible.
 */

@Singleton
public class LoanRepo implements Repo<Loan> {


	private final LocalLoanLab localLoanLab;
	private final WebService webService;
	private final AppExecutors appExecutors;
	private RateLimiter<String> loanListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

	@Inject
	LoanRepo(final LocalLoanLab loanLab, final WebService webService,
	         final AppExecutors appExecutors) {

		this.localLoanLab = loanLab;
		this.webService = webService;
		this.appExecutors = appExecutors;
	}

	public LocalLoanLab getLocalLoanLab() {
		return localLoanLab;
	}

	public LiveData<Resource<List<Loan>>> loadLoansForCustomer(int customerId) {
		return new NetworkBoundResource<List<Loan>, List<Loan>>(appExecutors) {
			@Override
			protected void onFetchFailed() {
				loanListRateLimit.reset(customerId + "");
			}

			@Override
			protected void saveCallResult(@NonNull List<Loan> item) {
				localLoanLab.addItems(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<Loan> data) {
				return data == null || data.isEmpty() || loanListRateLimit.shouldFetch(
						customerId + "");
			}

			@NonNull
			@Override
			protected LiveData<List<Loan>> loadFromDb() {
				return localLoanLab.getItemWithCustomerId(customerId);
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<List<Loan>>> createCall() {
				return webService.getLoans(customerId);
			}


		}.asLiveData();
	}

	@Override
	public LiveData<Resource<List<Loan>>> loadItems() {
		return new NetworkBoundResource<List<Loan>, List<Loan>>(appExecutors) {
			String key = Math.random() + "";

			@Override
			protected void saveCallResult(@NonNull List<Loan> item) {
				localLoanLab.addItems(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<Loan> data) {
				return data == null || data.isEmpty() || loanListRateLimit.shouldFetch(key + "");
			}

			@NonNull
			@Override
			protected LiveData<List<Loan>> loadFromDb() {
				return localLoanLab.getItems();
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<List<Loan>>> createCall() {
				return webService.getLoans();
			}

			@Override
			protected void onFetchFailed() {
				loanListRateLimit.reset(key + "");
			}
		}.asLiveData();
	}

	@Override
	public LiveData<Resource<Loan>> loadItem(final int acNo) {
		return new NetworkBoundResource<Loan, Loan>(appExecutors) {
			@Override
			protected void saveCallResult(@NonNull Loan item) {
				localLoanLab.saveItem(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable Loan data) {
				return data == null;//TODO Why this ?
			}

			@NonNull
			@Override
			protected LiveData<Loan> loadFromDb() {
				return localLoanLab.getItem(acNo);
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<Loan>> createCall() {
				return webService.getLoan(acNo);
			}
		}.asLiveData();
	}

	@Override
	public Completable saveItems(final List<Loan> items) {
		return localLoanLab.addItems(items);
	}

	@Override
	public Single<Loan> saveItem(final Loan loan) {
		return localLoanLab.saveItem(loan);
	}

	@Override
	public Single<Loan> updateItem(final Loan loan) {
		return null;
	}

	@Override
	public Completable deleteItem(final Loan loan) {
		return null;
	}


}