package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.TransactionDao;
import com.scleroid.financematic.data.local.model.TransactionModel;

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
public class LocalTransactionsLab implements LocalDataSource<TransactionModel> {
    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;
    private final TransactionDao transactionDao;

    @Inject
    LocalTransactionsLab(final AppDatabase appDatabase, final AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
        this.transactionDao = appDatabase.transactionDao();
    }


    /**
     * gets a list of all items
     */
    @Override
    public LiveData<List<TransactionModel>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Transaction>> transactions= transactionDao.getAllTransactionLive();
            appExecutors.mainThread().execute(() -> {
                if (transactions.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(transactions);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all transactions");
        return transactionDao.getAllTransactionsLive();
    }

    /**
     * gets a single item provided by id
     *
     * @param itemId the id of the item to be get
     */
    @Override
    public LiveData<TransactionModel> getItem(final int itemId) {
        Timber.d("getting transaction with id %d", itemId);
        return transactionDao.getTransaction(itemId);
    }

    /**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    @Override
    public Single<TransactionModel> saveItem(@NonNull final TransactionModel item) {
        Timber.d("creating new transaction ");

        return Single.fromCallable(() -> {
            long rowId = transactionDao.saveTransaction(item);
            Timber.d("transaction stored " + rowId);
            return item;
        });
    }

    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    @Override
    public Completable addItems(@NonNull final List<TransactionModel> items) {
        Timber.d("creating new transaction ");

        return Completable.fromAction(() -> {
            long[] rowId = transactionDao.saveTransactions(items);
            Timber.d("transaction stored " + rowId.length);
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
        Timber.d("Deleting all transactions");
        return Completable.fromAction(() -> transactionDao.nukeTable());

    }

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    @Override
    public Completable deleteItem(final int itemId) {
        Timber.d("deleting transaction with id %d", itemId);

        return Completable.fromAction(() -> transactionDao.delete(transactionDao.getTransaction(itemId).getValue()));
    }

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    @Override
    public Completable deleteItem(@NonNull final TransactionModel item) {
        Timber.d("deleting transaction with id %d", item.getTransactionId());

        return Completable.fromAction(() -> transactionDao.delete(item));
    }

    /**
     * gets a list of all items for a particular value of customer no
     */

    public LiveData<List<TransactionModel>> getItemsForLoan(int acNo) {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Transaction>> transactions= transactionDao.getAllTransactionLive();
            appExecutors.mainThread().execute(() -> {
                if (transactions.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(transactions);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all transactions");
        return transactionDao.getTransactionsForLoanLive(acNo);
    }
}
