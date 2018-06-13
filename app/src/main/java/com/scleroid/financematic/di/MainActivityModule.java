package com.scleroid.financematic.di;

import android.support.annotation.NonNull;

import com.scleroid.financematic.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
@Module
public abstract class MainActivityModule {
	@NonNull
	@ContributesAndroidInjector(modules = FragmentBuildersModule.class)
	abstract MainActivity contributeMainActivity();


}
