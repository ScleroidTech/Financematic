package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/5/18
 */
public class LocalInstallmentsLab implements LocalDataSource<Installment> {
	private final InstallmentDao installmentDao;
	@Inject
	LocalLoanLab loanLab;


	@Inject
	LocalInstallmentsLab(final AppDatabase appDatabase) {
		this.installmentDao = appDatabase.installmentDao();
	}

	/**
	 * gets a list of all items
	 */
	@Override
	public LiveData<List<Installment>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Installment>> installments= installmentDao.getAllInstallmentLive();
            appExecutors.mainThread().execute(() -> {
                if (installments.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(installments);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

		Timber.d("getting all installments");
		return installmentDao.getAllInstallmentsLive();
	}

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */
	@Override
	public LiveData<Installment> getItem(final int itemId) {
		Timber.d("getting installment with id %d", itemId);
		return installmentDao.getInstallment(itemId);
	}

	/**
	 * Saves item to data source
	 *
	 * @param item item object to be saved
	 */
	@Override
	public Single<Installment> saveItem(@NonNull final Installment item) {
		Timber.d("creating new installment ");

		return Single.fromCallable(() -> {
			long rowId = installmentDao.saveInstallment(item);
			Timber.d("installment stored " + rowId);
			return item;
		}).subscribeOn(Schedulers.io());
	}

	/**
	 * adds a list of objects to the data source
	 *
	 * @param items list of items
	 */
	@Override
	public Completable addItems(@NonNull final List<Installment> items) {
		Timber.d("ABCD creating new installment ");
		//	installmentDao.saveInstallments(items);
		return Completable.fromRunnable(() -> {
			long[] rowId = installmentDao.saveInstallments(items);
			Timber.d("ABCD installment stored " + rowId.length);
		}).subscribeOn(Schedulers.io());
	}

	@Override
	public void addNetworkItems(@NonNull final List<Installment> items) {
		Timber.d("Storing data");
		long[] rowId = installmentDao.saveInstallments(items);
		Timber.d("installment stored " + rowId.length);
	}

	@Override
	public void addNetworkItem(@NonNull final Installment item) {
		Timber.d("Storing single data");
		long rowId = installmentDao.saveInstallment(item);
		Timber.d("installment stored " + rowId);
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
		Timber.d("Deleting all installments");
		return Completable.fromRunnable(() -> installmentDao.nukeTable())
				.subscribeOn(Schedulers.io());

	}

	/**
	 * deletes a single item from the database
	 *
	 * @param item item to be deleted
	 */
	@Override
	public Single<Installment> deleteItem(@NonNull final Installment item) {
		Timber.d("deleting installment with id %d", item.getInstallmentId());
		return Single.fromCallable(() -> {
			installmentDao.delete(item);
			Timber.d("installment Deleted ");
			return item;
		}).subscribeOn(Schedulers.io());

	}

	/**
	 * Saves item to data source
	 *
	 * @param item item object to be saved
	 */

	public Single<Installment> updateItem(@NonNull final Installment item) {
		Timber.d("creating new installment ");

		return Single.fromCallable(() -> {
			int rowId = installmentDao.update(item);
			Timber.d("installment stored " + rowId);
			return item;
		}).subscribeOn(Schedulers.io());
	}

	/**
	 * adds a list of objects to the data source
	 */
	public Single<Installment> updateInstallments(final int acNo, final BigDecimal amt) {
		Timber.d("ABCD creating new installment ");
		return Single.fromCallable(() -> {
			Installment installment =
					installmentDao.updateInstallmentAmount(acNo);
			Timber.d("ABCD job done bro");
			return installment;
		}).subscribeOn(Schedulers.io());
		//
	}

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */
	public Single<Installment> getRxItem(final int itemId) {
		Timber.d("getting installment with id %d", itemId);
		return installmentDao.getRxInstallment(itemId);
	}

	/**
	 * gets a list of all items
	 */
	public Flowable<List<Installment>> getRxItemsForLoan(int acNo) {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Installment>> installments= installmentDao.getAllInstallmentLive();
            appExecutors.mainThread().execute(() -> {
                if (installments.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(installments);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

		Timber.d("getting all installments");
		return installmentDao.getRxInstallmentsByLoan(acNo);
	}

	/**
	 * gets a list of all items for a particular value of customer no
	 */

	public LiveData<List<Installment>> getItemsForLoan(int acNo) {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Installment>> installments= installmentDao.getAllInstallmentLive();
            appExecutors.mainThread().execute(() -> {
                if (installments.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(installments);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

		Timber.d("getting all installments");
		return installmentDao.getInstallmentsForLoanLive(acNo);
	}

	public LiveData<List<Installment>> getInstallmentWithCustomers() {
		LiveData<List<Installment>> installmentsLive = installmentDao.getAllInstallmentsLive();

		// TODO Test this, if works remove below code, this part has performance issues
		installmentsLive = Transformations.switchMap(installmentsLive,
				(List<Installment> inputInstallment) -> {
					MediatorLiveData<List<Installment>> installmentMediatorLiveData =
							new MediatorLiveData<>();
					for (Installment installment : inputInstallment) {
						installmentMediatorLiveData.addSource(
								loanLab.loadLoanDetails(installment.getLoanAcNo()),
								(Loan customer) -> {
									installment.setLoan(customer);
									installmentMediatorLiveData.postValue(inputInstallment);

								});
					}
					return installmentMediatorLiveData;
				});
		return installmentsLive;
		/*loansLive = Transformations.map(loansLive, new Function<List<Customer>, List<Customer>>
		() {

			@Override
			public List<Customer> apply(final List<Customer> inputStates) {
               *//* for (Customer state : inputStates) {
                    state.setLoans(dao.getLoans(state.getCustomerId()));
                }*//*
				return inputStates;
			}
		});
		return loansLive;*/
	}


}
