package com.scleroid.financematic;

import android.support.annotation.NonNull;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.ExpenseCategory;
import com.scleroid.financematic.data.local.model.IdProofType;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.LoanDurationType;
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

	public TempDataFaker() {
	}

	@DebugLog
	void populateData(@NonNull Faker faker) {
		customers = new ArrayList<Customer>();
		loans = new ArrayList<Loan>();
		installments = new ArrayList<Installment>();
		transactions = new ArrayList<TransactionModel>();
		expenses = new ArrayList<>();
		int customerId = faker.number.positive(10000, 99999);
		int accountNo = faker.number.between(1000, 9999);
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

			accountNo = faker.number.between(1000, 9999);

		}

	}

	@NonNull
	Customer createCustomerData(@NonNull Faker faker, int customerId) {

		return new Customer(
				customerId,
				faker.name.name(),
				faker.phoneNumber.phoneNumber(),
				faker.address.streetAddress(),

				(faker.number.hexadecimal(12)) + "",
				IdProofType.AADHAR

		);
	}

	private Loan createLoanData(Faker faker, int customerId, int accountNo) {

		return new Loan(accountNo,
				faker.commerce.price(5000, 1000000),
				faker.date.backward(),
				faker.date.forward(),
				faker.commerce.price(5, 10000),
				faker.commerce.price(0, 2000),
				faker.number.between(1, 20),
				LoanDurationType.MONTHLY,
				faker.commerce.price(6000, 1100000),
				customerId,
				faker.commerce.price(6000, 100000)

		);
	}

	private TransactionModel createTransactionData(Faker faker, int accountNo) {

		return new TransactionModel(
				faker.number.positive(100000, 999999),
				faker.date.backward(),
				faker.commerce.price(),
				faker.commerce.price(),
				faker.commerce.price(0, 2000),
				faker.company.catchPhrase(),
				accountNo

		);
	}

	private Installment createInstallmentData(Faker faker, int accountNo) {

		return new Installment(
				faker.number.positive(100000, 999999),
				faker.date.forward(),
				faker.commerce.price(),
				accountNo
		);
	}

	private Expense createExpenseData(Faker faker) {

		return new Expense(
				faker.commerce.price(),
				ExpenseCategory.PHONE_BILL,
				faker.date.backward()
		);
	}

	@DebugLog
	void saveInDatabase(@NonNull MainActivity mainActivity) {
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