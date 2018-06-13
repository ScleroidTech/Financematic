package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.lab.LocalExpenseLab;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.remote.lab.RemoteExpenseLab;
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
 * Concrete implementation to load Expenses from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p/>
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the ExpensesRespository (if it fails,
 * it emits a compiler error). It uses {@link com.scleroid.financematic.di.RepositoryModule} to do
 * so, and the constructed instance is available in
 * {@link com.scleroid.financematic.di.AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and therefore,
 * to ensure the developer doesn't instantiate the class manually and bypasses Dagger, it's good
 * practice minimise the visibility of the class/constructor as much as possible.
 */

public class ExpenseRepo implements Repo<Expense> {


	private final LocalExpenseLab localExpenseLab;

	private final WebService webService;

	private final AppExecutors appExecutors;
	private final RemoteExpenseLab remoteExpenseLab;

	@NonNull
	private RateLimiter<String> expenseListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

	@Inject
	ExpenseRepo(final LocalExpenseLab localExpenseLab,
	            final WebService webService, final AppExecutors appExecutors,
	            final RemoteExpenseLab remoteExpenseLab) {
		this.localExpenseLab = localExpenseLab;
		this.webService = webService;
		this.appExecutors = appExecutors;
		this.remoteExpenseLab = remoteExpenseLab;
	}


	@Override
	public LiveData<Resource<List<Expense>>> loadItems() {
		return new NetworkBoundResource<List<Expense>, List<Expense>>(appExecutors) {
			@NonNull
			String key = Math.random() + "";

			@Override
			protected void onFetchFailed() {
				expenseListRateLimit.reset(key + "");
			}

			@Override
			protected void saveCallResult(@NonNull List<Expense> items) {
				localExpenseLab.addNetworkItems(items);
			}

			@Override
			protected boolean shouldFetch(@Nullable List<Expense> data) {
				return data == null || data.isEmpty() || expenseListRateLimit.shouldFetch(key +
						"");
			}

			@NonNull
			@Override
			protected LiveData<List<Expense>> loadFromDb() {
				return localExpenseLab.getItems();
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<List<Expense>>> createCall() {
				return webService.getExpenses();
			}


		}.asLiveData();
	}

	@Override
	public LiveData<Resource<Expense>> loadItem(final int expenseNo) {
		return new NetworkBoundResource<Expense, Expense>(appExecutors) {
			@Override
			protected void saveCallResult(@NonNull Expense item) {
				localExpenseLab.addNetworkItem(item);
			}

			@Override
			protected boolean shouldFetch(@Nullable Expense data) {
				return data == null;//TODO Why this ?
			}

			@NonNull
			@Override
			protected LiveData<ApiResponse<Expense>> createCall() {
				return webService.getExpense(expenseNo);
			}

			@NonNull
			@Override
			protected LiveData<Expense> loadFromDb() {
				return localExpenseLab.getItem(expenseNo);
			}
		}.asLiveData();
	}

	@Override
	public Completable saveItems(@NonNull final List<Expense> items) {
		return localExpenseLab.addItems(items);
	}

	@Override
	public Completable saveItem(@NonNull final Expense expense) {
		return localExpenseLab.saveItem(expense).flatMapCompletable(remoteExpenseLab::sync);
	}

	@Override
	public Completable updateItem(final Expense expense) {
		return localExpenseLab.updateItem(expense).flatMapCompletable(remoteExpenseLab::sync);
	}

	@Override
	public Completable deleteItem(@NonNull final Expense expense) {
		//TODO update for server side also
		return localExpenseLab.deleteItem(expense).flatMapCompletable(remoteExpenseLab::delete);
	}


}



