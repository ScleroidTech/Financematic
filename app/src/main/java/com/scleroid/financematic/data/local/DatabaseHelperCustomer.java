package com.scleroid.financematic.data.local;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.scleroid.financematic.data.local.dao.CustomerDao;
import com.scleroid.financematic.data.local.dao.LoanDao;
import com.scleroid.financematic.data.local.model.Customer;

import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/3/18
 */
public class DatabaseHelperCustomer {

    //TODO to use or not to use this
    private CustomerDao dao;
    private LoanDao loanDao;

    @Inject
    public DatabaseHelperCustomer(AppDatabase database) {
        dao = database.customerDao();
        loanDao = database.loanDao();
    }

    /* Adding a livedata implementation of the same method
        Things going fun bitch
     public Customer getCustomer(int id) {
        Customer customer = dao.getCustomer(id);
        customer.setLoans(dao.getLoans(id));
        return customer;
    }
    */

    public LiveData<Customer> getCustomer(int id) {
        LiveData<Customer> customerLiveData = dao.getCustomerLive(id);
        /*customerLiveData = Transformations.switchMap(customerLiveData, inputCustomer -> {
            LiveData<List<Loan>> loanLiveData = loanDao.getLoansLive(inputCustomer.getCustomerId());
            LiveData<Customer> outputLiveData = Transformations.map(loanLiveData, input -> {
                inputCustomer.setLoans(input);
                return inputCustomer;
            });
            return outputLiveData;
        });*/
        return customerLiveData;
        //Good Job buddy, now the real challenge is next method
    }


    /* Do whatever I did for previous method, just in the list of list
     public List<Customer> getCustomers() {
         List<Customer> customers = dao.getCustomers();
         for (Customer customer : customers) {
             customer.setLoans(dao.getLoans(customer.getCustomerId()));
         }
         return customers;
     }*/
    public LiveData<List<Customer>> getCustomers() {
        LiveData<List<Customer>> customerLiveData = dao.getAllCustomerLive();

       /* TODO Test this, if works remove below code, this part has performance issues
       customerLiveData = Transformations.switchMap(customerLiveData, inputCustomers -> {
            MediatorLiveData<List<Customer>> customerMediatorLiveData = new MediatorLiveData<>();
            for (Customer customer : inputCustomers) {
                customerMediatorLiveData.addSource(dao.getLoansLive(customer.getCustomerId()), loans -> {
                    customer.setLoans(loans);
                    customerMediatorLiveData.postValue(inputCustomers);

                });
            }
            return customerMediatorLiveData;
        });
        return customerLiveData;*/
        customerLiveData = Transformations.map(customerLiveData, new Function<List<Customer>, List<Customer>>() {

            @Override
            public List<Customer> apply(final List<Customer> inputStates) {
               /* for (Customer state : inputStates) {
                    state.setLoans(dao.getLoans(state.getCustomerId()));
                }*/
                return inputStates;
            }
        });
        return customerLiveData;
    }

    public void saveCustomer(Customer customer) {
        dao.saveCustomer(customer);
        loanDao.saveLoans(customer.getLoans());
    }

    public void saveCustomers(List<Customer> customers) {
        dao.saveCustomers(customers);
        for (Customer customer : customers) {
            loanDao.saveLoans(customer.getLoans());
        }
    }

}
