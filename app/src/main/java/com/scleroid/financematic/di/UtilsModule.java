package com.scleroid.financematic.di;

import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.CurrencyStringUtils;
import com.scleroid.financematic.utils.DateUtils;
import com.scleroid.financematic.utils.TextViewUtils;

import javax.inject.Singleton;

import dagger.Module;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
@Module
public abstract class UtilsModule {

    @Singleton

    abstract DateUtils getDateUtils();

    @Singleton

    abstract TextViewUtils getTextViewUtils();

    @Singleton
    abstract ActivityUtils getActivityUtils();

    @Singleton
    abstract CurrencyStringUtils getCurrencyStringUtils();
}
