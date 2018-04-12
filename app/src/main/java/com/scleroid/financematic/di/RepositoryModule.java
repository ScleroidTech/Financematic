package com.scleroid.financematic.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.ExpenseDao;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.dao.TransactionDao;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.ExpenseRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.utils.AppExecutors;
import com.scleroid.financematic.utils.DiskIOThreadExecutor;
import com.scleroid.financematic.utils.LiveDataCallAdapterFactory;
import com.scleroid.financematic.utils.rx.AppSchedulerProvider;
import com.scleroid.financematic.utils.rx.SchedulerProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Copyright (C)
 *
 * @author ganesh This is used by Dagger to inject the required arguments into the {@link }.
 * @since 3/10/18
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

		AppDatabase appDatabase =
				Room.databaseBuilder(context, AppDatabase.class, "financeMatic.db")
						/*TODO
						.addCallback(new RoomDatabase.Callback() {
			   /**
				 * Called when the database is created for the first time.
				 * This is called after all the tables are created.
				 *
				 * @param db The database.

							@Override
							public void onCreate(@NonNull final SupportSQLiteDatabase db) {
								super.onCreate(db);

								//TODO add trigger to update values depending upon operations
								db.execSQL("CREATE TRIGGER");
							}
						})*/
						.fallbackToDestructiveMigration()
						.build();
		Timber.wtf("why we aren't calling this" + appDatabase);
		return appDatabase;
	}

	@Singleton
	@Provides
	static EventBus providesGlobalBus() {
		return EventBus.getDefault();
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
	static InstallmentDao provideInstallmentDao(AppDatabase db) {
		return db.installmentDao();
	}

	@Singleton
	@Provides
	static WebService provideWebService() {
		return new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
//			    .addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(new LiveDataCallAdapterFactory())
				.build()
				.create(WebService.class);
	}

	@Singleton
	@Provides
	static SchedulerProvider provideSchedulerProvider() {
		return new AppSchedulerProvider();
	}

	@Singleton
	@Provides
	static AppExecutors provideAppExecutors() {
		return new AppExecutors(new DiskIOThreadExecutor(),
				Executors.newFixedThreadPool(THREAD_COUNT),
				new AppExecutors.MainThreadExecutor());
	}

	@Singleton
	abstract LoanRepo provideLoanRepo(AppDatabase db);

	@Singleton
	abstract ExpenseRepo provideExpenseRepo(AppDatabase db);

	@Singleton
	abstract CustomerRepo provideCustomerRepo(AppDatabase db);

	@Singleton
	abstract InstallmentRepo provideInstallmentRepo(AppDatabase db);
	/*@Provides
	@Singleton
	Gson provideGson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}*/

	@Singleton
	abstract TransactionsRepo provideTransactionsRepo(AppDatabase db);


}
