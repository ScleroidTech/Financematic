package com.scleroid.financematic.fragments.report;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.fragments.DatePickerFragment;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 2/3/18
 */

public class ReportFragment extends BaseFragment<ReportViewModel> {
	private static final String DIALOG_DATE = "DIALOG_DATE";
	private static final int REQUEST_DATE_FROM = 1;
	private static final int REQUEST_DATE_TO = 2;

	@Inject
	DateUtils dateUtils;

	Calendar myCalendar = Calendar.getInstance();
	Calendar myCalendar1 = Calendar.getInstance();

	String[] filterSuggestions =
			{"All Amount", "Received Amount", "Lent Amount", "Earned Amount"};
	Spinner spin;
	@BindView(R.id.from_date_text_view)
	TextView fromDateTextView;
	@BindView(R.id.to_date_text_view)
	TextView toDateTextView;

	@BindView(R.id.report_recycler_view)
	RecyclerView reportRecyclerView;
	@BindView(R.id.spinnerr)
	Spinner spinnerFilter;
	@BindView(R.id.balanceAmt)
	TextView reportBalance;
	ActivityUtils activityUtils = new ActivityUtils();
	private List<TransactionModel> transactionsList = new ArrayList<>();
	private ReportAdapter mAdapter;

	private ReportViewModel reportViewModel;
	private Date startDate;
	private Date endDate;
	private List<TransactionModel> filteredList;

	public ReportFragment() {
		// Required empty public constructor
	}

	public static ReportFragment newInstance(String param1, String param2) {
		ReportFragment fragment = new ReportFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_DATE_FROM) {
			startDate = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			fromDateTextView.setText(dateUtils.getFormattedDate(startDate));
		} else if (requestCode == REQUEST_DATE_TO) {
			endDate = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			toDateTextView.setText(dateUtils.getFormattedDate(endDate));
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View rootView = getRootView();


		mAdapter = new ReportAdapter();

		reportRecyclerView.setHasFixedSize(true);

		/* reportRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext())
		);*/

		// vertical RecyclerView
		// keep movie_list_row.xml width to `match_parent`
		setupRecyclerView();
		setupSpinner();
		return rootView;


	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_report;
	}

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {
		reportViewModel.getTransactionLiveData().observe(this, transactions -> {
			updateListData(transactions);
		});
	}

	private void updateListData(final List<TransactionModel> transactions) {
		transactionsList = transactions;
		mAdapter.setReportList(transactionsList);
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public ReportViewModel getViewModel() {
		reportViewModel =
				ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory)
						.get(ReportViewModel.class);
		return reportViewModel;
	}

	private void setupSpinner() {
		ArrayAdapter<? extends String> spinnerList =
				new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
						android.R.layout.simple_spinner_item, filterSuggestions);
		spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {

				if (startDate == null && endDate == null) {
					filterWithoutDate(getSuggestion(position));
				}
				filterWithDate(startDate, endDate, filterSuggestions[position]);
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});

		spinnerFilter.setAdapter(spinnerList);

	}

	private ReportFilterType getSuggestion(final int filterSuggestion) {
		switch (filterSuggestion) {
			case 0:
				return ReportFilterType.ALL_TRANSACTIONS;
			case 1:
				return ReportFilterType.RECEIVED_AMOUNT;
			case 2:
				return ReportFilterType.LENT_AMOUNT;
			case 3:
				return ReportFilterType.EARNED_AMOUNT;
			default:
				return ReportFilterType.ALL_TRANSACTIONS;

		}

	}

	private void filterWithDate(final Date startDate, final Date endDate,
	                            final String filterSuggestion) {

	}

	private List<TransactionModel> filterWithoutDate(final ReportFilterType filterSuggestion) {
		List<TransactionModel> listToShow = new ArrayList<>();

		switch (filterSuggestion) {
			case ALL_TRANSACTIONS:
				allTransactionFilter(listToShow);
				break;
			case RECEIVED_AMOUNT:
				listToShow = applyReceivedFilter();
				updateUI(1);
				break;
			case LENT_AMOUNT:
				listToShow = applyLentFilter();
				break;
			case EARNED_AMOUNT:
				listToShow = applyEarnedFilter();
				break;
			default:
				allTransactionFilter(listToShow);
				break;

		}
		return listToShow;

	}

	private void updateUI(final int i) {

	}

	private List<TransactionModel> applyReceivedFilter() {
		return Stream.of(transactionsList)
				.filter(expenseList -> expenseList.getReceivedAmt() != null)
				.collect(Collectors.toList());
	}

	private List<TransactionModel> applyEarnedFilter() {
		return Stream.of(transactionsList)
				.filter(expenseList -> expenseList.getGainedAmt() != null)
				.collect(Collectors.toList());
	}

	private List<TransactionModel> applyLentFilter() {
		return Stream.of(transactionsList)
				.filter(expenseList -> expenseList.getLentAmt() != null)
				.collect(Collectors.toList());
	}

	private List<TransactionModel> allTransactionFilter(final List<TransactionModel> listToShow) {
		listToShow.addAll(transactionsList);
		return listToShow;
	}

	private List<TransactionModel> filterResults(ReportFilterType selected_type) {


	}

	private List<TransactionModel> filterApply(final BigDecimal amt) {
		return Stream.of(transactionsList)
				.filter(expenseList -> amt != null)
				.collect(Collectors.toList());

	}

	private void setupRecyclerView() {
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
				Objects.requireNonNull(getActivity()).getApplicationContext());

		// horizontal RecyclerView
		// keep movie_list_row.xml width to `wrap_content`
		// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
		// (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

		reportRecyclerView.setLayoutManager(mLayoutManager);


		reportRecyclerView.setItemAnimator(new DefaultItemAnimator());

		reportRecyclerView.setAdapter(mAdapter);

		// row click listener
		RecyclerTouchListener recyclerTouchListener =
				new RecyclerTouchListener(getActivity().getApplicationContext(),
						reportRecyclerView,
						new RecyclerTouchListener.ClickListener() {
							@Override
							public void onClick(View view, int position) {
								TransactionModel report = transactionsList.get(position);
								Toast.makeText(getActivity().getApplicationContext(),
										report.getReceivedAmt() + " is Available Balance",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onLongClick(View view, int position) {

							}
						});
		//	reportRecyclerView.addOnItemTouchListener(recyclerTouchListener);
	}


	@OnClick({R.id.from_date_text_view, R.id.to_date_text_view})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.from_date_text_view:
				loadDialogFragment(REQUEST_DATE_FROM);
				break;
			case R.id.to_date_text_view:
				loadDialogFragment(REQUEST_DATE_TO);
				break;

		}
	}

	private void loadDialogFragment(int requestDate) {
		activityUtils.loadDialogFragment(DatePickerFragment.newInstance(), this,
				getFragmentManager(), requestDate, DIALOG_DATE);
	}
}
