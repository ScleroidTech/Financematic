package com.scleroid.financematic.di;

/**
 * Copyright (C) 3/10/18 Author ganesh
 */

import android.app.Application;
import android.arch.persistence.room.Room;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.AppExecutors;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.ExpenseDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.dao.TransactionDao;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.utils.DiskIOThreadExecutor;
import com.scleroid.financematic.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is used by Dagger to inject the required arguments into the {@link }.
 */
@Module(includes = ViewModelModule.class)
abstract public class RepositoryModule {

    private static final int THREAD_COUNT = 3;

    //TODO When DataBase Added
  /*  @Singleton
    @Binds
    @Local
    abstract LocalDataSource provideTasksLocalDataSource(LocalLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract LocalDataSource provideTasksRemoteDataSource(FakeTasksRemoteDataSource dataSource);

*/
    @Singleton
    @Provides
    static AppDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "financeMatic.db")
                .build();
    }

    @Singleton
    @Provides
    static LoanDao provideLoanDao(AppDatabase db) {
        return db.loanDao();
    }

    @Singleton
    @Provides
    static ExpenseDao provideExpenseDao(AppDatabase db) {
        return db.expenseDao();
    }

    @Singleton
    @Provides
    static CustomerDao provideCustomerDao(AppDatabase db) {
        return db.customerDao();
    }

    @Singleton
    @Provides
    static TransactionDao provideTransactionsDao(AppDatabase db) {
        return db.transactionDao();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }

    @Singleton
    @Provides
    WebService provideWebService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(WebService.class);
    }

}
