package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.lab.LocalTransactionsLab;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.remote.lab.RemoteTransactionLab;
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
 * <p>
 * <p>
 * <p>
 * Concrete implementation to load Transactions from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p/>
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the TransactionsRepository (if it
 * fails, it emits a compiler error). It uses {@link com.scleroid.financematic.di.RepositoryModule}
 * to do so, and the constructed instance is available in
 * {@link com.scleroid.financematic.di.AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and therefore,
 * to ensure the developer doesn't instantiate the class manually and bypasses Dagger, it's good
 * practice minimise the visibility of the class/constructor as much as possible.
 */

public class TransactionsRepo implements Repo<TransactionModel> {

	private final LocalTransactionsLab localTransactionsLab;
	private final WebService webService;

	private final RemoteTransactionLab remoteTransactionLab;
	private final AppExecutors appExecutors;
	@NonNull
	private RateLimiter<String> transactionListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

	@Inject
	TransactionsRepo(final LocalTransactionsLab transactionsLab,
	                 final WebService webService, final AppExecutors appExecutors,
	                 final RemoteTransactionLab remoteTransactionLab) {
		this.localTransactionsLab = transactionsLab;
		this.webService = webService;
		this.appExecutors = appExecutors;
		this.remoteTransactionLab = remoteTransactionLab;
	}


	public LiveData<Resource<List<TransactionModel>>> loadTransactionsForLoan(int loanAcNo) {
		return new NetworkBoundResource<List<TransactionModel>, List<TransactionModel>>(
				appExecutors) {
			@NonNull
			@Override
			protected LiveData<List<TransactionModel>> loadFromDb() {
				return localTransactionsLab.getItemsForLoan(loanAcNo);
			}

			@Override
			protected void onFetchFailed() {
				transactionListRateLimit.reset(loanAcNo + "");
			}

			@Override
			protected void saveCallResult(@NonNull List<TransactionModel> item) {
				localTransactionsLab.addNetworkItems(item);
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<List<TransactionModel>>> createCall() {
				return webService.getTransactionsForLoan(loanAcNo);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<TransactionModel> data) {
				return data == null || data.isEmpty() || transactionListRateLimit.shouldFetch(
						loanAcNo + "");
			}


		}.asLiveData();
	}


	@Override
	public LiveData<Resource<List<TransactionModel>>> loadItems() {
		return new NetworkBoundResource<List<TransactionModel>, List<TransactionModel>>(
				appExecutors) {
			@NonNull
			String key = Math.random() + "";

			@Override
			protected void saveCallResult(@NonNull List<TransactionModel> item) {
				localTransactionsLab.addNetworkItems(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<TransactionModel> data) {
				return data == null || data.isEmpty() || transactionListRateLimit.shouldFetch(
						key + "");
			}

			@NonNull
			@Override
			protected LiveData<List<TransactionModel>> loadFromDb() {
				return localTransactionsLab.getItems();
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<List<TransactionModel>>> createCall() {

				return webService.getTransactions();
			}

			@Override
			protected void onFetchFailed() {
				transactionListRateLimit.reset(key + "");
			}
		}.asLiveData();
	}

	@Override
	public LiveData<Resource<TransactionModel>> loadItem(final int transactionNo) {
		return new NetworkBoundResource<TransactionModel, TransactionModel>(appExecutors) {
			@Override
			protected void saveCallResult(@NonNull TransactionModel item) {
				localTransactionsLab.addNetworkItem(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable TransactionModel data) {
				return data == null;
			}

			@NonNull
			@Override
			protected LiveData<TransactionModel> loadFromDb() {
				return localTransactionsLab.getItem(transactionNo);
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<TransactionModel>> createCall() {
				return webService.getTransaction(transactionNo);
			}
		}.asLiveData();
	}

	@Override
	public Completable saveItems(@NonNull final List<TransactionModel> items) {
		return localTransactionsLab.addItems(items);
	}

	@Override
	public Completable saveItem(@NonNull final TransactionModel transactionModel) {
		return localTransactionsLab.saveItem(transactionModel)
				.flatMapCompletable(remoteTransactionLab::sync);
	}

	@Override
	public Completable updateItem(final TransactionModel transactionModel) {
		return localTransactionsLab.updateItem(transactionModel)
				.flatMapCompletable(remoteTransactionLab::sync);
	}

	@Override
	public Completable deleteItem(@NonNull final TransactionModel transactionModel) {
		//TODO update to server
		return localTransactionsLab.deleteItem(transactionModel)
				.flatMapCompletable(remoteTransactionLab::delete);
	}


}





