package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
@Entity(indices = {@Index(value = "customerId", unique = true)})
public class Customer {
    @Ignore
    public static final byte AADHAR = 0;
    @Ignore
    public static final byte PAN = 1;
    @Ignore
    public static final byte PASSPORT = 2;
    @Ignore
    public static final byte RATION_CARD = 3;
    @Ignore
    public static final byte VOTER_ID = 4;
    @Ignore
    public static final byte SEVEN_TWELVE_CERTIFICATE = 5;


    @PrimaryKey(autoGenerate = false)
    private int customerId;
    private String name;
    private String mobileNumber;
    private String address;
    private String city;
    private String idProofNo;
    private byte idProofType;
    @Ignore
    private List<Loan> loans;

    public Customer(int customerId, String name, String mobileNumber, String address, String city, String idProofNo, byte idProofType) {
        this.customerId = customerId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.city = city;
        this.idProofNo = idProofNo;
        this.idProofType = idProofType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdProofNo() {
        return idProofNo;
    }

    public void setIdProofNo(String idProofNo) {
        this.idProofNo = idProofNo;
    }

    public byte getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(byte idProofType) {
        if (idProofType > 6 && idProofType < 0) {
            throw new IllegalStateException("The value is not permitted");
        }
        this.idProofType = idProofType;
    }


}
