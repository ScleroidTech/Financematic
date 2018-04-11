package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.model.Installment;

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
public class LocalInstallmentsLab implements LocalDataSource<Installment> {
    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;
    private final InstallmentDao installmentDao;

    @Inject
    LocalInstallmentsLab(final AppDatabase appDatabase, final AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
        this.installmentDao = appDatabase.installmentDao();
    }


    /**
     * gets a list of all items
     */
    @Override
    public LiveData<List<Installment>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Installment>> installments= installmentDao.getAllInstallmentLive();
            appExecutors.mainThread().execute(() -> {
                if (installments.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(installments);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all installments");
        return installmentDao.getAllInstallmentsLive();
    }

    /**
     * gets a single item provided by id
     *
     * @param itemId the id of the item to be get
     */
    @Override
    public LiveData<Installment> getItem(final int itemId) {
        Timber.d("getting installment with id %d", itemId);
        return installmentDao.getInstallment(itemId);
    }

    /**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    @Override
    public Single<Installment> saveItem(@NonNull final Installment item) {
        Timber.d("creating new installment ");

        return Single.fromCallable(() -> {
            long rowId = installmentDao.saveInstallment(item);
            Timber.d("installment stored " + rowId);
            return item;
        });
    }

    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    @Override
    public Completable addItems(@NonNull final List<Installment> items) {
        Timber.d("creating new installment ");

        return Completable.fromAction(() -> {
            long[] rowId = installmentDao.saveInstallments(items);
            Timber.d("installment stored " + rowId.length);
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
        Timber.d("Deleting all installments");
        return Completable.fromAction(() -> installmentDao.nukeTable());

    }

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    @Override
    public Completable deleteItem(final int itemId) {
        Timber.d("deleting installment with id %d", itemId);

        return Completable.fromAction(() -> installmentDao.delete(installmentDao.getInstallment(itemId).getValue()));
    }

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    @Override
    public Completable deleteItem(@NonNull final Installment item) {
        Timber.d("deleting installment with id %d", item.getInstallmentId());

        return Completable.fromAction(() -> installmentDao.delete(item));
    }

    /**
     * gets a list of all items for a particular value of customer no
     */

    public LiveData<List<Installment>> getItemsForLoan(int acNo) {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Installment>> installments= installmentDao.getAllInstallmentLive();
            appExecutors.mainThread().execute(() -> {
                if (installments.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(installments);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all installments");
        return installmentDao.getInstallmentsForLoanLive(acNo);
    }
}
