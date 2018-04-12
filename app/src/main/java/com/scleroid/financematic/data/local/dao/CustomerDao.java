package com.scleroid.financematic.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.data.local.model.Customer;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Data Access Object required for
 * <p>
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @see android.arch.persistence.room.Room For Model
 * @see Customer
 * @since 4/2/18
 * @since 10-01-2018
 */
@Dao

public interface CustomerDao {

	/**
	 * Select Query
	 *
	 * @return List of all customers in database
	 */
	@Query("SELECT * FROM Customer")
	List<Customer> getCustomers();

	/**
	 * Returns  list of all customers
	 *
	 * @return LiveData object List of all customers in database
	 */
	@Query("SELECT * FROM Customer")
	LiveData<List<Customer>> getAllCustomerLive();

	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return customer object with same serialNo
	 */
	@Query("SELECT * FROM Customer where customerId = :serialNo ")
	Customer getCustomer(int serialNo);

	@Query("SELECT * FROM Customer where customerId = :serialNo ")
	Single<Customer> getRxCustomer(int serialNo);

	/**
	 * Returns a specific value compared to serialNo passed
	 *
	 * @param serialNo the serialNo of object to be found
	 * @return customer object with same serialNo
	 */
	@Query("SELECT * FROM Customer where customerId = :serialNo ")
	LiveData<Customer> getCustomerLive(int serialNo);

	/* *//**
	 * Returns no of loans per customer
	 *
	 * @param custId the id of customer which we need data about
	 * @return list of loans
	 *//*

    @Query("SELECT * FROM Loan WHERE custId = :custId")
    List<Loan> getLoans(int custId);

    *//**
	 * Returns no of loans per customer
	 *
	 * @param custId customer id which we need loan about
	 * @return list of loans in a livedata wrapper
	 *//*

    @Query("SELECT * FROM Loan WHERE custId = :custId")
    LiveData<List<Loan>> getLoansLive(int custId);
*/

	/**
	 * select query to count Number of customer
	 *
	 * @return number of total entries in the table
	 */
	@Query("SELECT COUNT(*) from Customer")
	int countCustomer();

	/**
	 * Performs insertion operation
	 *
	 * @param customer inserts this object in the database
	 */
	@Insert(onConflict = REPLACE)
	long saveCustomer(Customer customer);

	/**
	 * Performs insertion operation for multiple values
	 *
	 * @param customer inserts list of customer object
	 */
	@Insert
	long[] saveCustomers(List<Customer> customer);

	/**
	 * Updates a specified dataset
	 *
	 * @param customer the customer which needs to be updated
	 */
	@Update(onConflict = REPLACE)
	void update(Customer customer);

	/**
	 * Removes a particular dataset from the database
	 *
	 * @param customer the object which needs to be deleted
	 */
	@Delete
	void delete(Customer customer);

	/**
	 * Let the database be a part of history I meant, it deletes the whole table
	 */
	@Query("DELETE FROM Customer")
	void nukeTable();

}