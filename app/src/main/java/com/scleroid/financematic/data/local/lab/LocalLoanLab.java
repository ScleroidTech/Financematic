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
import com.scleroid.financematic.data.local.model.Loan;
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
public class LocalLoanLab implements LocalDataSource<Loan> {
    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;
    private final LoanDao loanDao;

	@Inject
	CustomerDao customerDao;

    @Inject
    LocalLoanLab(final AppDatabase appDatabase, final AppExecutors appExecutors) {
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
        this.loanDao = appDatabase.loanDao();
    }


    /**
     * gets a list of all items
     */
    @Override
    public LiveData<List<Loan>> getItems() {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Loan>> loans= loanDao.getAllLoanLive();
            appExecutors.mainThread().execute(() -> {
                if (loans.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(loans);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all loans");
        return loanDao.getLoansLive();
    }

    /**
     * gets a single item provided by id
     *
     * @param itemId the id of the item to be get
     */
    @Override
    public LiveData<Loan> getItem(final int itemId) {
        Timber.d("getting loan with id %d", itemId);
        return loanDao.getLoanLive(itemId);
    }

	/**
	 * gets a single item provided by id
	 *
	 * @param itemId the id of the item to be get
	 */

	public LiveData<Loan> getItemWithCustomerId(final int itemId) {
		Timber.d("getting loan with customer id %d", itemId);
		return loanDao.getLoanByCustomerIdLive(itemId);
	}

	@Inject
	LocalCustomerLab localCustomerLab;


	/**
     * Saves item to data source
     *
     * @param item item object to be saved
     */
    @Override
    public Single<Loan> saveItem(@NonNull final Loan item) {
        Timber.d("creating new loan ");

        return Single.fromCallable(() -> {
            long rowId = loanDao.saveLoan(item);
            Timber.d("loan stored " + rowId);
            return item;
        }).subscribeOn(Schedulers.io());
    }

    /**
     * adds a list of objects to the data source
     *
     * @param items list of items
     */
    @Override
    public Completable addItems(@NonNull final List<Loan> items) {
        Timber.d("creating new loan ");

	    return Completable.fromRunnable(() -> {
            long[] rowId = loanDao.saveLoans(items);
            Timber.d("loan stored " + rowId.length);
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
        Timber.d("Deleting all loans");
	    return Completable.fromRunnable(() -> loanDao.nukeTable()).subscribeOn(Schedulers.io());

    }

    /**
     * deletes a single item from the database
     *
     * @param itemId id of item to be deleted
     */
    @Override
    public Completable deleteItem(final int itemId) {
        Timber.d("deleting loan with id %d", itemId);

	    return Completable.fromRunnable(
			    () -> loanDao.delete(loanDao.getLoanLive(itemId).getValue()))
			    .subscribeOn(Schedulers.io());
    }

    /**
     * deletes a single item from the database
     *
     * @param item item to be deleted
     */
    @Override
    public Completable deleteItem(@NonNull final Loan item) {
        Timber.d("deleting loan with id %d", item.getAccountNo());

	    return Completable.fromRunnable(() -> loanDao.delete(item)).subscribeOn(Schedulers.io());
    }

    /**
     * gets a list of all items for a particular value of customer no
     */

    public LiveData<List<Loan>> getItemsForCustomer(int custNo) {
        /* Alternate Method for same purpose
        Runnable runnable = () -> {
            final LiveData<List<Loan>> loans= loanDao.getAllLoanLive();
            appExecutors.mainThread().execute(() -> {
                if (loans.getValue().isEmpty()){
                    callback.onDataNotAvailable();
                }
                else callback.onLoaded(loans);
            });


        };
        appExecutors.diskIO().execute(runnable);*/

        Timber.d("getting all loans");
        return loanDao.getLoansForCustomerLive(custNo);
    }

	public Single<Loan> getRxItem(final int itemId) {
		Timber.d("getting loan with id %d", itemId);
		return loanDao.getRxLoan(itemId);
	}

	/*public LiveData<Loan> loadQuotationDetails(int quotationId) {
		LiveData<Loan> quotationLiveData =
				quotationDao.getQuotationCustomer(quotationId);
		LiveData<QuotationCustomer> result =
				Transformations.switchMap(quotationLiveData, quotation -> {
					MutableLiveData<QuotationCustomer> mutableResult = new MutableLiveData<>();
					appExecutors.diskIO().execute(() -> {
						quotation.quotationDetList =
								quotationDetDao.getQuotationDetsByQuotationIdSync(quotationId);
						mutableResult.postValue(quotation);
					});
					return mutableResult;
				});
		return result;
	}
*/
	public LiveData<List<Loan>> getLoanWithCustomers() {
		LiveData<List<Loan>> loansLive = loanDao.getLoansLive();

		// TODO Test this, if works remove below code, this part has performance issues
		loansLive = Transformations.switchMap(loansLive, (List<Loan> inputLoan) -> {
			MediatorLiveData<List<Loan>> loanMediatorLiveData = new MediatorLiveData<>();
			for (Loan loan : inputLoan) {
				loanMediatorLiveData.addSource(localCustomerLab.getItem(loan.getCustId()),
						(Customer customer) -> {
							loan.setCustomer(customer);
							loanMediatorLiveData.postValue(inputLoan);

						});
			}
			return loanMediatorLiveData;
		});
		return loansLive;
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

	public LiveData<Loan> getLoanWithCustomer(int id) {
		LiveData<Loan> loanLiveData = loanDao.getLoanLive(id);
		final LiveData<Loan> finalLoanLiveData = loanLiveData;

		loanLiveData = Transformations.switchMap(loanLiveData, (Loan loan) -> {
			Customer customer = localCustomerLab.getRxItem(loan.getCustId());
			loan.setCustomer(customer);
			return finalLoanLiveData;
		});
		return loanLiveData;
		//Good Job buddy, now the real challenge is next method
	}

	public LiveData<Loan> loadLoanDetails(int loanId) {
		LiveData<Loan> loanLiveData = loanDao.getLoanLive(loanId);
		LiveData<Loan> result =
				Transformations.switchMap(loanLiveData, loan -> {
					MediatorLiveData<Loan> mutableResult = new MediatorLiveData<>();
					mutableResult.addSource(localCustomerLab.getItem(loan.getCustId()),
							(Customer customer) -> {
								loan.setCustomer(customer);
								mutableResult.postValue(loan);
							});
					return mutableResult;
				});
		return result;
	}
}
