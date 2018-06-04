package com.scleroid.financematic.data.remote.services.jobs.utils;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;

import javax.inject.Inject;

public class GcmJobService extends GcmJobSchedulerService {

	@Inject
	JobManager jobManager;

	@NonNull
	protected JobManager getJobManager() {
		return JobManagerFactory.getJobManager();
	}
}
