package com.scleroid.financematic.data.local.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.model.Loan;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public class LocalLoanRepo implements LocalDataSource<Loan> {
    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;
    private final LoanDao loanDao;

    @Inject
    private LocalLoanRepo(final AppDatabase appDatabase, final AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
        this.loanDao = appDatabase.loanDao();
    }


    /**
     * gets a list of all items
     */
    @Override
    public LiveData<List<Loan>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Loan>> loans= loanDao.getAllLoanLive();
            appExecutors.mainThread().execute(() -> {
                if (loans.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(loans);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all loans");
        return loanDao.getLoansLive();
    }

    /**
     * gets a single item provided by id
     *
     * @param itemId the id of the item to be get
     */
    @Override
    public LiveData<Loan> getItem(final int itemId) {
        Timber.d("getting loan with id %d", itemId);
        return loanDao.getLoan(itemId);
    }

    /**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    @Override
    public Single<Loan> saveItem(@NonNull final Loan item) {
        Timber.d("creating new loan ");

        return Single.fromCallable(() -> {
            long rowId = loanDao.saveLoan(item);
            Timber.d("loan stored " + rowId);
            return item;
        });
    }

    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    @Override
    public Completable addItems(@NonNull final List<Loan> items) {
        Timber.d("creating new loan ");

        return Completable.fromAction(() -> {
            long rowId = loanDao.saveLoans(items);
            Timber.d("loan stored " + rowId);
        });
    }

    /**
     * refreshes the data source
     */
    @Override
    public void refreshItems() {

    }

    /**
     * Deletes all the data source
     */
    @Override
    public Completable deleteAllItems() {
        Timber.d("Deleting all loans");
        return Completable.fromAction(() -> loanDao.nukeTable());

    }

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    @Override
    public Completable deleteItem(final int itemId) {
        Timber.d("deleting loan with id %d", itemId);

        return Completable.fromAction(() -> loanDao.delete(loanDao.getLoan(itemId).getValue()));
    }

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    @Override
    public Completable deleteItem(@NonNull final Loan item) {
        Timber.d("deleting loan with id %d", item.getAccountNo());

        return Completable.fromAction(() -> loanDao.delete(item));
    }

    /**
     * gets a list of all items for a particular value of customer no
     */

    public LiveData<List<Loan>> getItemsForCustomer(int custNo) {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Loan>> loans= loanDao.getAllLoanLive();
            appExecutors.mainThread().execute(() -> {
                if (loans.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(loans);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all loans");
        return loanDao.getLoansForCustomerLive(custNo);
    }
}
