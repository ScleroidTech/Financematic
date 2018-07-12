package com.scleroid.financematic.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.scleroid.financematic.utils.network.Resource;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */

/**
 * It's actually unnecessary
 * its base class for all viewmodels
 *
 * @param <N> generic viewmodel object
 */
public abstract class BaseViewModel<N> extends ViewModel {


	/**
	 * Updates the livedata object with most recent data
	 * @return resource object with fresh data
	 */
	protected abstract LiveData<Resource<List<N>>> updateItemLiveData();

	/**
	 * returns livedata object to be used in further places
	 * @return
	 */
	protected abstract LiveData<Resource<List<N>>> getItemList();

}
