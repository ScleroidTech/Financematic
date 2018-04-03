package com.scleroid.financematic.data.local;

import com.scleroid.financematic.AppDatabase;
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
    private CustomerDao dao;
    private LoanDao loanDao;

    @Inject
    public DatabaseHelperCustomer(AppDatabase database) {
        dao = database.customerDao();
        loanDao = database.loanDao();
    }

    public Customer getCustomer(int id) {
        Customer customer = dao.getCustomer(id);
        customer.setLoans(dao.getLoans(id));
        return customer;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = dao.getCustomers();
        for (Customer customer : customers) {
            customer.setLoans(dao.getLoans(customer.getCustomerId()));
        }
        return customers;
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
