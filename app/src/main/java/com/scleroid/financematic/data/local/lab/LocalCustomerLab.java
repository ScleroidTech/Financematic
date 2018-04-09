package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.model.Customer;

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
public class LocalCustomerLab implements LocalDataSource<Customer> {
    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;
    private final CustomerDao customerDao;

    @Inject
    LocalCustomerLab(final AppDatabase appDatabase, final AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
        this.customerDao = appDatabase.customerDao();
    }

    /**
     * gets a list of all items
     */
    @Override
    public LiveData<List<Customer>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Customer>> customers= customerDao.getAllCustomerLive();
            appExecutors.mainThread().execute(() -> {
                if (customers.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(customers);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all customers");
        return customerDao.getAllCustomerLive();
    }

    /**
     * gets a single item provided by id
     *
     * @param itemId the id of the item to be get
     */
    @Override
    public LiveData<Customer> getItem(final int itemId) {
        Timber.d("getting customer with id %d", itemId);
        return customerDao.getCustomerLive(itemId);
    }

    /**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    @Override
    public Single<Customer> saveItem(@NonNull final Customer item) {
        Timber.d("creating new customer ");

        return Single.fromCallable(() -> {
            long rowId = customerDao.saveCustomer(item);
            Timber.d("customer stored " + rowId);
            return item;
        });
    }


    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    @Override
    public Completable addItems(@NonNull final List<Customer> items) {
        Timber.d("creating new customer ");

        return Completable.fromAction(() -> {
            long[] rowId = customerDao.saveCustomers(items);
            Timber.d("customer stored " + rowId.length);
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
        Timber.d("Deleting all customers");
        return Completable.fromAction(() -> customerDao.nukeTable());

    }

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    @Override
    public Completable deleteItem(final int itemId) {
        Timber.d("deleting customer with id %d", itemId);

        return Completable.fromAction(() -> customerDao.delete(customerDao.getCustomerLive(itemId).getValue()));
    }

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    @Override
    public Completable deleteItem(@NonNull final Customer item) {
        Timber.d("deleting customer with id %d", item.getCustomerId());

        return Completable.fromAction(() -> customerDao.delete(item));
    }
}
