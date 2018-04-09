package com.scleroid.financematic.fragments.people;

import android.arch.lifecycle.LiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.viewmodels.CustomerViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class PeopleViewModel extends BaseViewModel implements CustomerViewModel {
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
