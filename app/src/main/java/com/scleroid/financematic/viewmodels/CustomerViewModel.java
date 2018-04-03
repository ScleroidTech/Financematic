package com.scleroid.financematic.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scleroid.financematic.AppDatabase;
import com.scleroid.financematic.model.local.Customer;

import java.util.List;


public class CustomerViewModel extends AndroidViewModel {

    private List<Customer> customers;

    //    private LiveData<List<Parcel>> parcelList;
    private LiveData<List<Customer>> customerList;

    private AppDatabase appDatabase;

    public CustomerViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
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
        LiveData<List<Customer>> customerList = appDatabase.customerDao().getAllCustomerLive();
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