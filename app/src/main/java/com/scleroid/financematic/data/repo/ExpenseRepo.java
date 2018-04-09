package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.Resource;
import com.scleroid.financematic.data.local.dao.ExpenseDao;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.utils.NetworkBoundResource;
import com.scleroid.financematic.utils.RateLimiter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
 * so, and the constructed instance is available in {@link com.scleroid.financematic.di.AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and therefore,
 * to ensure the developer doesn't instantiate the class manually and bypasses Dagger, it's good
 * practice minimise the visibility of the class/constructor as much as possible.
 */

public class ExpenseRepo {


    private final AppDatabase db;

    private final ExpenseDao expenseDao;

    private final WebService webService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> expenseListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    private ExpenseRepo(final AppDatabase db, final ExpenseDao expenseDao, final WebService webService, final AppExecutors appExecutors) {
        this.db = db;
        this.expenseDao = expenseDao;
        this.webService = webService;
        this.appExecutors = appExecutors;
    }


    public LiveData<Resource<List<Expense>>> loadExpenses() {
        return new NetworkBoundResource<List<Expense>, List<Expense>>(appExecutors) {
            String key = Math.random() + "";

            @Override
            protected void onFetchFailed() {
                expenseListRateLimit.reset(key + "");
            }

            @Override
            protected void saveCallResult(@NonNull List<Expense> item) {
                expenseDao.saveExpenses(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Expense> data) {
                return data == null || data.isEmpty() || expenseListRateLimit.shouldFetch(key + "");
            }

            @NonNull
            @Override
            protected LiveData<List<Expense>> loadFromDb() {
                return expenseDao.getAllExpenseLive();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Expense>>> createCall() {
                return webService.getExpenses();
            }


        }.asLiveData();
    }


    public LiveData<Resource<Expense>> loadExpense(int expenseNo) {
        return new NetworkBoundResource<Expense, Expense>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Expense item) {
                expenseDao.saveExpense(item);
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
                return expenseDao.getExpense(expenseNo);
            }
        }.asLiveData();
    }
}



