package com.scleroid.financematic.data.remote;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public interface RemoteDataSource<T> {
	Completable sync(T t);

	CompletableSource sync(List<T> items);
}
