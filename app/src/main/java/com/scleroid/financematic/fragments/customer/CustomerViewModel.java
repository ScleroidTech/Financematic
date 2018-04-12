package com.scleroid.financematic.fragments.customer;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class CustomerViewModel extends BaseViewModel
		implements com.scleroid.financematic.viewmodels.CustomerViewModel {

	//TODO add  data in it
	@Override
	protected LiveData<List> updateItemLiveData() {
		return null;
	}

	@Override
	protected LiveData<List> getItemList() {
		return null;
	}
}
