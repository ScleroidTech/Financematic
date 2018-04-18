package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.utils.AppExecutors;

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
 * @since 4/5/18
 */
public class LocalCustomerLab implements LocalDataSource<Customer> {
	private final AppDatabase appDatabase;
	private final AppExecutors appExecutors;
	private final CustomerDao customerDao;
	@Inject
	LoanDao loanDao;


	@Inject
	LocalCustomerLab(final AppDatabase appDatabase, final AppExecutors appExecutors) {
		this.appDatabase = appDatabase;
		this.appExecutors = appExecutors;
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
		Timber.d("creating new customer ");

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
		Timber.d("creating new customer ");

		return Completable.fromRunnable(() -> {
			long[] rowId = customerDao.saveCustomers(items);
			Timber.d("customer stored " + rowId.length);

		}).subscribeOn(Schedulers.io());
	}

	/**
	 * refreshes the data source
	 */
	@Override
	public void refreshItems() {

	}

	/**
	 * Deletes all the data source
	 */
	@Override
	public Completable deleteAllItems() {
		Timber.d("Deleting all customers");
		return Completable.fromRunnable(() -> customerDao.nukeTable()).subscribeOn(Schedulers.io
				());

	}

	/**
	 * deletes a single item from the database
	 *
	 * @param item item to be deleted
	 */
	@Override
	public Completable deleteItem(@NonNull final Customer item) {
		Timber.d("deleting customer with id %d", item.getCustomerId());

		return Completable.fromRunnable(() -> customerDao.delete(item))
				.subscribeOn(Schedulers.io());
	}

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

	public LiveData<List<Customer>> getCustomersWithLoans() {
		LiveData<List<Customer>> customerLiveData = customerDao.getAllCustomerLive();

		// TODO Test this, if works remove below code, this part has performance issues
		customerLiveData = Transformations.switchMap(customerLiveData, inputCustomers -> {
			MediatorLiveData<List<Customer>> customerMediatorLiveData = new MediatorLiveData<>();

			for (Customer customer : inputCustomers) {

				customerMediatorLiveData.addSource(
						loanDao.getLoanByCustomerIdLive(customer.getCustomerId()), loan -> {

							customer.setLoans(loan);
							customerMediatorLiveData.postValue(inputCustomers);

						});
			}
			return customerMediatorLiveData;
		});
		return customerLiveData;
       /* customerLiveData = Transformations.map(customerLiveData, inputStates -> {
            for (Customer state : inputStates) {
                state.setLoans(loanDao.getLoans(state.getCustomerId()));
            }
            return inputStates;
        });
        return customerLiveData;*/
	}

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
		//Good Job buddy, now the real challenge is next method
	}


}
