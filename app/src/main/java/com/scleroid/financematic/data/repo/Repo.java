package com.scleroid.financematic.data.repo;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.Resource;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/10/18
 */
public interface Repo<T> {
	LiveData<Resource<List<T>>> loadItems();

	LiveData<Resource<T>> loadItem(int t);

	void saveItems(List<T> items);

	//TODO Make this call also save data to network layer
	void saveItem(T t);

}
