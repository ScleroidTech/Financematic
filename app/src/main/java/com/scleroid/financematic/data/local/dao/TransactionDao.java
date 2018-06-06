package com.scleroid.financematic.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
@Dao
public interface TransactionDao {

	/**
	 * Select Query
	 *
	 * @return List of all transactionModels in database
	 */
	@Query("SELECT * FROM TransactionModel")
	List<TransactionModel> getAll();

	/**
	 * Returns  list of all transactionModels
	 *
	 * @return LiveData object List of all transactionModels in database
	 */
	@Query("SELECT * FROM TransactionModel")
	LiveData<List<TransactionModel>> getAllTransactionsLive();

	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return transactionModel object with same serialNo
	 */
	@Query("SELECT * FROM TransactionModel where transactionId = :serialNo ")
	LiveData<TransactionModel> getTransaction(int serialNo);

	/**
	 * select query to count Number of transactionModel
	 *
	 * @return number of total entries in the table
	 */
	@Query("SELECT COUNT(*) from TransactionModel")
	int countTransaction();

	/**
	 * Performs insertion operation
	 *
	 * @param transactionModelModel inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	long[] saveTransactions(List<TransactionModel> transactionModelModel);

	/**
	 * Performs insertion operation
	 *
	 * @param transactionModelModel inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	long saveTransaction(TransactionModel transactionModelModel);


	/**
	 * Updates a specified dataset
	 *
	 * @param transactionModel the transactionModelModel which needs to be updated
	 */
	@Update(onConflict = REPLACE)
	void update(TransactionModel transactionModel);

	/**
	 * Removes a particular dataset from the database
	 *
	 * @param transactionModel the object which needs to be deleted
	 */
	@Delete
	void delete(TransactionModel transactionModel);

	/**
	 * Let the database be a part of history I meant, it deletes the whole table
	 */
	@Query("DELETE FROM TransactionModel")
	void nukeTable();

	@Query("SELECT * FROM transactionmodel WHERE loanAcNo=:userId")
	List<TransactionModel> getTransactionsForLoan(final int userId);

	@Query("SELECT * FROM TransactionModel WHERE loanAcNo=:userId")
	LiveData<List<TransactionModel>> getTransactionsForLoanLive(final int userId);


}
