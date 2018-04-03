package com.scleroid.financematic.model.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.model.local.Loan;

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
    List<Loan> getAll();

    /**
     * Returns  list of all loans
     *
     * @return LiveData object List of all loans in database
     */
    @Query("SELECT * FROM Loan")
    LiveData<List<Loan>> getAllLoansLive();

    /**
     * Returns a specific value compared to serialNo passed
     *
     * @param serialNo the serialNo of object to be found
     * @return loan object with same serialNo
     */
    @Query("SELECT * FROM Loan where accountNo  = :serialNo ")
    Loan findById(int serialNo);

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
    void insert(Loan loan);

    /**
     * Performs insertion operation for multiple values
     *
     * @param loan inserts list of loan object
     */
    @Insert
    void insertAll(Loan... loan);

    /**
     * Updates a specified dataset
     *
     * @param loan the loan which needs to be updated
     */
    @Update(onConflict = REPLACE)
    void update(Loan loan);

    /**
     * Removes a particular dataset from the database
     *
     * @param loan the object which needs to be deleted
     */
    @Delete
    void delete(Loan loan);

    /**
     * Let the database be a part of history
     * I meant, it deletes the whole table
     */
    @Query("DELETE FROM Loan")
    void nukeTable();


}
