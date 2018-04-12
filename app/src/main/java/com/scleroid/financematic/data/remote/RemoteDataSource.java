package com.scleroid.financematic.data.remote;

import io.reactivex.Completable;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
public interface RemoteDataSource<T> {
	Completable sync(T t);
}
