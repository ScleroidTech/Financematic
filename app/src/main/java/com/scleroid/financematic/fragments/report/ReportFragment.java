package com.scleroid.financematic.fragments.report;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.fragments.dialogs.DatePickerDialogFragment;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;


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
	private static final String FILTER_TYPE = "filter_type";

	@Nullable
	@BindView(R.id.empty_card)
	CardView emptyCard;

	@Inject
	DateUtils dateUtils;


	@NonNull
	String[] filterSuggestions =
			{"All Amount", "Received Amount", "Lent Amount", "Earned Amount"};

	/*String[] shortSuggestions =
			{"Date modified", "Total Outstanding"};*/
	Spinner spin;
	/*Spinner spin1;*/


	@Nullable
	@BindView(R.id.from_date_text_view)
	TextView fromDateTextView;
	@Nullable
	@BindView(R.id.to_date_text_view)
	TextView toDateTextView;

	@Nullable
	@BindView(R.id.report_recycler_view)
	RecyclerView reportRecyclerView;
	@Nullable
	@BindView(R.id.spinnerr)
	Spinner spinnerFilter;
	/*@BindView(R.id.spinnershortdate)
	Spinner spinnershort;
*/

	@NonNull
	ActivityUtils activityUtils = new ActivityUtils();
	/*@Nullable
	@BindView(R.id.accNo)
	TextView accNo;
	@Nullable
	@BindView(R.id.installmentDate)
	TextView installmentDate;
	@Nullable
	@BindView(R.id.expectedAmt)
	TextView expectedAmt;
	@Nullable
	@BindView(R.id.earnedAmt)
	TextView earnedAmt;


	@Nullable
	@BindView(R.id.receivedAmt)
	TextView receivedAmt;*/
	@Nullable
	@BindView(R.id.filter_button)
	Button filterButton;
	@Nullable
	@BindView(R.id.no_address_title)
	TextView noAddressTitle;
	@Nullable
	@BindView(R.id.no_address_subtitle)
	TextView noAddressSubtitle;
	Unbinder unbinder;
	@Nullable
	private List<TransactionModel> transactionsList = new ArrayList<>();
	private ReportAdapter mAdapter;

	private ReportViewModel reportViewModel;
	private Date startDate;
	private Date endDate;
	@Nullable
	private ReportFilterType reportFilterType = ReportFilterType.ALL_TRANSACTIONS;

	public ReportFragment() {
		// Required empty public constructor
	}

	@NonNull
	public static ReportFragment newInstance(ReportFilterType filterType) {
		ReportFragment fragment = new ReportFragment();
		Bundle args = new Bundle();
		args.putSerializable(FILTER_TYPE, filterType);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @NonNull Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_DATE_FROM) {
			startDate = (Date) intent.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
			fromDateTextView.setText(dateUtils.getFormattedDate(startDate));
		} else if (requestCode == REQUEST_DATE_TO) {
			endDate = (Date) intent.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
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


//add manoj
		/*final Spinner spin1 = rootView.findViewById(R.id.spinnershortdate);
	ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_item, shortSuggestions);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		spin1.setAdapter(aa);
		spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
									   final int position, final long id) {

			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});*/


	/*		spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
									   final int position, final long id) {
				durationType = country[position];
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});*/


		mAdapter = new ReportAdapter();

		reportRecyclerView.setHasFixedSize(true);

		reportRecyclerView.setNestedScrollingEnabled(true);

		/* reportRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext())
		);*/

		// vertical RecyclerView
		// keep movie_list_row.xml width to `match_parent`
		setupRecyclerView();
		setupSpinner();


		//	mChart.setUsePercentValues(true);
		//	mChart.getDescription().setEnabled(false);
		//  mChart.setCenterTextTypeface(mTfLight);

		//	initializeChartData();

		handleClickFromDashboard();
		setTitle();

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
		reportViewModel.getItemList()
				.observe(this, transactions -> updateListData(transactions.data));
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
				List<TransactionModel> tempList;
				reportFilterType = getSuggestion(position);
				//getTheListSorted();
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});

		spinnerFilter.setAdapter(spinnerList);

	}

	@NonNull
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

	private void handleClickFromDashboard() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			reportFilterType = (ReportFilterType) bundle.get(FILTER_TYPE);
			if (reportFilterType != null) {
				if (reportFilterType == ReportFilterType.RECEIVED_AMOUNT) {
					spinnerFilter.setSelection(1);
				} else if (reportFilterType == ReportFilterType.LENT_AMOUNT) {
					spinnerFilter.setSelection(2);
				}
				final List<TransactionModel> tempList = filterWithoutDate(reportFilterType);
				updateListDataTemp(tempList);
			}
		}
	}

	private void updateListDataTemp(@Nullable final List<TransactionModel> transactions) {
		if (transactions == null || transactions.isEmpty()) {
			emptyCard.setVisibility(View.VISIBLE);
			reportRecyclerView.setVisibility(View.GONE);
		} else {
			emptyCard.setVisibility(View.GONE);
			reportRecyclerView.setVisibility(View.VISIBLE);
			sort(transactions);
			//	transactionsList = transactions;
			mAdapter.setReportList(transactions);
			mAdapter.setFilterType(reportFilterType);
		}
	}

	private List<TransactionModel> filterWithoutDate(final ReportFilterType filterSuggestion) {
		List<TransactionModel> listToShow = new ArrayList<>();

		switch (filterSuggestion) {
			case ALL_TRANSACTIONS:
				allTransactionFilter(listToShow);
				break;
			case RECEIVED_AMOUNT:
				listToShow = applyReceivedFilter();
				//updateUI(receivedAmt);
				break;
			case LENT_AMOUNT:
				listToShow = applyLentFilter();
				//	updateUI(expectedAmt);
				break;
			case EARNED_AMOUNT:
				listToShow = applyEarnedFilter();
				//	updateUI(earnedAmt);
				break;
			default:
				allTransactionFilter(listToShow);
				break;

		}
		return listToShow;

	}


	private void sort(@NonNull final List<TransactionModel> transactions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			transactions.sort(Comparator.comparing(TransactionModel::getTransactionDate));
		} else {
			Collections.sort(transactions,
					(m1, m2) -> m1.getTransactionDate().compareTo(m2.getTransactionDate()));
		}
	}

	@NonNull
	private List<TransactionModel> allTransactionFilter(final List<TransactionModel> listToShow) {
		listToShow.addAll(transactionsList);
		//receivedAmt.setVisibility(View.VISIBLE);
		//	expectedAmt.setVisibility(View.VISIBLE);
		//	earnedAmt.setVisibility(View.VISIBLE);
		return listToShow;
	}

	private void updateListData(@Nullable final List<TransactionModel> transactions) {
		if (transactions == null || transactions.isEmpty()) {
			emptyCard.setVisibility(View.VISIBLE);
			reportRecyclerView.setVisibility(View.GONE);
		} else {
			emptyCard.setVisibility(View.GONE);
			reportRecyclerView.setVisibility(View.VISIBLE);
			sort(transactions);
			transactionsList = transactions;
			mAdapter.setReportList(transactions);
			mAdapter.setFilterType(reportFilterType);
		}
	}

	private List<TransactionModel> applyReceivedFilter() {
		return Stream.of(transactionsList)
				.filter(expenseList -> expenseList.getReceivedAmt() != null)
				.collect(Collectors.toList());
	}

	private void updateUI(final TextView amt) {
		//First Enable any previously disabled views
		visibilityToggle();
		amt.setVisibility(View.VISIBLE);

	}

	private void visibilityToggle() {
//		receivedAmt.setVisibility(View.GONE);
//		expectedAmt.setVisibility(View.GONE);
//		earnedAmt.setVisibility(View.GONE);
	}

	private List<TransactionModel> applyLentFilter() {
		return Stream.of(transactionsList)
				.filter(expenseList -> expenseList.getLentAmt() != null)
				.collect(Collectors.toList());
	}

	private List<TransactionModel> applyEarnedFilter() {
		return Stream.of(transactionsList)
				.filter(expenseList -> expenseList.getGainedAmt() != null)
				.collect(Collectors.toList());
	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(), "Report");
	}

	@OnClick(R.id.filter_button)
	public void onViewClicked() {
		getTheListSorted();
	}

	private List<TransactionModel> filterWithDate(final Date startDate, final Date endDate,
	                                              @NonNull final ReportFilterType
			                                              filterSuggestion) {
		List<TransactionModel> transactionModels = filterWithoutDate(filterSuggestion);
		return Stream.of(transactionModels)
				.filter(expenseList -> expenseList.getTransactionDate()
						.after(startDate) && expenseList.getTransactionDate().before(endDate))
				.collect(Collectors.toList());

	}

	@OnClick({R.id.from_date_text_view, R.id.to_date_text_view})
	public void onViewClicked(@NonNull View view) {
		switch (view.getId()) {
			case R.id.from_date_text_view:
				loadDialogFragment(REQUEST_DATE_FROM);
				break;
			case R.id.to_date_text_view:
				loadDialogFragment(REQUEST_DATE_TO);
				break;

		}
	}

	private void loadDialogFragment(int msg) {
		activityUtils.loadDialogFragment(DatePickerDialogFragment.newInstance(false), this,
				getFragmentManager(), msg, DIALOG_DATE);
	}

	private void getTheListSorted() {
		final List<TransactionModel> tempList;
		if (startDate == null && endDate == null) {
			tempList = filterWithoutDate(reportFilterType);
		} else {tempList = filterWithDate(startDate, endDate, reportFilterType); }
		updateListDataTemp(tempList);
	}
}
