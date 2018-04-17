package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.lab.LocalExpenseLab;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.utils.AppExecutors;
import com.scleroid.financematic.utils.NetworkBoundResource;
import com.scleroid.financematic.utils.RateLimiter;
import com.scleroid.financematic.utils.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

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


	private final AppDatabase db;

	public LocalExpenseLab getLocalExpenseLab() {
		return localExpenseLab;
	}

	private final LocalExpenseLab localExpenseLab;

	private final WebService webService;

	private final AppExecutors appExecutors;

	private RateLimiter<String> expenseListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

	@Inject
	ExpenseRepo(final AppDatabase db, final LocalExpenseLab localExpenseLab,
	            final WebService webService, final AppExecutors appExecutors) {
		this.db = db;
		this.localExpenseLab = localExpenseLab;
		this.webService = webService;
		this.appExecutors = appExecutors;
	}


	@Override
	public LiveData<Resource<List<Expense>>> loadItems() {
		return new NetworkBoundResource<List<Expense>, List<Expense>>(appExecutors) {
			String key = Math.random() + "";

			@Override
			protected void onFetchFailed() {
				expenseListRateLimit.reset(key + "");
			}

			@Override
			protected void saveCallResult(@NonNull List<Expense> items) {
				localExpenseLab.addItems(items);
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
				localExpenseLab.saveItem(item);
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
	public Completable saveItems(final List<Expense> items) {
		return localExpenseLab.addItems(items);
	}

	@Override
	public Single<Expense> saveItem(final Expense expense) {
		return localExpenseLab.saveItem(expense);
	}

	@Override
	public Single<Installment> updateItem(final Expense expense) {
		return null;
	}


}



