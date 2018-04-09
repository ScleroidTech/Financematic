package com.scleroid.financematic.base;

import android.arch.lifecycle.ViewModel;

import com.scleroid.financematic.AppDatabase;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public class BaseViewModel extends ViewModel {

    protected AppDatabase appDatabase;

    public BaseViewModel(final AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

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
