package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.scleroid.financematic.Resource;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.viewmodels.CustomerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

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
    @Inject
    DateUtils dateUtils;
    private LiveData<Resource<List<Installment>>> installments;
    private LiveData<List<DashBoardModel>> upcomingInstallmentsTransformed;
    private MutableLiveData<List<Installment>> upcomingInstallments = new MutableLiveData<>();

    @Inject
    public DashboardViewModel(CustomerRepo customerRepo, LoanRepo loanRepo, InstallmentRepo installmentRepo) {
        this.customerRepo = customerRepo;
        this.loanRepo = loanRepo;
        this.installmentRepo = installmentRepo;

        installments = installmentRepo.loadItems();
//        Timber.d(installments.getValue().data.isEmpty() + "" );
        upcomingInstallments.setValue(filterResults());
        //   Timber.d(upcomingInstallments.getValue().isEmpty() + "" );
        setUpcomingInstallmentsTransformed(getTransformedUpcomingData());
    }

    private List<Installment> filterResults() {

        if ((getItemList().getValue()).data == null) return new ArrayList<>();
        return Stream.of((getItemList().getValue().data))
                .filter(installments -> dateUtils.isThisDateWithinAWeek(installments.getInstallmentDate()))
                .collect(Collectors.toList());
    }

    public LiveData<List<DashBoardModel>> getTransformedUpcomingData() {
        new MediatorLiveData<List<DashBoardModel>>();
        LiveData<List<DashBoardModel>> dashBoardLiveData;
        dashBoardLiveData = Transformations.switchMap(upcomingInstallments, (List<Installment> input) -> {
            MediatorLiveData<List<DashBoardModel>> data = new MediatorLiveData<>();
            List<DashBoardModel> dash = new ArrayList<>();
            for (Installment installment : input) {
                DashBoardModel dashBoardModel;
                int loanAcNo = installment.getLoanAcNo();
                final Loan loan = loanRepo.loadItem(loanAcNo).getValue().data;
                Timber.d(loan.toString());
                int custId = loan.getCustId();
                Customer customer = customerRepo.loadItem(
                        custId).getValue().data;
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
        return dashBoardLiveData;
    }

    @Override
    protected LiveData<Resource<List<Installment>>> updateItemLiveData() {
        return null;
    }

    @Override
    protected LiveData<Resource<List<Installment>>> getItemList() {
        return installments;
    }

    public MutableLiveData<List<Installment>> getUpcomingInstallments() {
        return upcomingInstallments;
    }

    public void setUpcomingInstallments(final MutableLiveData<List<Installment>> upcomingInstallments) {
        this.upcomingInstallments = upcomingInstallments;
    }

    public LiveData<List<DashBoardModel>> getUpcomingInstallmentsTransformed() {
        return upcomingInstallmentsTransformed;
    }

    public void setUpcomingInstallmentsTransformed(final LiveData<List<DashBoardModel>> upcomingInstallmentsTransformed) {
        this.upcomingInstallmentsTransformed = upcomingInstallmentsTransformed;
    }
}
