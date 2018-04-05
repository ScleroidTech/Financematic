package com.scleroid.financematic.data;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/4/18
 */

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Main entry point for accessing Items data.
 * <p>
 * For simplicity, only getItems() and getItem() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations. For example, when
 * a new Item is created, it's synchronously stored in cache but usually every operation on database
 * or network should be executed in a different thread.
 */
public interface DataSource<T> {

    /**
     * gets a list of all items
     *
     * @param callback callback upon receiving the data
     */
    void getItems(@NonNull LoadCallback callback);

    /**
     * gets a single item provided by id
     *
     * @param ItemId   the id of the item to be get
     * @param callback callback receiving upon data
     */
    void getItem(int ItemId, @NonNull GetCallback callback);

    /**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    void saveItem(@NonNull T item);

    /**
     * adds a new item to the data source
     *
     * @param item object to be added in the database
     */
    void addItem(@NonNull T item);

    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    void addItems(@NonNull List<T> items);

    /**
     * refreshes the data source
     */
    void refreshItems();

    /**
     * Deletes all the data source
     */
    void deleteAllItems();

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    void deleteItem(int itemId);

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    void deleteItem(@NonNull T item);

    /**
     * Callback for getItems
     *
     * @param <T>
     */
    interface LoadCallback<T> {

        void onLoaded(List<T> items);

        void onDataNotAvailable();
    }


    /**
     * CallBack for getItem
     *
     * @param <T>
     */
    interface GetCallback<T> {

        default void onLoaded(T item) {

        }

        void onDataNotAvailable();
    }
}
