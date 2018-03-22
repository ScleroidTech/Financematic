package com.scleroid.financematic.di;

/**
 * Copyright (C) 3/10/18
 * Author ganesh
 */

import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.utils.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is used by Dagger to inject the required arguments into the {@link }.
 */
@Module
abstract public class RepositoryModule {

    private static final int THREAD_COUNT = 3;

    //TODO When DataBase Added
  /*  @Singleton
    @Binds
    @Local
    abstract TasksDataSource provideTasksLocalDataSource(TasksLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract TasksDataSource provideTasksRemoteDataSource(FakeTasksRemoteDataSource dataSource);

    @Singleton
    @Provides
    static ToDoDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), ToDoDatabase.class, "Tasks.db")
                .build();
    }

    @Singleton
    @Provides
    static TasksDao provideTasksDao(ToDoDatabase db) {
        return db.taskDao();
    }
*/
    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
