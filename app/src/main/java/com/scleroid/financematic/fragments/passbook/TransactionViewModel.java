package com.scleroid.financematic.fragments.passbook;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class TransactionViewModel extends BaseViewModel implements com.scleroid.financematic.viewmodels.TransactionViewModel {
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
