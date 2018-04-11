package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.dao.InstallmentDao;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.AppExecutors;
import com.scleroid.financematic.utils.Resource;
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
    public DashboardViewModel(CustomerRepo customerRepo, LoanRepo loanRepo, InstallmentRepo installmentRepo) {

	    super();
        this.customerRepo = customerRepo;
        this.loanRepo = loanRepo;
        this.installmentRepo = installmentRepo;

	    // installments = ;
//        Timber.d(installments.getValue().data.isEmpty() + "" );
	    upcomingInstallments = getUpcomingInstallments();
        //   Timber.d(upcomingInstallments.getValue().isEmpty() + "" );
	    //   setUpcomingInstallmentsTransformed(getTransformedUpcomingData());
    }

	//TODO make it MutableLiveData
	public LiveData<List<Installment>> getUpcomingInstallments() {
		if (upcomingInstallments.getValue() == null || upcomingInstallments.getValue().isEmpty()) {
			upcomingInstallments = installmentRepo.getLocalInstallmentsLab()
					.getInstallmentWithCustomers(); /*filterResults(installmentRepo
					.getLocalInstallmentsLab().getItems());*/
		}
		return upcomingInstallments;
	}

	@Override
	protected LiveData<Resource<List<Installment>>> getItemList() {
		return installments;
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
        return null;
    }

    public void setUpcomingInstallments(final MutableLiveData<List<Installment>> upcomingInstallments) {
        this.upcomingInstallments = upcomingInstallments;
    }

}
