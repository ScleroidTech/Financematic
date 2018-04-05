package com.scleroid.financematic.data.local;

import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.data.DataSource;
import com.scleroid.financematic.data.local.model.Loan;

import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public class LocalLoanData implements DataSource<Loan> {
    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;

    @Inject
    private LocalLoanData(final AppDatabase appDatabase, final AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
    }

    /**
     * gets a list of all items
     *
     * @param callback callback upon receiving the data
     */
    @Override
    public void getItems(@NonNull final LoadCallback callback) {

    }

    /**
     * gets a single item provided by id
     *
     * @param ItemId   the id of the item to be get
     * @param callback callback receiving upon data
     */
    @Override
    public void getItem(final int ItemId, @NonNull final GetCallback callback) {

    }

    /**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    @Override
    public void saveItem(@NonNull final Loan item) {

    }

    /**
     * adds a new item to the data source
     *
     * @param item object to be added in the database
     */
    @Override
    public void addItem(@NonNull final Loan item) {

    }

    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    @Override
    public void addItems(@NonNull final List<Loan> items) {

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
    public void deleteAllItems() {

    }

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    @Override
    public void deleteItem(final int itemId) {

    }

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    @Override
    public void deleteItem(@NonNull final Loan item) {

    }
}
