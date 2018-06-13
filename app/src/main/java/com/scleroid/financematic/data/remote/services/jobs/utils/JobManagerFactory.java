package com.scleroid.financematic.data.remote.services.jobs.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import timber.log.Timber;

public class JobManagerFactory {

	private static JobManager jobManager;
	@NonNull
	private static CustomLogger customLogger = new CustomLogger() {

		@Override
		public boolean isDebugEnabled() {
			return true;
		}

		@Override
		public void d(@NonNull String text, Object... args) {
			Timber.d(String.format(text, args));
		}

		@Override
		public void e(Throwable t, @NonNull String text, Object... args) {
			Timber.e(t, String.format(text, args));
		}

		@Override
		public void e(@NonNull String text, Object... args) {
			Timber.e(String.format(text, args));
		}

		@Override
		public void v(String text, Object... args) {
			// no-op
		}
	};

	public static synchronized JobManager getJobManager() {
		return jobManager;
	}

	public static synchronized JobManager getJobManager(@NonNull Context context) {
		if (jobManager == null) {
			jobManager = configureJobManager(context);

		}
		return jobManager;
	}

	private static JobManager configureJobManager(@NonNull Context context) {

	 /*   DependencyInjector dependencyInjector = job -> {
		    // this line depends on how your Dagger components are setup;
		    // the important part is to cast job to BaseJob
		    ((Application) context).DaggerAppComponent().inject((BaseJob) job);
	    };
*/
		Configuration.Builder builder = new Configuration.Builder(context)
				.minConsumerCount(1)//always keep at least one consumer alive
				.maxConsumerCount(3)//up to 3 consumers at a time
				.loadFactor(3)//3 jobs per consumer
				.consumerKeepAlive(120)//wait 2 minutes
				//      .injector(dependencyInjector)
				.customLogger(customLogger);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(context,
					SchedulerJobService.class), true);
		} else {
			int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable
           (context);
            if (enableGcm == ConnectionResult.SUCCESS) {
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(context,
                        GcmJobSchedulerService.class), true);
            }
		}
		return new JobManager(builder.build());
	}
}
