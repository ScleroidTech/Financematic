
package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.Resource;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.utils.NetworkBoundResource;
import com.scleroid.financematic.utils.RateLimiter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
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
public class LoanRepo {

    private final AppDatabase db;

    private final LoanDao loanDao;

    private final WebService webService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> loanListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    private LoanRepo(final AppDatabase db, final LoanDao loanDao, final WebService webService, final AppExecutors appExecutors) {
        this.db = db;
        this.loanDao = loanDao;
        this.webService = webService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Loan>>> loadLoansForCustomer(int customerId) {
        return new NetworkBoundResource<List<Loan>, List<Loan>>(appExecutors) {
            @Override
            protected void onFetchFailed() {
                loanListRateLimit.reset(customerId + "");
            }

            @Override
            protected void saveCallResult(@NonNull List<Loan> item) {
                loanDao.saveLoans(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Loan> data) {
                return data == null || data.isEmpty() || loanListRateLimit.shouldFetch(customerId + "");
            }

            @NonNull
            @Override
            protected LiveData<List<Loan>> loadFromDb() {
                return loanDao.getLoansForCustomerLive(customerId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Loan>>> createCall() {
                return webService.getLoans(customerId);
            }


        }.asLiveData();
    }

    public LiveData<Resource<List<Loan>>> loadLoans() {
        return new NetworkBoundResource<List<Loan>, List<Loan>>(appExecutors) {
            String key = Math.random() + "";

            @Override
            protected void saveCallResult(@NonNull List<Loan> item) {
                loanDao.saveLoans(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Loan> data) {
                return data == null || data.isEmpty() || loanListRateLimit.shouldFetch(key + "");
            }

            @NonNull
            @Override
            protected LiveData<List<Loan>> loadFromDb() {
                return loanDao.getLoansLive();
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


    public LiveData<Resource<Loan>> loadLoan(int acNo) {
        return new NetworkBoundResource<Loan, Loan>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Loan item) {
                loanDao.saveLoan(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Loan data) {
                return data == null;//TODO Why this ?
            }

            @NonNull
            @Override
            protected LiveData<Loan> loadFromDb() {
                return loanDao.getLoan(acNo);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Loan>> createCall() {
                return webService.getLoan(acNo);
            }
        }.asLiveData();
    }


}