package com.scleroid.financematic.utils.network;

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.scleroid.financematic.data.remote.ApiResponse;
import com.scleroid.financematic.utils.multithread.AppExecutors;

import java.util.Objects;

import timber.log.Timber;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 * <p>
 * You can read more about it in the <a href="https://developer.android.com/arch">Architecture
 * Guide</a>.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {
	private final AppExecutors appExecutors;

	private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

	@MainThread
	protected NetworkBoundResource(AppExecutors appExecutors) {
		this.appExecutors = appExecutors;
		result.setValue(Resource.loading(null));
		LiveData<ResultType> dbSource = loadFromDb();
		result.addSource(dbSource, data -> {
			result.removeSource(dbSource);
			if (shouldFetch(data)) {
				fetchFromNetwork(dbSource);
			} else {
				result.addSource(dbSource, newData -> setValue(Resource.success(newData)));
			}
		});
	}

	private void fetchFromNetwork(@NonNull final LiveData<ResultType> dbSource) {
		LiveData<ApiResponse<RequestType>> apiResponse = createCall();
		/*result.addSource(dbSource,
				newData -> setValue(Resource.error("No data here", newData)));*/

		// we re-attach dbSource as a new source, it will dispatch its latest value quickly
		result.addSource(dbSource, newData -> setValue(Resource.loading(newData)));
		result.addSource(apiResponse, response -> {
			result.removeSource(apiResponse);
			result.removeSource(dbSource);

			if (response != null) {
				if (response.isSuccessful()) {
					appExecutors.diskIO().execute(() -> {
						Timber.d(
								response.body != null ? response.body.toString() : response
										.errorMessage);
						saveCallResult(processResponse(response));
						appExecutors.mainThread().execute(() ->
								// we specially request a new live data,
								// otherwise we will get immediately last cached value,
								// which may not be updated with latest results received from
								// network.
								result.addSource(loadFromDb(),
										newData -> setValue(Resource.success(newData)))
						);
					});
				} else {
					onFetchFailed();
					result.addSource(dbSource,
							newData -> setValue(Resource.error(response.errorMessage, newData)));
				}
			} else {
				Timber.d("No response from server ");
			}
		});
	}

	@MainThread
	private void setValue(Resource<ResultType> newValue) {
		if (!Objects.equals(result.getValue(), newValue)) {
			result.setValue(newValue);
		}
	}

	protected void onFetchFailed() {
	}

	@Nullable
	@WorkerThread
	protected RequestType processResponse(@NonNull ApiResponse<RequestType> response) {
		Timber.d(response.body + "");
		return response.body;
	}

	@WorkerThread
	protected abstract void saveCallResult(@NonNull RequestType item);

	@NonNull
	@MainThread
	protected abstract LiveData<ApiResponse<RequestType>> createCall();

	@MainThread
	protected abstract boolean shouldFetch(@Nullable ResultType data);

	@NonNull
	@MainThread
	protected abstract LiveData<ResultType> loadFromDb();

	@NonNull
	public LiveData<Resource<ResultType>> asLiveData() {
		return result;
	}
}