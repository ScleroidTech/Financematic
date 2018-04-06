package com.scleroid.financematic.di;

import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.CurrencyStringUtils;
import com.scleroid.financematic.utils.DateUtils;
import com.scleroid.financematic.utils.TextViewUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
@Module
public abstract class UtilsModule {

    @Singleton
    @Provides
    abstract DateUtils getDateUtils();

    @Singleton
    @Provides
    abstract TextViewUtils getTextViewUtils();

    @Singleton
    @Provides
    abstract ActivityUtils getActivityUtils();

    @Singleton
    @Provides
    abstract CurrencyStringUtils getCurrencyStringUtils();
}
