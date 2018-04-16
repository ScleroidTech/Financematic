package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.fragments.report.ReportFilterType;
import com.scleroid.financematic.fragments.report.ReportFragment;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DashboardFragment extends BaseFragment<DashboardViewModel> {

	@Inject
	TextViewUtils textViewUtils;
	TextView firstFragment;
	@BindView(R.id.total_amount_text_view)
	TextView totalAmountTextView;
	@BindView(R.id.remaining_amount_text_view)
	TextView remainingAmountTextView;
	@BindView(R.id.lent_amount_text_view)
	TextView lentAmountTextView;
	@BindView(R.id.available_amount_text_view)
	TextView availableAmountTextView;
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


		return rootView;
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_dashboard;
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
		mAdapter = new DashboardAdapter(new ArrayList<>(), localLoanLab, localCustomerLab);

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
					mAdapter.setInstallmentList(items);
					installments = items;
				});
	}


	@OnClick({R.id.total_amount_text_view, R.id.total_amount_title_text_view, R.id
			.remaining_amount_text_view, R.id.lent_amount_text_view, R.id
			.lent_amount_title_text_view, R.id.available_amount_text_view})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.total_amount_text_view:
			case R.id.total_amount_title_text_view:
				activityUtils.loadFragment(
						ReportFragment.newInstance(ReportFilterType.ALL_TRANSACTIONS),
						getFragmentManager());
				break;
			case R.id.remaining_amount_text_view:
			case R.id.available_amount_text_view:
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
