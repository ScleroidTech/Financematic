package com.scleroid.financematic.di;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.scleroid.financematic.data.remote.services.jobs.utils.GcmJobService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 5/2/18
 */
@Module
public class JobManagerModule {

	private AppComponent component;

	@Provides
	@Singleton
	public JobManager jobManager(Context context) {
		component = DaggerAppComponent.builder()
				.build();
		Configuration.Builder builder = new Configuration.Builder(context)
				.customLogger(new CustomLogger() {
					private static final String TAG = "JOBS";

					@Override
					public boolean isDebugEnabled() {
						return true;
					}

					@Override
					public void d(String text, Object... args) {
						Log.e(TAG, String.format(text, args));
					}

					@Override
					public void e(Throwable t, String text, Object... args) {
						Log.e(TAG, String.format(text, args), t);
					}

					@Override
					public void e(String text, Object... args) {
						Log.e(TAG, String.format(text, args));
					}

					@Override
					public void v(String text, Object... args) {

					}
				})
				.minConsumerCount(1)//always keep at least one consumer alive
				.maxConsumerCount(3)//up to 3 consumers at a time
				.loadFactor(3)//3 jobs per consumer
				.consumerKeepAlive(30)//wait 1 minute
				.injector(job -> {
					if (job instanceof JobManagerInjectable) {
						((JobManagerInjectable) job).inject(component);
					}
				});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			builder.scheduler(
					FrameworkJobSchedulerService.createSchedulerFor(context, GcmJobService.class),
					true);
		} /*else {
			int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable
			(context);
			if (enableGcm == ConnectionResult.SUCCESS) {
				builder.scheduler(GcmJobSchedulerService.createSchedulerFor(context, GcmJobService
				.class), true);
			}*/

		return new JobManager(builder.build());
	}
}