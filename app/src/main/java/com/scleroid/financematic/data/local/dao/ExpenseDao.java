package com.scleroid.financematic.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scleroid.financematic.data.local.model.Expense;

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
 * @see Expense
 * @since 10-01-2018
 */
@Dao

public interface ExpenseDao {

    /**
     * Select Query
     *
     * @return List of all expenses in database
     */
    @Query("SELECT * FROM Expense")
    List<Expense> getAll();

    /**
     * Returns  list of all expenses
     *
     * @return LiveData object List of all expenses in database
     */
    @Query("SELECT * FROM Expense")
    LiveData<List<Expense>> getAllExpenseLive();

    /**
     * Returns a specific value compared to serialNo passed
     *
     * @param serialNo the serialNo of object to be found
     * @return expense object with same serialNo
     */
    @Query("SELECT * FROM Expense where expenseId = :serialNo ")
    LiveData<Expense> getExpense(int serialNo);

    /**
     * select query to count Number of expense
     *
     * @return number of total entries in the table
     */
    @Query("SELECT COUNT(*) from Expense")
    int countExpense();

    /**
     * Performs insertion operation
     *
     * @param expense inserts this object in the database
     */
    @Insert(onConflict = REPLACE)
    long saveExpense(Expense expense);

    /**
     * Performs insertion operation for multiple values
     *
     * @param expense inserts list of expense object
     */
    @Insert
    long[] saveExpenses(List<Expense> expense);

    /**
     * Updates a specified dataset
     *
     * @param expense the expense which needs to be updated
     */
    @Update(onConflict = REPLACE)
    void update(Expense expense);

    /**
     * Removes a particular dataset from the database
     *
     * @param expense the object which needs to be deleted
     */
    @Delete
    void delete(Expense expense);

    /**
     * Let the database be a part of history
     * I meant, it deletes the whole table
     */
    @Query("DELETE FROM Expense")
    void nukeTable();


}
