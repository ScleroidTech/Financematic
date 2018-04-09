package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Customer;

import java.util.List;


public class CustomerViewModel extends BaseViewModel {

    private List<Customer> customers;

    //    private LiveData<List<Parcel>> parcelList;
    private LiveData<List<Customer>> customerList;


    public CustomerViewModel(@NonNull Application database) {


        //      parcelList = updateParcelLiveData();
        customerList = updateCustomerLiveData();
    }

/*Possible duplicate code    private LiveData<List<Parcel>> updateParcelLiveData() {

        LiveData<List<Parcel>> parcelList = appDatabase.parcelDao().getAllParcelsLive();
        return parcelList;
    }

    public LiveData<List<Parcel>> getParcelList() {
        if (parcelList == null) {
            parcelList = updateParcelLiveData();
        }

        return parcelList;
    }*/

    private LiveData<List<Customer>> updateCustomerLiveData() {
        //TODo     LiveData<List<Customer>> customerList = appDatabase.customerDao().getAllCustomerLive();
        return customerList;
    }

    public LiveData<List<Customer>> getCustomerList() {
        if (customerList == null) {
            customerList = updateCustomerLiveData();
        }

        return customerList;
    }

  /*  public void editItem(Customer customer) {
        CustomerLab.updateCustomer(customer, appDatabase);
    }*/
}