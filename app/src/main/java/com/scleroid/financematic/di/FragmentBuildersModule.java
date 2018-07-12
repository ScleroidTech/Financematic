package com.scleroid.financematic.di;

import android.support.annotation.NonNull;

import com.scleroid.financematic.NotificationActivity;
import com.scleroid.financematic.fragments.AddMoneyFragment;
import com.scleroid.financematic.fragments.RegisterCustomerFragment;
import com.scleroid.financematic.fragments.RegisterLoanFragment;
import com.scleroid.financematic.fragments.ReminderFragment;
import com.scleroid.financematic.fragments.customer.CustomerFragment;
import com.scleroid.financematic.fragments.dashboard.DashboardFragment;
import com.scleroid.financematic.fragments.dialogs.DatePickerDialogFragment;
import com.scleroid.financematic.fragments.dialogs.DelayDialogFragment;
import com.scleroid.financematic.fragments.dialogs.RegisterReceivedDialogFragment;
import com.scleroid.financematic.fragments.expense.ExpenseFragment;
import com.scleroid.financematic.fragments.loanDetails.LoanDetailsFragment;
import com.scleroid.financematic.fragments.passbook.PassbookFragment;
import com.scleroid.financematic.fragments.people.PeopleFragment;
import com.scleroid.financematic.fragments.report.ReportFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/6/18
 */
@Module
public abstract class FragmentBuildersModule {
	@NonNull
	@ContributesAndroidInjector
	abstract DashboardFragment contributeDashboardFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract CustomerFragment contributeCustomerFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract ExpenseFragment contributeExpenseFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract LoanDetailsFragment contributeLoanDetailsFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract PassbookFragment contributePassbookFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract PeopleFragment contributePeopleFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract RegisterCustomerFragment contributeRegisterCustomerFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract RegisterLoanFragment contributeRegisterMoneyFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract RegisterReceivedDialogFragment contributeRegisterReceivedFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract ReminderFragment contributeReminderFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract ReportFragment contributeReportFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract NotificationActivity contributeNotification();

	@NonNull
	@ContributesAndroidInjector
	abstract DelayDialogFragment contributeDelayDialog();

	@NonNull
	@ContributesAndroidInjector
	abstract DatePickerDialogFragment contributeDatePickerDialogFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract com.scleroid.financematic.fragments.dialogs.InsertExpenseDialogFragment
	contributeInserDialogFragment();

	@NonNull
	@ContributesAndroidInjector
	abstract AddMoneyFragment
	contributeAddMoneyFragment();


}
