package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.scleroid.financematic.Resource;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.DateUtils;
import com.scleroid.financematic.viewmodels.CustomerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        installments = installmentRepo.loadInstallments();
        upcomingInstallments.setValue(filterResults());
        setUpcomingInstallmentsTransformed(getTransformedUpcomingData());


    }

    private List<Installment> filterResults() {
        // if (Objects.requireNonNull(getItemList().getValue()).data == null) return new ArrayList<>();
        return Stream.of(Objects.requireNonNull(getItemList().getValue().data))
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
                String customerName = customerRepo.getCustomerDao().getCustomer(loanRepo.getLoanDao().getLoan(installment.getLoanAcNo()).getCustId()).getName();
                dashBoardModel = new DashBoardModel(customerName, installment.getExpectedAmt(), installment.getInstallmentDate());
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
