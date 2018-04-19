package com.scleroid.financematic.data.local.lab;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.data.local.AppDatabase;
import com.scleroid.financematic.data.local.LocalDataSource;
import com.scleroid.financematic.data.local.dao.ExpenseDao;
import com.scleroid.financematic.data.local.model.Expense;

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
public class LocalExpenseLab implements LocalDataSource<Expense> {
	private final ExpenseDao expenseDao;

	@Inject
	LocalExpenseLab(final AppDatabase appDatabase) {
		this.expenseDao = appDatabase.expenseDao();
	}


	/**
	 * gets a list of all items
	 */
	@Override
	public LiveData<List<Expense>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Expense>> expenses= expenseDao.getAllExpenseLive();
            appExecutors.mainThread().execute(() -> {
                if (expenses.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(expenses);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

		Timber.d("getting all expenses");
		return expenseDao.getAllExpenseLive();
	}

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */
	@Override
	public LiveData<Expense> getItem(final int itemId) {
		Timber.d("getting expense with id %d", itemId);
		return expenseDao.getExpense(itemId);
	}

	/**
	 * Saves item to data source
	 *
	 * @param item item object to be saved
	 */
	@Override
	public Single<Expense> saveItem(@NonNull final Expense item) {
		Timber.d("creating new expense ");

		return Single.fromCallable(() -> {
			long rowId = expenseDao.saveExpense(item);
			Timber.d("expense stored " + rowId);
			return item;
		}).subscribeOn(Schedulers.io());
	}


	/**
	 * adds a list of objects to the data source
	 *
	 * @param items list of items
	 */
	@Override
	public Completable addItems(@NonNull final List<Expense> items) {
		Timber.d("creating new expense ");

		return Completable.fromRunnable(() -> {
			long[] rowId = expenseDao.saveExpenses(items);
			Timber.d("expense stored " + rowId.length);
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
		Timber.d("Deleting all expenses");
		return Completable.fromRunnable(() -> expenseDao.nukeTable()).subscribeOn(Schedulers.io());

	}

	/**
	 * deletes a single item from the database
	 *
	 * @param item item to be deleted
	 */
	@Override
	public Completable deleteItem(@NonNull final Expense item) {
		Timber.d("deleting expense with id %d", item.getExpenseId());

		return Completable.fromRunnable(() -> expenseDao.delete(item)).subscribeOn(Schedulers.io
				());
	}

	@Override
	public Single<Expense> updateItem(final Expense expense) {
		Timber.d("creating new installment ");

		return Single.fromCallable(() -> {
			int rowId = expenseDao.update(expense);
			Timber.d("installment stored " + rowId);
			return expense;
		}).subscribeOn(Schedulers.io());
	}
}
