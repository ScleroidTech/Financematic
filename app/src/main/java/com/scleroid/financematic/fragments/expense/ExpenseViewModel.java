package com.scleroid.financematic.fragments.expense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.repo.ExpenseRepo;
import com.scleroid.financematic.utils.network.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/9/18
 */
public class ExpenseViewModel extends BaseViewModel<Expense>
		implements com.scleroid.financematic.viewmodels.ExpenseViewModel {
	private final ExpenseRepo expenseRepo;

	private LiveData<Resource<List<Expense>>> expenseList = new MutableLiveData<>();

	@Inject
	public ExpenseViewModel(final ExpenseRepo expenseRepo) {
		this.expenseRepo = expenseRepo;

		expenseList = getItemList();
	}

	@Override
	protected LiveData<Resource<List<Expense>>> updateItemLiveData() {


		return expenseRepo.loadItems();
	}

	@Override
	protected LiveData<Resource<List<Expense>>> getItemList() {
		if (expenseList.getValue() == null || expenseList.getValue().data == null || expenseList
				.getValue().data
				.isEmpty()) {
			expenseList = updateItemLiveData();
		}
		return expenseList;
	}
}
