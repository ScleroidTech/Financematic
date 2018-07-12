package com.scleroid.financematic.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.scleroid.financematic.utils.network.Resource;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @param <N> generic viewmodel object
 * @author Ganesh Kaple
 * @since 4/6/18
 * <p>
 * It's actually unnecessary
 * its base class for all viewmodels
 */
public abstract class BaseViewModel<N> extends ViewModel {


	/**
	 * Updates the livedata object with most recent data
	 *
	 * @return resource object with fresh data
	 */
	protected abstract LiveData<Resource<List<N>>> updateItemLiveData();

	/**
	 * returns livedata object to be used in further places
	 *
	 * @return list of item in livedata wrapper
	 */
	protected abstract LiveData<Resource<List<N>>> getItemList();

}
