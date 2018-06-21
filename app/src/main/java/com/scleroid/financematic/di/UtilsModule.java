package com.scleroid.financematic.di;

import android.support.annotation.NonNull;

import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.SnackBarUtils;
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

	@NonNull
	@Singleton
	abstract DateUtils getDateUtils();

	@NonNull
	@Singleton
	abstract TextViewUtils getTextViewUtils();

	@NonNull
	@Singleton
	abstract ActivityUtils getActivityUtils();

	@NonNull
	@Singleton
	abstract SnackBarUtils getSnackBarUtils();

}
