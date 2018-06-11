package com.scleroid.financematic.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Copyright (C) 2018
 *
 * @since 4/2/18
 */

/**
 * Data Access Object required for
 *
 * @author Ganesh Kaple
 * @see android.arch.persistence.room.Room
 * For Model
 * @see Loan
 * @since 10-01-2018
 */

@Dao
public interface LoanDao {

	/**
	 * Select Query
	 *
	 * @return List of all loans in database
	 */
	@Query("SELECT * FROM Loan")
	List<Loan> getLoans();

	/**
	 * Returns  list of all loans
	 *
	 * @return LiveData object List of all loans in database
	 */
	@Query("SELECT * FROM Loan")
	LiveData<List<Loan>> getLoansLive();

	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return loan object with same serialNo
	 */
	@Query("SELECT * FROM Loan where accountNo  = :serialNo ")
	LiveData<Loan> getLoanLive(int serialNo);


	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the customer Id of object to be found
	 * @return loan object with same serialNo
	 */
	@Query("SELECT * FROM Loan where custId  = :serialNo ")
	LiveData<List<Loan>> getLoanByCustomerIdLive(int serialNo);


	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return loan object with same serialNo
	 */
	@Query("SELECT * FROM Loan where accountNo  = :serialNo ")
	Loan getLoan(int serialNo);

	@Query("SELECT * FROM Loan where accountNo  = :serialNo ")
	Single<Loan> getRxLoan(int serialNo);

	/**
	 * select query to count Number of loan
	 *
	 * @return number of total entries in the table
	 */
	@Query("SELECT COUNT(*) from Loan")
	int countLoan();

	/**
	 * Performs insertion operation
	 *
	 * @param loan inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	long saveLoan(Loan loan);

	/**
	 * Performs insertion operation for multiple values
	 *
	 * @param loan inserts list of loan object
	 */
	@Insert(onConflict = REPLACE)
	long[] saveLoans(List<Loan> loan);

	/**
	 * Updates a specified dataset
	 *
	 * @param loan the loan which needs to be updated
	 */
	@Update(onConflict = REPLACE)
	int update(Loan loan);

	/**
	 * Removes a particular dataset from the database
	 *
	 * @param loan the object which needs to be deleted
	 */
	@Delete
	void delete(Loan loan);

	/**
	 * Let the database be a part of history I meant, it deletes the whole table
	 */
	@Query("DELETE FROM Loan")
	void nukeTable();


	/**
	 * Returns no of transactions per loan
	 *
	 * @param accNo the id of loan which we need data about
	 * @return list of transactions per loan
	 */

	@Query("SELECT * FROM TransactionModel WHERE loanAcNo = :accNo")
	List<TransactionModel> getTransactions(int accNo);

	/**
	 * Returns no of loans per customer
	 *
	 * @param accNo id of the loan which we need data about
	 * @return list of transactions in a livedata wrapper
	 */

	@Query("SELECT * FROM TransactionModel WHERE loanAcNo = :accNo")
	LiveData<List<TransactionModel>> getTransactionsLive(int accNo);

	@Query("SELECT * FROM loan WHERE custId=:userId")
	List<Loan> getLoansForCustomer(final int userId);

	@Query("SELECT * FROM loan WHERE custId=:userId")
	LiveData<List<Loan>> getLoansForCustomerLive(final int userId);


}
