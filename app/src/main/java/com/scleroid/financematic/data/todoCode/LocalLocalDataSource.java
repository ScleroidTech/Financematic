package com.scleroid.financematic.data.todoCode;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/4/18
 */

import com.scleroid.financematic.AppExecutors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Concrete implementation of a data source as a db.
 */
@Singleton
public class LocalLocalDataSource implements com.scleroid.financematic.data.local.LocalDataSource {

    private final TasksDao mTasksDao;

    private final AppExecutors mAppExecutors;

    @Inject
    public LocalLocalDataSource(@NonNull AppExecutors executors, @NonNull TasksDao tasksDao) {
        mTasksDao = tasksDao;
        mAppExecutors = executors;
    }

    /*
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = mTasksDao.getTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTasksLoaded(tasks);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }


    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Task task = mTasksDao.getTaskById(taskId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task != null) {
                            callback.onTaskLoaded(task);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        checkNotNull(task);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.insertTask(task);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void completeTask(@NonNull final Task task) {
        Runnable completeRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), true);
            }
        };

        mAppExecutors.diskIO().execute(completeRunnable);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        // Not required for the local data source because the {@link LoanRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void activateTask(@NonNull final Task task) {
        Runnable activateRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), false);
            }
        };
        mAppExecutors.diskIO().execute(activateRunnable);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        // Not required for the local data source because the {@link LoanRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void clearCompletedTasks() {
        Runnable clearTasksRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteCompletedTasks();

            }
        };

        mAppExecutors.diskIO().execute(clearTasksRunnable);
    }

    @Override
    public void refreshTasks() {
        // Not required because the {@link LoanRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllTasks() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteTasks();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteTask(@NonNull final String taskId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteTaskById(taskId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}