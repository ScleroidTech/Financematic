package com.scleroid.financematic.fragments.expense;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class ExpenseViewModel extends BaseViewModel implements com.scleroid.financematic.viewmodels.ExpenseViewModel {
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
