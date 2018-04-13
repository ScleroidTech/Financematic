package com.scleroid.financematic;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import io.bloco.faker.Faker;
import timber.log.Timber;

public class TempDataFaker {

	private List<Installment> installments;
	private List<TransactionModel> transactions;
	private List<Customer> customers;
	private List<Loan> loans;
	private List<Expense> expenses;

	public TempDataFaker() { }

	@DebugLog
	void populateData(Faker faker) {
		customers = new ArrayList<Customer>();
		loans = new ArrayList<Loan>();
		installments = new ArrayList<Installment>();
		transactions = new ArrayList<TransactionModel>();
		expenses = new ArrayList<Expense>();
		int customerId = faker.number.positive();
		int accountNo = faker.number.between();
		Customer customerData = createCustomerData(faker, customerId);
		Timber.d(customerData.toString());
		customers.add(customerData);
		for (int i = 0; i < 5; i++) {

			Loan loanData = createLoanData(faker, customerId, accountNo);
			Timber.d(loanData.toString());
			loans.add(loanData);
			for (int j = 0; j < 5; j++) {
				TransactionModel transactionData = createTransactionData(faker, accountNo);
				Timber.d(transactionData.toString());
				transactions.add(transactionData);
				Installment installmentData = createInstallmentData(faker, accountNo);
				Timber.d(installmentData.toString());
				installments.add(installmentData);
				Expense expenseData =
						createExpenseData(faker);
				Timber.d(expenseData.toString());
				expenses.add(expenseData);
			}

			accountNo = faker.number.between();

		}

	}

	Customer createCustomerData(Faker faker, int customerId) {

		return new Customer(
				customerId,
				faker.name.name(),
				faker.phoneNumber.phoneNumber(),
				faker.address.streetAddress(),
				faker.address.city(),
				(faker.number.hexadecimal(12)) + "",
				(byte) faker.number.between(0, 6)

		);
	}

	Loan createLoanData(Faker faker, int customerId, int accountNo) {

		return new Loan(
				faker.commerce.price(5000, 1000000),
				faker.date.backward(),
				faker.date.forward(),
				faker.number.between(1, 100),
				faker.commerce.price(0, 2000),
				faker.number.between(1, 20), faker.number.between(0, 20),
				(byte) faker.number.between(0, 9),
				faker.commerce.price(6000, 1100000),
				accountNo,
				customerId,
				faker.commerce.price(6000, 100000)

		);
	}

	TransactionModel createTransactionData(Faker faker, int accountNo) {

		return new TransactionModel(
				faker.number.positive(),
				faker.date.backward(),
				faker.commerce.price(),
				faker.commerce.price(),
				faker.commerce.price(0, 2000),
				faker.company.catchPhrase(),
				accountNo

		);
	}

	Installment createInstallmentData(Faker faker, int accountNo) {

		return new Installment(
				faker.number.positive(),
				faker.date.forward(),
				faker.commerce.price(),
				accountNo
		);
	}

	Expense createExpenseData(Faker faker) {

		return new Expense(
				faker.commerce.price(),
				(byte) faker.number.between(0, 5),
				faker.date.backward()
		);
	}

	@DebugLog
	void saveInDatabase(MainActivity mainActivity) {
		mainActivity.getCustomerRepo().saveItems(customers).subscribe(() -> {
			// handle completion
			Timber.d("Items Saved");
			//YOu can't run this on Background Thread
			//	Toasty.success(context, "Customers Added");
		}, throwable -> {
			// handle error
			Timber.d(throwable, "Items not Saved" + throwable.getMessage());
			//		Toasty.error(context, "Customers Not Added");
		});
		mainActivity.getLoanRepo().saveItems(loans).subscribe(() -> {
			// handle completion
			Timber.d("Items Saved");
			//		Toasty.success(context, "Customers Added");
		}, throwable -> {
			// handle error
			Timber.d(throwable, "Items not Saved" + throwable.getMessage());
			//		Toasty.error(context, "Customers Not Added");
		});
		mainActivity.getTransactionsRepo()
				.saveItems(transactions)
				.subscribe(() -> {
					// handle completion
					Timber.d("Items Saved");
					//		Toasty.success(context, "Customers Added");
				}, throwable -> {
					// handle error
					Timber.d(throwable, "Items not Saved" + throwable.getMessage());
					//		Toasty.error(context, "Customers Not Added");
				});
		mainActivity.getInstallmentRepo()
				.saveItems(installments)
				.subscribe(() -> {
					// handle completion
					Timber.d("Items Saved");
					//		Toasty.success(context, "Customers Added");
				}, throwable -> {
					// handle error
					Timber.d(throwable, "Items not Saved" + throwable.getMessage());
					//		Toasty.error(context, "Customers Not Added");
				});


		mainActivity.getExpenseRepo().saveItems(expenses)

				.subscribe(() -> {
			// handle completion
			Timber.d("Items Saved");
			//		Toasty.success(context, "Customers Added");
		}, throwable -> {
			// handle error
			Timber.d(throwable, "Items not Saved" + throwable.getMessage());
			//		Toasty.error(context, "Customers Not Added");
		});

	}
}