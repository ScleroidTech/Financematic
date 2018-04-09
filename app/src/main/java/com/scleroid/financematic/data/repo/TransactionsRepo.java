package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.Resource;
import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.dao.TransactionDao;
import com.scleroid.financematic.data.local.model.TransactionModel;
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
 * <p>
 * Concrete implementation to load Transactions from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 * <p/>
 * By marking the constructor with {@code @Inject} and the class with {@code @Singleton}, Dagger
 * injects the dependencies required to create an instance of the TransactionsRespository (if it
 * fails, it emits a compiler error). It uses {@link com.scleroid.financematic.di.RepositoryModule}
 * to do so, and the constructed instance is available in {@link com.scleroid.financematic.di.AppComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and therefore,
 * to ensure the developer doesn't instantiate the class manually and bypasses Dagger, it's good
 * practice minimise the visibility of the class/constructor as much as possible.
 */

public class TransactionsRepo {

    private final AppDatabase db;

    private final TransactionDao transactionDao;

    private final WebService webService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> transactionListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    TransactionsRepo(final AppDatabase db, final TransactionDao transactionDao, final WebService webService, final AppExecutors appExecutors) {
        this.db = db;
        this.transactionDao = transactionDao;
        this.webService = webService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<TransactionModel>>> loadTransactionsForLoan(int loanAcNo) {
        return new NetworkBoundResource<List<TransactionModel>, List<TransactionModel>>(appExecutors) {
            @Override
            protected void onFetchFailed() {
                transactionListRateLimit.reset(loanAcNo + "");
            }

            @Override
            protected void saveCallResult(@NonNull List<TransactionModel> item) {
                transactionDao.saveTransactions(item);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<TransactionModel>>> createCall() {
                return webService.getTransactionsForLoan(loanAcNo);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<TransactionModel> data) {
                return data == null || data.isEmpty() || transactionListRateLimit.shouldFetch(loanAcNo + "");
            }

            @NonNull
            @Override
            protected LiveData<List<TransactionModel>> loadFromDb() {
                return transactionDao.getTransactionsForLoanLive(loanAcNo);
            }


        }.asLiveData();
    }

    public LiveData<Resource<List<TransactionModel>>> loadTransactions() {
        return new NetworkBoundResource<List<TransactionModel>, List<TransactionModel>>(appExecutors) {
            String key = Math.random() + "";

            @Override
            protected void saveCallResult(@NonNull List<TransactionModel> item) {
                transactionDao.saveTransactions(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<TransactionModel> data) {
                return data == null || data.isEmpty() || transactionListRateLimit.shouldFetch(key + "");
            }

            @NonNull
            @Override
            protected LiveData<List<TransactionModel>> loadFromDb() {
                return transactionDao.getAllTransactionsLive();
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


    public LiveData<Resource<TransactionModel>> loadTransaction(int transactionNo) {
        return new NetworkBoundResource<TransactionModel, TransactionModel>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull TransactionModel item) {
                transactionDao.saveTransaction(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable TransactionModel data) {
                return data == null;//TODO Why this ?
            }

            @NonNull
            @Override
            protected LiveData<TransactionModel> loadFromDb() {
                return transactionDao.getTransaction(transactionNo);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<TransactionModel>> createCall() {
                return webService.getTransaction(transactionNo);
            }
        }.asLiveData();
    }
}





