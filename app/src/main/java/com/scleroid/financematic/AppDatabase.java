package com.scleroid.financematic;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scleroid.financematic.model.local.Customer;
import com.scleroid.financematic.model.local.Expense;
import com.scleroid.financematic.model.local.Loan;
import com.scleroid.financematic.model.local.TransactionModel;
import com.scleroid.financematic.model.local.dao.CustomerDao;
import com.scleroid.financematic.model.local.dao.ExpenseDao;
import com.scleroid.financematic.model.local.dao.LoanDao;
import com.scleroid.financematic.model.local.dao.TransactionModelDao;

/**
 * @author Ganesh Kaple
 * @see Customer
 * @see Loan
 * @see TransactionModel
 * @see Expense
 * @since 27/10/17
 * It is a singleton class,so it holds only one object for it's entire existence
 * It holds the current object of database
 * It handles creating of the database if it doesn't exists & providing the database object whenever required
 * There are 3 tables in this database,
 */
@Database(entities = {Customer.class, Loan.class, TransactionModel.class, Expense.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Holds the instance of the database
     */

    private static AppDatabase instance;

    /**
     * Returns the instance of AppDatabase class, creates a new one if doesn't exists,
     * & returns that
     *
     * @param context Context of Application or current activity needs to be passed
     * @return AppDatabase returns the instance of Appdatabase
     */
    public static AppDatabase getAppDatabase(Context context) {
        /*
           creates a new database if instance doesn't exists
         */

        if (instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "financeMatic-database")
                            //While Migration of database, it destroys previous versions, should be removed
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return instance;
    }

    /**
     * Destroys the instance of the database, doesn't actually destroy the database, just the pointer to it,
     */
    public static void destroyInstance() {

        instance = null;
    }

    /**
     * Customer Model Data Access Object,
     * For Room Library
     *
     * @return an object of
     * @see CustomerDao
     * @see CustomerDao
     */
    public abstract CustomerDao customerDao();

    /**
     * Loan Model Data Access Object,
     * For Room Library
     *
     * @return an object of
     * @see LoanDao
     * @see LoanDao
     */
    public abstract LoanDao loanDao();

    /**
     * Expense Model Data Access Object,
     * For Room Library
     *
     * @return an object of
     * @see ExpenseDao
     * @see ExpenseDao
     */
    public abstract ExpenseDao expenseDao();


    /**
     * TransactionModel Model Data Access Object,
     * For Room Library
     *
     * @return returns an object of
     * @see TransactionModelDao
     * @see TransactionModelDao
     */
    public abstract TransactionModelDao transactionDao();


}
