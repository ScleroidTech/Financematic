package com.scleroid.financematic.fragments.passbook;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import com.scleroid.financematic.base.BaseViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
@Deprecated
public class PassbookViewModel extends BaseViewModel
		implements com.scleroid.financematic.viewmodels.TransactionViewModel {
	//TODO add  data in it
	@Nullable
	@Override
	protected LiveData<List> updateItemLiveData() {
		return null;
	}

	@Nullable
	@Override
	protected LiveData<List> getItemList() {
		return null;
	}
}
