package com.scleroid.financematic.model.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.model.Customer;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */

/**
 * Data Access Object required for
 *
 * @author Ganesh Kaple
 * @see android.arch.persistence.room.Room
 * For Model
 * @see Customer
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
    List<Customer> getAll();

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
    @Query("SELECT * FROM Customer where _id = :serialNo ")
    Customer findById(long serialNo);

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
    void insert(Customer customer);

    /**
     * Performs insertion operation for multiple values
     *
     * @param customer inserts list of customer object
     */
    @Insert
    void insertAll(Customer... customer);

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
     * Let the database be a part of history
     * I meant, it deletes the whole table
     */
    @Query("DELETE FROM Customer")
    void nukeTable();

}