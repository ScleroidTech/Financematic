package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.multithread.AppExecutors;
import com.scleroid.financematic.utils.network.Resource;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.viewmodels.CustomerViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class DashboardViewModel extends BaseViewModel<Installment> implements CustomerViewModel {
	public static final int FILTER_DAYS = 100;
	private final LoanRepo loanRepo;
	private final InstallmentRepo installmentRepo;

	@Inject
	InstallmentDao installmentDao;
	@Inject
	DateUtils dateUtils;
	@Inject
	AppExecutors appExecutors;

	private LiveData<Resource<List<Loan>>> loanListLiveData = new MutableLiveData<>();
	private LiveData<Resource<List<Installment>>> installments;
	private LiveData<List<Installment>> upcomingInstallments = new MutableLiveData<>();


	@Inject
	public DashboardViewModel(LoanRepo loanRepo,
	                          InstallmentRepo installmentRepo) {

		super();
		this.loanRepo = loanRepo;
		this.installmentRepo = installmentRepo;


		upcomingInstallments = getUpcomingInstallments();
		loanListLiveData = getLoans();
	}


	public LiveData<List<Installment>> getUpcomingInstallments() {
		if (upcomingInstallments.getValue() == null || upcomingInstallments.getValue().isEmpty()) {
			upcomingInstallments = setUpcomingInstallments();
		}
		return upcomingInstallments;
	}

	public LiveData<List<Installment>> setUpcomingInstallments() {
		upcomingInstallments = installmentRepo.getLocalInstallmentsLab()
				.getInstallmentWithCustomers();
		return upcomingInstallments;
	}

	public LiveData<Resource<List<Loan>>> getLoans() {
		if (loanListLiveData.getValue() == null || loanListLiveData.getValue().data == null ||
				loanListLiveData
						.getValue().data.isEmpty()) {
			loanListLiveData = setLoans();
		}
		return loanListLiveData;
	}

	public LiveData<Resource<List<Loan>>> setLoans(
	) {
		return
				loanRepo.loadItems();
	}



	@Nullable
	@Override
	protected LiveData<Resource<List<Installment>>> updateItemLiveData() {
		return null;
	}

	@Override
	protected LiveData<Resource<List<Installment>>> getItemList() {
		return installments;
	}

}
