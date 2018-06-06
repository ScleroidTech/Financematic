package com.scleroid.financematic.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.data.local.model.Installment;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
@Dao
public interface InstallmentDao {

	/**
	 * Select Query
	 *
	 * @return List of all installments in database
	 */
	@Query("SELECT * FROM installment")
	List<Installment> getAll();

	/**
	 * Returns  list of all installments
	 *
	 * @return LiveData object List of all installments in database
	 */
	@Query("SELECT * FROM Installment")
	LiveData<List<Installment>> getAllInstallmentsLive();

	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return installment object with same serialNo
	 */
	@Query("SELECT * FROM Installment where installmentId = :serialNo ")
	LiveData<Installment> getInstallment(int serialNo);

	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return installment object with same serialNo
	 */
	@Query("SELECT * FROM Installment where installmentId = :serialNo ")
	Single<Installment> getRxInstallment(int serialNo);


	/**
	 * select query to count Number of installment
	 *
	 * @return number of total entries in the table
	 */
	@Query("SELECT COUNT(*) from Installment")
	int countInstallment();

	/**
	 * Performs insertion operation
	 *
	 * @param installmentModel inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	long[] saveInstallments(List<Installment> installmentModel);

	/**
	 * Performs insertion operation
	 *
	 * @param installmentModel inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	long saveInstallment(Installment installmentModel);



	/**
	 * Updates a specified dataset
	 *
	 * @param installment the installmentModel which needs to be updated
	 */
	@Update(onConflict = REPLACE)
	int update(Installment installment);

	/**
	 * Removes a particular dataset from the database
	 *
	 * @param installment the object which needs to be deleted
	 */
	@Delete
	void delete(Installment installment);

	/**
	 * Let the database be a part of history I meant, it deletes the whole table
	 */
	@Query("DELETE FROM Installment")
	void nukeTable();

	@Query("SELECT * FROM installment WHERE loanAcNo=:userId")
	List<Installment> getInstallmentsForLoan(final int userId);

	@Query("SELECT * FROM installment WHERE loanAcNo=:userId")
	LiveData<List<Installment>> getInstallmentsForLoanLive(final int userId);


}
