package com.scleroid.financematic.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scleroid.financematic.BuildConfig;
import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.ExpenseDao;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.dao.TransactionDao;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;
import com.scleroid.financematic.data.remote.WebService;
import com.scleroid.financematic.data.remote.services.jobs.utils.GcmJobService;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.ExpenseRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.multithread.DiskIOThreadExecutor;
import com.scleroid.financematic.utils.network.livedata.LiveDataCallAdapterFactory;
import com.scleroid.financematic.utils.rx.AppSchedulerProvider;
import com.scleroid.financematic.utils.rx.SchedulerProvider;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Copyright (C)
 *
 * @author ganesh This is used by Dagger to inject the required arguments into the {@link }.
 * @since 3/10/18
 */
@Module(includes = {ViewModelModule.class, JobManagerModule.class})
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

	@Provides
	@Singleton
	static Cache provideHttpCache(Application application) {
		int cacheSize = 10 * 1024 * 1024;
		Cache cache = new Cache(application.getCacheDir(), cacheSize);
		return cache;
	}

	@Provides
	@Singleton
	static OkHttpClient provideOkhttpClient(Cache cache, Interceptor interceptor,
	                                        HttpLoggingInterceptor httpLoggingInterceptor) {
		OkHttpClient.Builder client = new OkHttpClient.Builder();
		client.cache(cache);
		client.addInterceptor(httpLoggingInterceptor);
		client.addNetworkInterceptor(interceptor);

		return client.build();
	}

	@Provides
	@Singleton
	static Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient) {
		return new Retrofit.Builder()
				.baseUrl(BuildConfig.API_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(new LiveDataCallAdapterFactory())
				.client(okHttpClient)
				.build();
	}
	@Singleton
	@Provides
	static WebService provideWebService(Retrofit retrofit) {

		return retrofit
				.create(WebService.class);
	}


	@Singleton
	@Provides
	static RemotePostEndpoint providePostWebService(Retrofit retrofit) {
		//return   retrofit.create(RemotePostEndpoint.class);
		return retrofit
				.create(RemotePostEndpoint.class);
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
	@Provides
	static GcmJobService provideGcmJobService() {
		return new GcmJobService();
	}

	@Singleton
	abstract LoanRepo provideLoanRepo(AppDatabase db);

	@Singleton
	abstract ExpenseRepo provideExpenseRepo(AppDatabase db);

	@Singleton
	abstract CustomerRepo provideCustomerRepo(AppDatabase db);

	@Singleton
	abstract InstallmentRepo provideInstallmentRepo(AppDatabase db);

	@Provides
	@Singleton
	static Gson provideGson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}

	@Singleton
	abstract TransactionsRepo provideTransactionsRepo(AppDatabase db);


	@Provides
	static public HttpLoggingInterceptor loggingInterceptor() {
		HttpLoggingInterceptor interceptor =
				new HttpLoggingInterceptor(message -> Timber.i(message));
		interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
		return interceptor;
	}

	@Provides
	static public Interceptor headerInterceptor() {

		return chain -> {
			Request original = chain.request();
			Request request = original.newBuilder()
					.header("Content-Type", "application/json")
					.method(original.method(), original.body())
					.build();
			return chain.proceed(request);
		};
	}




}
