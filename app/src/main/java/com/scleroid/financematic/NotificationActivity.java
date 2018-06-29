package com.scleroid.financematic;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.Session;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.fragments.dashboard.DashboardAdapter;
import com.scleroid.financematic.fragments.dashboard.DashboardFragment;
import com.scleroid.financematic.fragments.dashboard.DashboardViewModel;
import com.scleroid.financematic.fragments.report.ReportFilterType;
import com.scleroid.financematic.fragments.report.ReportFragment;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by scleroid on 16/4/18.
 */


/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 2/3/18
 */


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.Session;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.fragments.report.ReportFilterType;
import com.scleroid.financematic.fragments.report.ReportFragment;
import com.scleroid.financematic.utils.network.Resource;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class NotificationActivity extends BaseFragment<NotificationViewModel>{


		@Inject
		TextViewUtils textViewUtils;
		@Nullable
		@BindView(R.id.total_amount_text_view)
		RupeeTextView totalAmountTextView;
		@Nullable
		@BindView(R.id.remaining_amount_text_view)
		RupeeTextView remainingAmountTextView;
		@Nullable
		@BindView(R.id.lent_amount_text_view)
		RupeeTextView lentAmountTextView;

		@Nullable
		@BindView(R.id.upcoming_events_text_view)
		TextView upcomingEventsTextView;
		@Nullable
		@BindView(R.id.recycler_view_dashboard)
		RecyclerView recyclerViewDashboard;

		@Inject
		ActivityUtils activityUtils;

		@Inject
		LocalCustomerLab localCustomerLab;
		@Inject
		LocalLoanLab localLoanLab;

		@Nullable
		@BindView(R.id.total_amount_title_text_view)
		TextView totalAmountTitleTextView;

		@Nullable
		@BindView(R.id.lent_amount_title_text_view)
		TextView lentAmountTitleTextView;
		@Nullable
		@BindView(R.id.empty_card)
		CardView emptyCard;
		@Inject
		Session session;
		private NotificationAdapter mAdapter;
		private NotificationViewModel notificationViewModel;
		@Nullable
		private List<Installment> installments = new ArrayList<>();
		@Nullable
		private List<Loan> loanList = new ArrayList<>();

		public NotificationActivity() {
			// Required empty public constructor
		}

		@NonNull
		public static NotificationActivity newInstance() {
			NotificationActivity fragment = new NotificationActivity();
			Bundle args = new Bundle();
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			ButterKnife.setDebug(true);
			// Inflate the layout for this fragment
			View rootView = getRootView();

			// recyclerView = rootView.findViewById(R.id.recycler_view_dashboard);11

			// prepareTempDashBoardModelData();


			setupRecyclerView();
			/*textViewUtils.textViewExperiments(upcomingEventsTextView);
			textViewUtils.textViewExperiments(totalAmountTextView);
*/
			setTitle();
			updateView(installments);
			return rootView;
		}

		/**
		 * @return layout resource id
		 */
		@Override
		public int getLayoutId() {
			return R.layout.notification;
		}

		@Override
		protected void subscribeToLiveData() {
			notificationViewModel.getUpcomingInstallments().observe(this,
					items -> {
						updateView(items);
					});

			notificationViewModel.getLoans().observe(this, items -> {
				if (items.data == null) {
					return;
				}
				loanList = items.data;
				NotificationActivity.this.updateUi();
			});
		}

		/**
		 * Override for set view model
		 *
		 * @return view model instance
		 */
		@Override
		public NotificationViewModel getViewModel() {
			notificationViewModel =
					ViewModelProviders.of(NotificationActivity.this, viewModelFactory).get(NotificationViewModel.class);
			return notificationViewModel;
		}

		private void updateUi() {

			/*double totalAmt = calculateTotalAmt(loanList);
			double lentAmt = calculateLentAmt(loanList);
			double receivedAmt = totalAmt - lentAmt;
			Timber.d("Calculating Daashboard " + totalAmt + "  " + lentAmt + "  " + receivedAmt);
			totalAmountTextView.setText(String.valueOf(totalAmt));
			lentAmountTextView.setText(String.valueOf(lentAmt));
			remainingAmountTextView.setText(String.valueOf(receivedAmt));
*/
		}

		/*private double calculateLentAmt(@NonNull final List<Loan> loans) {
			double sum = Stream.of(loans).withoutNulls().mapToDouble(loan ->
					loan.getLoanAmt().doubleValue()).sum();
			Timber.i("sum of Total Amt" + sum);
			return sum;
		}
*/
		private double calculateTotalAmt(@NonNull final List<Loan> loans) {
	/*	int sum = Stream.of(loans).mapToInt(loan ->
				loan.getLoanAmt() != null ? loan.getLoanAmt().intValue() : 0).sum();*/
			double sum = session.getAmount();

		/*//TODO this maybe need to removed
		if (sum == 0) {
			sum = Stream.of(loans).mapToDouble(loan ->
					loan.getLoanAmt() != null ? loan.getLoanAmt().doubleValue() : 0).sum();
		}*/
			Timber.i("sum of Total Amt" + sum);
			return sum;
		}

		private void setupRecyclerView() {
			mAdapter = new NotificationAdapter(new ArrayList<>());

			recyclerViewDashboard.setHasFixedSize(true);


			// vertical RecyclerView
			// keep movie_list_row.xml width to `match_parent`
			RecyclerView.LayoutManager mLayoutManager =
					new LinearLayoutManager(getActivity().getApplicationContext());

			// horizontal RecyclerView
			// keep movie_list_row.xml width to `wrap_content`
			// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
			// (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

			recyclerViewDashboard.setLayoutManager(mLayoutManager);


			recyclerViewDashboard.setItemAnimator(new DefaultItemAnimator());

			recyclerViewDashboard.setAdapter(mAdapter);
			DividerItemDecoration dividerItemDecoration =
					new DividerItemDecoration(recyclerViewDashboard.getContext(),
							DividerItemDecoration.VERTICAL);
			//  recyclerView.addItemDecoration(dividerItemDecoration);


			// row click listener
			// TODO not needed, should be removed recyclerViewDashboard.addOnItemTouchListener
			// (recyclerTouchListener);
		}

		private void setTitle() {
			activityUtils.setTitle((AppCompatActivity) getActivity(), "Notifaction");
		}

		private void updateView(@Nullable final List<Installment> items) {
			if (items == null || items.isEmpty()) {
				emptyCard.setVisibility(View.VISIBLE);
				recyclerViewDashboard.setVisibility(View.GONE);
			} else {
				emptyCard.setVisibility(View.GONE);
				recyclerViewDashboard.setVisibility(View.VISIBLE);
				sort(items);
				mAdapter.setInstallmentList(items);
				installments = items;
			}

		}

		private void sort(@Nullable final List<Installment> transactions) {
			if (transactions == null) return;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				//	transactions.removeIf(transaction -> transaction.getLoan() == null || transaction
				// .getLoan().getCustomer() == null);
				transactions.sort(Comparator.comparing(Installment::getInstallmentDate));
			} else {

				Collections.sort(transactions,
						(m1, m2) -> m1.getInstallmentDate().compareTo(m2.getInstallmentDate()));
			}
		}



		/*private boolean predicate(final Installment next) {
			return next.getLoan() == null || next.getLoan().getCustomer() == null;
		}*/

		/*@OnClick({R.id.total_amount_text_view, R.id.total_amount_title_text_view, R.id
				.remaining_amount_text_view, R.id.lent_amount_text_view, R.id
				.lent_amount_title_text_view, R.id.available_amount_title_text_view})
		public void onViewClicked(@NonNull View view) {
			switch (view.getId()) {
				case R.id.total_amount_text_view:
				case R.id.total_amount_title_text_view:
					activityUtils.loadFragment(
							ReportFragment.newInstance(ReportFilterType.ALL_TRANSACTIONS),
							getFragmentManager());
					break;
				case R.id.remaining_amount_text_view:
				case R.id.available_amount_title_text_view:
					activityUtils.loadFragment(
							ReportFragment.newInstance(ReportFilterType.RECEIVED_AMOUNT),
							getFragmentManager());
					break;
				case R.id.lent_amount_text_view:
				case R.id.lent_amount_title_text_view:
					activityUtils.loadFragment(ReportFragment.newInstance(ReportFilterType
									.LENT_AMOUNT),
							getFragmentManager());
					break;

			}


		}*/

	}
