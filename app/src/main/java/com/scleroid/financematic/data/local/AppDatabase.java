package com.scleroid.financematic.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.ExpenseDao;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.dao.TransactionDao;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

/**
 * @author Ganesh Kaple
 * @see Customer
 * @see Loan
 * @see TransactionModel
 * @see Expense
 * @see com.scleroid.financematic.data.local.model.Installment
 * @since 27/10/17
 * It is responsible for the Database for entire app
 * Object initialization is done via dagger and is a singleton object over all app
 *
 */

@Database(entities = {Customer.class, Loan.class, TransactionModel.class, Expense.class,
		Installment.class}, version = 13, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

	/**
	 * Customer Model Data Access Object, For Room Library
	 *
	 * @return an object of
	 * @see CustomerDao
	 * @see CustomerDao
	 */
	public abstract CustomerDao customerDao();

	/**
	 * Loan Model Data Access Object, For Room Library
	 *
	 * @return an object of
	 * @see LoanDao
	 * @see LoanDao
	 */
	public abstract LoanDao loanDao();

	/**
	 * Expense Model Data Access Object, For Room Library
	 *
	 * @return an object of
	 * @see ExpenseDao
	 * @see ExpenseDao
	 */
	public abstract ExpenseDao expenseDao();


	/**
	 * TransactionModel Model Data Access Object, For Room Library
	 *
	 * @return returns an object of
	 * @see TransactionDao
	 * @see TransactionDao
	 */
	public abstract TransactionDao transactionDao();

	/**
	 * TransactionModel Model Data Access Object, For Room Library
	 *
	 * @return returns an object of
	 * @see TransactionDao
	 * @see TransactionDao
	 */
	public abstract InstallmentDao installmentDao();


}
