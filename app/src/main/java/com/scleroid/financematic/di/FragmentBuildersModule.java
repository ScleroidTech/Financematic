package com.scleroid.financematic.di;

import com.scleroid.financematic.Notification;
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
	@ContributesAndroidInjector
	abstract DashboardFragment contributeDashboardFragment();

	@ContributesAndroidInjector
	abstract CustomerFragment contributeCustomerFragment();

	@ContributesAndroidInjector
	abstract ExpenseFragment contributeExpenseFragment();

	@ContributesAndroidInjector
	abstract LoanDetailsFragment contributeLoanDetailsFragment();

	@ContributesAndroidInjector
	abstract PassbookFragment contributePassbookFragment();

	@ContributesAndroidInjector
	abstract PeopleFragment contributePeopleFragment();

	@ContributesAndroidInjector
	abstract RegisterCustomerFragment contributeRegisterCustomerFragment();

	@ContributesAndroidInjector
	abstract RegisterLoanFragment contributeRegisterMoneyFragment();

	@ContributesAndroidInjector
	abstract RegisterReceivedDialogFragment contributeRegisterReceivedFragment();

	@ContributesAndroidInjector
	abstract ReminderFragment contributeReminderFragment();

	@ContributesAndroidInjector
	abstract ReportFragment contributeReportFragment();

	@ContributesAndroidInjector
	abstract Notification contributeNotification();

	@ContributesAndroidInjector
	abstract DelayDialogFragment contributeDelayDialog();

	@ContributesAndroidInjector
	abstract DatePickerDialogFragment contributeDatePickerDialogFragment();

	@ContributesAndroidInjector
	abstract com.scleroid.financematic.fragments.dialogs.InsertExpenseDialogFragment
	contributeInserDialogFragment();


}
