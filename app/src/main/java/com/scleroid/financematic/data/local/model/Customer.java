package com.scleroid.financematic.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/2/18
 */
@Entity(indices = {@Index(value = "customerId", unique = true)})
public class Customer implements Serializable {


	@Ignore
	List<Loan> loanList;
	@PrimaryKey(autoGenerate = false)
	private int customerId;
	private String name;
	private String mobileNumber;
	private String address;
	private String city;
	private String idProofNo;
	private String idProofType;
	@Ignore
	private List<Loan> loans;

	public Customer(int customerId, String name, String mobileNumber, String address, String city,
	                String idProofNo, String idProofType) {
		this.customerId = customerId;
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.city = city;
		this.idProofNo = idProofNo;
		this.idProofType = idProofType;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public void setLoans(final List<Loan> loans) {
		this.loans = loans;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"customerId=" + customerId +
				", name='" + name + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", idProofNo='" + idProofNo + '\'' +
				", idProofType=" + idProofType +
				'}';
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getIdProofType() {
		return idProofType;
	}

	public void setIdProofType(String idProofType) {

		this.idProofType = idProofType;
	}


}
