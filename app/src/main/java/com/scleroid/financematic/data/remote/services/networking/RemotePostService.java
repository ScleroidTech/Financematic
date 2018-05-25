package com.scleroid.financematic.data.remote.services.networking;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.remote.RemotePostEndpoint;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;
import timber.log.Timber;

public final class RemotePostService {

	private static RemotePostService instance;

	@Inject
	RemotePostEndpoint service;

	@Inject
	public RemotePostService(
			final RemotePostEndpoint service) {
		this.service = service;
	}

	/*	public RemotePostService() {

		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(loggingInterceptor)
				.build();

		Gson gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.create();

		retrofit = new Retrofit.Builder()
				.client(httpClient)
				.baseUrl(BuildConfig.API_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
	}*/

	/*public static synchronized RemotePostService getInstance() {
		if (instance == null) {
			instance = new RemotePostService();
		}
		return instance;
	}*/

	public void addLoan(Loan loan) throws IOException, RemoteException {
		//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

		// Remote call can be executed synchronously since the job calling it is already
		// backgrounded.
		Response<Loan> response = service.addLoan(loan).execute();

		if (response == null || !response.isSuccessful() || response.errorBody() != null) {
			throw new RemoteException(response);
		}

		Timber.d("successful remote response: " + response.body());
	}

	public void addTransactionModel(TransactionModel transaction) throws IOException,
			RemoteException {
		//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

		// Remote call can be executed synchronously since the job calling it is already
		// backgrounded.
		Response<TransactionModel> response = service.addTransaction(transaction).execute();

		if (response == null || !response.isSuccessful() || response.errorBody() != null) {
			throw new RemoteException(response);
		}

		Timber.d("successful remote response: " + response.body());
	}

	public void addInstallment(Installment installment) throws IOException, RemoteException {
		//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

		// Remote call can be executed synchronously since the job calling it is already
		// backgrounded.
		Response<Installment> response = service.addInstallment(installment).execute();

		if (response == null || !response.isSuccessful() || response.errorBody() != null) {
			throw new RemoteException(response);
		}

		Timber.d("successful remote response: " + response.body());
	}

	public void addExpense(Expense expense) throws IOException, RemoteException {
		//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

		// Remote call can be executed synchronously since the job calling it is already
		// backgrounded.
		Response<Expense> response = service.addExpense(expense).execute();

		if (response == null || !response.isSuccessful() || response.errorBody() != null) {
			throw new RemoteException(response);
		}

		Timber.d("successful remote response: " + response.body());
	}

	public void addCustomer(Customer customer) throws IOException, RemoteException {
		//	RemotePostEndpoint service = retrofit.create(RemotePostEndpoint.class);

		// Remote call can be executed synchronously since the job calling it is already
		// backgrounded.
		Response<Customer> response = service.addCustomer(customer).execute();

		if (response == null || !response.isSuccessful() || response.errorBody() != null) {
			throw new RemoteException(response);
		}

		Timber.d("successful remote response: " + response.body());
	}


}
