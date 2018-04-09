package com.scleroid.financematic.fragments.loanDetails;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.viewmodels.LoanViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class LoanDetailsViewModel extends BaseViewModel implements LoanViewModel {
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
