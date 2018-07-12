package com.scleroid.financematic.data.local;

/**
 * Copyright (C) 2018
 *
 * @since 4/4/18
 */

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Main entry point for accessing Items data.
 * <p>
 * For simplicity, only getItems() and getItem() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations. For example, when
 * a new Item is created, it's synchronously stored in cache but usually every operation on database
 * or network should be executed in a different thread.
 */
public interface LocalDataSource<T> {

	/**
	 * gets a list of all items
	 */
	LiveData<List<T>> getItems();

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */
	LiveData<T> getItem(int itemId);


	/**
	 * Saves item to data source
	 *
	 * @param item item object to be saved
	 */
	Single<T> saveItem(@NonNull T item);


	/**
	 * adds a list of objects to the data source
	 *
	 * @param items list of items
	 */
	Completable addItems(@NonNull List<T> items);


	/**
	 * adds items from network to local database
	 *
	 * @param items list of generic items
	 */
	void addNetworkItems(@NonNull List<T> items);

	/**
	 * adds a single item from network when called
	 * to local database
	 *
	 * @param item generic object
	 */
	void addNetworkItem(@NonNull T item);


	/**
	 * Deletes all the data source
	 */
	Completable deleteAllItems();

	/**
	 * deletes a single item from the database
	 *
	 * @param item item to be deleted
	 */
	Single<T> deleteItem(@NonNull T item);

	/**
	 * Updates a particular item
	 *
	 * @param t the item to be updated
	 * @return the updated item wrapped in Rx
	 * @see Single object
	 */
	@Nullable
	Single<T> updateItem(T t);


}
