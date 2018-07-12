package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.model.Customer;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @see Customer class
 * @since 4/5/18
 * <p>
 * A single point of access for all DB related queries for
 */
public class LocalCustomerLab implements LocalDataSource<Customer> {
	/**
	 * Object of Dao
	 * which will be required to perform db related queries
	 * initialized in constructor
	 */
	private final CustomerDao customerDao;
	/**
	 * Object of loanDao class,
	 * required for some queries,
	 * instantiated via dagger
	 */
	@Inject
	LoanDao loanDao;


	@Inject
	LocalCustomerLab(final AppDatabase appDatabase) {
		this.customerDao = appDatabase.customerDao();
	}

	/**
	 * gets a list of all items
	 */
	@Override
	public LiveData<List<Customer>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Customer>> customers= customerDao.getAllCustomerLive();
            appExecutors.mainThread().execute(() -> {
                if (customers.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(customers);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

		Timber.d("getting all customers");
		return customerDao.getAllCustomerLive();
	}

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */
	@Override
	public LiveData<Customer> getItem(final int itemId) {
		Timber.d("getting customer with id %d", itemId);
		return customerDao.getCustomerLive(itemId);
	}

	/**
	 * Saves item to data source
	 *
	 * @param item item object to be saved
	 */
	@Override
	public Single<Customer> saveItem(@NonNull final Customer item) {

		return Single.fromCallable(() -> {
			long rowId = customerDao.saveCustomer(item);
			Timber.d("customer stored " + rowId);
			return item;
		}).subscribeOn(Schedulers.io());
	}


	/**
	 * adds a list of objects to the data source
	 *
	 * @param items list of items
	 */
	@Override
	public Completable addItems(@NonNull final List<Customer> items) {


		return Completable.fromRunnable(() -> {
			long[] rowId = customerDao.saveCustomers(items);
			Timber.d("customer stored " + rowId.length);

		}).subscribeOn(Schedulers.io());
	}

	/**
	 * adds a list of objects to the data source
	 *
	 * @param items list of items
	 */
	@Override
	public void addNetworkItems(@NonNull final List<Customer> items) {

		long[] rowId2 = customerDao.saveCustomers(items);
		Timber.d("creating new customers " + rowId2.length);

	}

	/**
	 * adds a list of objects to the data source
	 *
	 * @param items list of items
	 */
	@Override
	public void addNetworkItem(@NonNull final Customer items) {

		long rowId2 = customerDao.saveCustomer(items);
		Timber.d("creating new customers " + rowId2);

	}


	/**
	 * Deletes all the data source
	 */
	@Override
	public Completable deleteAllItems() {
		Timber.d("Deleting all customers");
		return Completable.fromRunnable(customerDao::nukeTable).subscribeOn(Schedulers.io
				());

	}

	/**
	 * deletes a single item from the database
	 *
	 * @param item item to be deleted
	 */
	@Override
	public Single<Customer> deleteItem(@NonNull final Customer item) {
		Timber.d("deleting customer with id %d", item.getCustomerId());

		return Single.fromCallable(() -> {
			customerDao.delete(item);

			return item;
		}).subscribeOn(Schedulers.io());
	}

	@Nullable
	@Override
	public Single<Customer> updateItem(final Customer customer) {
		return null;
	}

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */

	public Single<Customer> getRxItem(final int itemId) {
		Timber.d("getting customer with id %d", itemId);
		return customerDao.getRxCustomer(itemId);
	}

	/**
	 * gets customer with all his loans attached to it's object
	 *
	 * @param id customer id for the customer to be retrieved
	 * @return customer wrapped in  livedata object
	 */
	public LiveData<Customer> getCustomer(int id) {
		LiveData<Customer> customerLiveData = customerDao.getCustomerLive(id);
		customerLiveData = Transformations.switchMap(customerLiveData, inputCustomer -> {
			MediatorLiveData<Customer> mediatorLiveData = new MediatorLiveData<>();
			//List<Loan> loans = new ArrayList<>();
			mediatorLiveData.addSource(loanDao.getLoanByCustomerIdLive(inputCustomer.getCustomerId
							()),
					inputCustomer::setLoans);

			return mediatorLiveData;
		});
		return customerLiveData;
	}


}
