package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
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


public class DashboardFragment extends BaseFragment<DashboardViewModel> {

	@Inject
	TextViewUtils textViewUtils;
	@BindView(R.id.total_amount_text_view)
	RupeeTextView totalAmountTextView;
	@BindView(R.id.remaining_amount_text_view)
	RupeeTextView remainingAmountTextView;
	@BindView(R.id.lent_amount_text_view)
	RupeeTextView lentAmountTextView;

	@BindView(R.id.upcoming_events_text_view)
	TextView upcomingEventsTextView;
	@BindView(R.id.recycler_view_dashboard)
	RecyclerView recyclerViewDashboard;

	@Inject
	ActivityUtils activityUtils;

	@Inject
	LocalCustomerLab localCustomerLab;
	@Inject
	LocalLoanLab localLoanLab;

	@BindView(R.id.total_amount_title_text_view)
	TextView totalAmountTitleTextView;

	@BindView(R.id.lent_amount_title_text_view)
	TextView lentAmountTitleTextView;

	private DashboardAdapter mAdapter;
	private DashboardViewModel dashBoardViewModel;
	private List<Installment> installments = new ArrayList<>();
	private List<Loan> loanList = new ArrayList<>();

	public DashboardFragment() {
		// Required empty public constructor
	}

	public static DashboardFragment newInstance(String param1, String param2) {
		DashboardFragment fragment = new DashboardFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@BindView(R.id.empty_card)
	CardView emptyCard;

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(), "DashBoard");
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_dashboard;
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
		textViewUtils.textViewExperiments(upcomingEventsTextView);
		textViewUtils.textViewExperiments(totalAmountTextView);

		setTitle();
		updateView(installments);
		return rootView;
	}
	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public DashboardViewModel getViewModel() {
		dashBoardViewModel =
				ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
		return dashBoardViewModel;
	}

	private void setupRecyclerView() {
		mAdapter = new DashboardAdapter(new ArrayList<>());

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

	@Override
	protected void subscribeToLiveData() {
		dashBoardViewModel.getUpcomingInstallments().observe(this,
				items -> {
					updateView(items);
				});

		dashBoardViewModel.getLoans().observe(this, items -> {
			loanList = items;
			updateUi();
		});
	}

	private void updateView(final List<Installment> items) {
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

	private void updateUi() {
		int receivedAmt = calculateReceivedAmt(loanList);
		int totalAmt = calculateTotalAmt(loanList);
		int lentAmt = totalAmt - receivedAmt;

		totalAmountTextView.setText(String.valueOf(totalAmt));
		lentAmountTextView.setText(String.valueOf(lentAmt));
		remainingAmountTextView.setText(String.valueOf(receivedAmt));

	}

	private void sort(final List<Installment> transactions) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//	transactions.removeIf(transaction -> transaction.getLoan() == null || transaction
			// .getLoan().getCustomer() == null);
			transactions.sort(Comparator.comparing(Installment::getInstallmentDate));
		} else {

			Collections.sort(transactions,
					(m1, m2) -> m1.getInstallmentDate().compareTo(m2.getInstallmentDate()));
		}
	}

	private boolean predicate(final Installment next) {
		return next.getLoan() == null || next.getLoan().getCustomer() == null;
	}

	private int calculateTotalAmt(final List<Loan> loans) {
		int sum = Stream.of(loans).mapToInt(loan ->
				loan.getLoanAmt() != null ? loan.getLoanAmt().intValue() : 0).sum();
		Timber.wtf("sum of Total Amt" + sum);
		return sum;
	}

	private int calculateReceivedAmt(final List<Loan> loans) {

		int sum = Stream.of(loans).withoutNulls().mapToInt(loan ->
				loan.getReceivedAmt() != null ? loan.getReceivedAmt().intValue() : 0).sum();
		Timber.wtf("sum of received Amt" + sum);
		return sum;
	}

	@OnClick({R.id.total_amount_text_view, R.id.total_amount_title_text_view, R.id
			.remaining_amount_text_view, R.id.lent_amount_text_view, R.id
			.lent_amount_title_text_view, R.id.available_amount_title_text_view})
	public void onViewClicked(View view) {
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


	}

}
