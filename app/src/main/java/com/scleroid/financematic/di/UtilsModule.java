package com.scleroid.financematic.di;

import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.CurrencyStringUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.TextViewUtils;

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
