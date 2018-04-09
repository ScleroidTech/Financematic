package com.scleroid.financematic.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public abstract class BaseViewModel<N> extends ViewModel {


    protected abstract LiveData<List<N>> updateItemLiveData();

    protected abstract LiveData<List<N>> getItemList();


 /* @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }
*/

}
