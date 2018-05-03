package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.utils.network.Resource;

import java.util.List;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/10/18
 */
public interface Repo<T> {
	LiveData<Resource<List<T>>> loadItems();

	LiveData<Resource<T>> loadItem(int t);

	Completable saveItems(List<T> items);

	//TODO Make this call also save data to network layer
	Completable saveItem(T t);


	Completable updateItem(T t);

	Completable deleteItem(T t);
}
