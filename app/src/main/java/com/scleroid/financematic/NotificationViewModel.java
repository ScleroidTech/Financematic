package com.scleroid.financematic;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class NotificationViewModel extends BaseViewModel<Installment> implements CustomerViewModel {
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
	// private LiveData<List<DashBoardModel>> upcomingInstallmentsTransformed;
	private LiveData<List<Installment>> upcomingInstallments = new MutableLiveData<>();

    /*TODO temporarily removed
    private MutableLiveData<List<Installment>> filterResults(
		    final LiveData<List<Installment>> data) {
		if (data == null ) Timber.wtf("The list is empty");
         List<Installment> list = Stream.of(data.getValue())
                .filter(installments -> dateUtils.isThisDateWithinRange(installments
                .getInstallmentDate(), FILTER_DAYS))
                .collect(Collectors.toList());
	    MutableLiveData<List<Installment>> mutableLiveData=  new MutableLiveData<>();
	    mutableLiveData.setValue(list);
        return mutableLiveData;
    }
*/

	@Inject
	public NotificationViewModel(LoanRepo loanRepo,
	                             InstallmentRepo installmentRepo) {

		super();
		this.loanRepo = loanRepo;
		this.installmentRepo = installmentRepo;

		// installments = ;
//        Timber.d(installments.getValue().data.isEmpty() + "" );
		upcomingInstallments = getUpcomingInstallments();
		//   Timber.d(upcomingInstallments.getValue().isEmpty() + "" );
		//   setUpcomingInstallmentsTransformed(getTransformedUpcomingData());
		loanListLiveData = getLoans();
	}


	public LiveData<List<Installment>> getUpcomingInstallments() {
		if (upcomingInstallments.getValue() == null || upcomingInstallments.getValue().isEmpty()) {
			upcomingInstallments = setUpcomingInstallments();  /*filterResults(installmentRepo
					.getLocalInstallmentsLab().getItems());*/
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
			loanListLiveData = setLoans(); /*filterResults(installmentRepo
					.getLocalInstallmentsLab().getItems());*/
		}
		return loanListLiveData;
	}

	public LiveData<Resource<List<Loan>>> setLoans(
	) {
		return
				loanRepo.loadItems();
	}


	//There's a copy of this code in adapter too,
	private List<Installment> filterResults(@Nullable List<Installment> installments) {

		if (installments == null) return new ArrayList<>();
		return Stream.of(installments)
				.filter(installment -> dateUtils.isThisDateWithinRange(
						installment.getInstallmentDate(), FILTER_DAYS))
				.collect(Collectors.toList());
	}

	private boolean filterResult(@NonNull Installment installment) {

		if (installments == null) return false;
		return dateUtils.isThisDateWithinRange(
				installment.getInstallmentDate(), FILTER_DAYS);

	}

	//TODO doesnt work
	public LiveData<List<Installment>> getFilteredResult() {
		LiveData<List<Installment>> installmentsLive = getUpcomingInstallments();

		// TODO Test this, if works remove below code, this part has performance issues
		final LiveData<List<Installment>> finalInstallmentsLive = installmentsLive;
		installmentsLive = Transformations.switchMap(installmentsLive,
				(List<Installment> inputInstallment) -> {
					MediatorLiveData<List<Installment>> installmentMediatorLiveData =
							new MediatorLiveData<>();
					final List<Installment> install = new ArrayList<>();

					installmentMediatorLiveData.postValue(install);
					return installmentMediatorLiveData;
				});
		return installmentsLive;

	}

	@Nullable
	@Override
	protected LiveData<Resource<List<Installment>>> updateItemLiveData() {
		//TODO implement this
		return null;
	}

	@Override
	protected LiveData<Resource<List<Installment>>> getItemList() {
		return installments;
	}

}
