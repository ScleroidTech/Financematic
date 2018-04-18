package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.AppExecutors;
import com.scleroid.financematic.utils.Resource;
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
public class DashboardViewModel extends BaseViewModel<Installment> implements CustomerViewModel {
	public static final int RANGE = 100;
	private final CustomerRepo customerRepo;
	private final LoanRepo loanRepo;
	private final InstallmentRepo installmentRepo;

	private final int FILTER_DAYS = 30;
	@Inject
	InstallmentDao installmentDao;
	@Inject
	DateUtils dateUtils;
	@Inject
	AppExecutors appExecutors;

	private LiveData<List<Loan>> loanListLiveData = new MutableLiveData<>();
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
	public DashboardViewModel(CustomerRepo customerRepo, LoanRepo loanRepo,
	                          InstallmentRepo installmentRepo) {

		super();
		this.customerRepo = customerRepo;
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

	public LiveData<List<Loan>> getLoans() {
		if (loanListLiveData.getValue() == null || loanListLiveData.getValue().isEmpty()) {
			loanListLiveData = setLoans(); /*filterResults(installmentRepo
					.getLocalInstallmentsLab().getItems());*/
		}
		return loanListLiveData;
	}

	public LiveData<List<Loan>> setLoans(
	) {
		return
				loanRepo.getLocalLoanLab()
						.getItems();
	}


	//There's a copy of this code in adapter too,
	private List<Installment> filterResults(List<Installment> installments) {

		if (installments == null) return new ArrayList<>();
		return Stream.of(installments)
				.filter(installment -> dateUtils.isThisDateWithinRange(
						installment.getInstallmentDate(), RANGE))
				.collect(Collectors.toList());
	}

	private boolean filterResult(Installment installment) {

		if (installments == null) return false;
		return dateUtils.isThisDateWithinRange(
				installment.getInstallmentDate(), RANGE);

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
		/*loansLive = Transformations.map(loansLive, new Function<List<Customer>, List<Customer>>
		() {

			@Override
			public List<Customer> apply(final List<Customer> inputStates) {
               *//* for (Customer state : inputStates) {
                    state.setLoans(dao.getLoans(state.getCustomerId()));
                }*//*
				return inputStates;
			}
		});
		return loansLive;*/
	}

	/* public LiveData<List<DashboardViewModel>> getTransformedUpcomingData() {
	 *//* new MediatorLiveData<List<DashBoardModel>>();
        LiveData<List<DashBoardModel>> dashBoardLiveData;
        dashBoardLiveData = Transformations.switchMap(getUpcomingInstallments(),
        (List<Installment> input) -> {
            MediatorLiveData<List<DashBoardModel>> data = new MediatorLiveData<>();
            List<DashBoardModel> dash = new ArrayList<>();
            for (Installment installment : input) {
                DashBoardModel dashBoardModel;
                installment = loadInstallments(installment);
                int loanAcNo = installment.getLoanAcNo();

                //TODO remove getLocalLoanLAb from here
                Loan loan =  ;
                if (loan == null) continue;
                Timber.d(loan.toString());
                int custId = loan.getCustId();
	            //TODO remove getLocalCstLAb from her
                Customer customer = customerRepo.getLocalCustomerLab().getItem(
                        custId).getValue();
               // if (customer == null) continue;
                Timber.d(customer.toString());
                String customerName = customer.getName();
                dashBoardModel =
                        new DashBoardModel(custId, loanAcNo, installment.getInstallmentId(),
                                customerName, customer.getMobileNumber(),
                                installment.getExpectedAmt(), installment.getInstallmentDate());
                dash.add(dashBoardModel);
            }
            data.setValue(dash);
            return data;
        });
        return dashBoardLiveData;*//*
    }
	public LiveData<Installment> loadInstallments(Installment installment) {
		//LiveData<Installment> quotationLiveData = get
		LiveData<Installment> result =
				Transformations.switchMap(installment, quotation -> {
					MutableLiveData<Installment> mutableResult = new MutableLiveData<>();
					appExecutors.diskIO().execute(() -> {
						installment.setLoan(loanRepo.getLocalLoanLab().getRxItem(installment
						.getLoanAcNo()));
						mutableResult.postValue(quotation);
					});
					return mutableResult;
				});
		return result;
	}
*/
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
