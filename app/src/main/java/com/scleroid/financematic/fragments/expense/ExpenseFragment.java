package com.scleroid.financematic.fragments.expense;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.LinearLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.ExpenseCategory;
import com.scleroid.financematic.fragments.dialogs.InsertExpenseDialogFragment;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class
ExpenseFragment extends BaseFragment {
	private static final String DIALOG_EXPENSE_ADD = "add_new_expense";
	@Nullable
	@BindView(R.id.expense_recycler)
	RecyclerView expenseRecyclerView;
	@Nullable
	@BindView(R.id.room_rent_amt_text_view)
	RupeeTextView roomRentAmtTextView;
	@Nullable
	@BindView(R.id.light_bill_text_view)
	RupeeTextView lightBillTextView;
	@Nullable
	@BindView(R.id.phone_bill_text_view)
	RupeeTextView phoneBillTextView;
	@Nullable
	@BindView(R.id.salary_text_view)
	RupeeTextView salaryTextView;
	@Nullable
	@BindView(R.id.fuel_text_view)
	RupeeTextView fuelTextView;
	@Nullable
	@BindView(R.id.other_text_view)
	RupeeTextView otherTextView;
	@Nullable
	@BindView(R.id.room_rent_card)
	LinearLayout roomRentCard;
	@Nullable
	@BindView(R.id.light_bill_card)
	LinearLayout lightBillCard;
	@Nullable
	@BindView(R.id.phone_bill_card)
	LinearLayout phoneBillCard;
	@Nullable
	@BindView(R.id.salary_card)
	LinearLayout salaryCard;
	@Nullable
	@BindView(R.id.fuel_card)
	LinearLayout fuelCard;
	@Nullable
	@BindView(R.id.other_card)
	LinearLayout otherCard;
	@Nullable
	@BindView(R.id.pie_chart_expense)
	PieChart mChart;
	@Nullable
	@BindView(R.id.total_expense_text_view)
	RupeeTextView totalExpenseTextView;
	@Nullable
	@BindView(R.id.add_exp_call_button)
	Button addExpCallButton;
	@Nullable
	@BindView(R.id.empty_card)
	CardView emptyCard;
	@Inject
	ActivityUtils activityUtils;
	Button firstFragment;
	@Nullable
	private List<Expense> expenseList = new ArrayList<>();
	@Nullable
	private ExpenseAdapter mAdapter;
	private ExpenseViewModel expenseViewModel;
	private int totalRoomRentAmt;
	private int totalFuelAmt;
	private int totalPaidSalaryAmt;
	private int totalOtherAmt;
	private int totalPhoneBillAmt;
	private int totalLightBillAmt;
	private int totalLoan;

	public ExpenseFragment() {
		// Required empty public constructor
	}

	@NonNull
	public static ExpenseFragment newInstance(String param1, String param2) {
		ExpenseFragment fragment = new ExpenseFragment();
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
		// Inflate the layout for this fragment
		View view = getRootView();

		initializeRecyclerView();
		//      float[] data = {450, 630, 300, 200, 400};
		//    mChart.setData(data);

		//  mChart.setCenterTextTypeface(mTfLight);
		//	initializeChartData();

		setTitle();
		updateView(expenseList);

		return view;
	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(), "Expenses");
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_expense;
	}

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {
		expenseViewModel.getItemList().observe(this, items -> updateView(items.data));
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public BaseViewModel getViewModel() {
		expenseViewModel =
				ViewModelProviders.of(this, viewModelFactory).get(ExpenseViewModel.class);

		return expenseViewModel;
	}

	private void initializeRecyclerView() {

		mAdapter = new ExpenseAdapter(expenseList, getContext());
		/* recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));*/

		// vertical RecyclerView
		// keep movie_list_row.xml width to `match_parent`
		RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity());
		mLayoutManager.setAutoMeasureEnabled(false);

		// horizontal RecyclerView
		// keep movie_list_row.xml width to `wrap_content`
		// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
		// (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

		expenseRecyclerView.setLayoutManager(mLayoutManager);


		expenseRecyclerView.setItemAnimator(new DefaultItemAnimator());

		expenseRecyclerView.setAdapter(mAdapter);
		//	expenseRecyclerView.setHasFixedSize(true);
		firstFragment = getRootView().findViewById(R.id.add_exp_call_button);
		firstFragment.setOnClickListener(
				v -> activityUtils.loadDialogFragment(InsertExpenseDialogFragment.newInstance(),
						getFragmentManager
								(), DIALOG_EXPENSE_ADD));

		expenseRecyclerView.setNestedScrollingEnabled(false);

	}

	/*private void updateUi(final List<Expense> items) {
		getTotalLoan(items);

		getTotalCategoryAmt(items, ExpenseCategory.LIGHT_BILL);
		getTotalCategoryAmt(items, ExpenseCategory.PHONE_BILL);
		getTotalCategoryAmt(items, ExpenseCategory.OTHER);
		getTotalCategoryAmt(items, ExpenseCategory.PAID_SALARIES);
		getTotalCategoryAmt(items, ExpenseCategory.FUEL);
		getTotalCategoryAmt(items, ExpenseCategory.ROOM_RENT);
		initializeChartData();

	}
*/

	private void updateView(@Nullable final List<Expense> items) {
		if (items == null || items.isEmpty()) {
			emptyCard.setVisibility(View.VISIBLE);
			expenseRecyclerView.setVisibility(View.GONE);
		} else {
			emptyCard.setVisibility(View.GONE);
			expenseRecyclerView.setVisibility(View.VISIBLE);
			sort(items);
			expenseList = items;
			updateUi(items);
			refreshRecyclerView(expenseList);

		}
	}

	private void updateUi(@NonNull final List<Expense> items) {


		totalLoan = getTotalLoan(items);

		totalLightBillAmt = getTotalCategoryAmt(items, ExpenseCategory.LIGHT_BILL);
		totalPhoneBillAmt = getTotalCategoryAmt(items, ExpenseCategory.PHONE_BILL);
		totalOtherAmt = getTotalCategoryAmt(items, ExpenseCategory.OTHER);
		totalPaidSalaryAmt = getTotalCategoryAmt(items, ExpenseCategory.PAID_SALARIES);
		totalFuelAmt = getTotalCategoryAmt(items, ExpenseCategory.FUEL);
		totalRoomRentAmt = getTotalCategoryAmt(items, ExpenseCategory.ROOM_RENT);
		initializeChartData();

		//	Timber.d("THis thing runs, yeah");

		totalExpenseTextView.setText(String.valueOf(totalLoan));
		lightBillTextView.setText(String.valueOf(
				totalLightBillAmt));
		phoneBillTextView.setText(String.valueOf(
				totalPhoneBillAmt));
		otherTextView.setText(String.valueOf(totalOtherAmt));
		salaryTextView.setText(String.valueOf(
				totalPaidSalaryAmt));
		fuelTextView.setText(String.valueOf(totalFuelAmt));
		roomRentAmtTextView.setText(String.valueOf(
				totalRoomRentAmt));


	}

	private void initializeChartData() {
		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		float roomRent = getPercentage(totalRoomRentAmt);
		float phoneBill = getPercentage(totalPhoneBillAmt);
		float lightBill = getPercentage(totalLightBillAmt);
		float other = getPercentage(totalOtherAmt);
		float fuel = getPercentage(totalFuelAmt);
		float salaries = getPercentage(totalPaidSalaryAmt);
		ArrayList<PieEntry> yvalues = new ArrayList<>();
		yvalues.add(new PieEntry(roomRent, ExpenseCategory.ROOM_RENT));
		yvalues.add(new PieEntry(phoneBill, ExpenseCategory.PHONE_BILL));
		yvalues.add(new PieEntry(lightBill, ExpenseCategory.LIGHT_BILL));
		yvalues.add(new PieEntry(fuel, ExpenseCategory.FUEL));
		yvalues.add(new PieEntry(salaries, ExpenseCategory.PAID_SALARIES));
		yvalues.add(new PieEntry(other, ExpenseCategory.OTHER));

		PieDataSet dataSet = new PieDataSet(yvalues, "Expenses");
		List<String> xVals = new ArrayList<>();

		xVals.add(ExpenseCategory.ROOM_RENT);
		xVals.add(ExpenseCategory.PHONE_BILL);
		xVals.add(ExpenseCategory.LIGHT_BILL);
		xVals.add(ExpenseCategory.FUEL);
		xVals.add(ExpenseCategory.PAID_SALARIES);
		xVals.add(ExpenseCategory.OTHER);
		//   List<LegendEntry> entries = new ArrayList<>();

   /*     for (int i = 0; i < xVals.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = xVals.get(i);
            entries.add(entry);
        }*/
		Legend legend = mChart.getLegend();
		legend.setEnabled(false);

		dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
		dataSet.setSliceSpace(3f);
		mChart.setDrawEntryLabels(true);
		mChart.setUsePercentValues(true);
		mChart.getDescription().setEnabled(false);
//        mChart.getXAxis().setTextColor(Color.GRAY);
		mChart.getLegend().setTextColor(Color.DKGRAY);
		PieData data = new PieData(dataSet);
		data.setValueTextColor(Color.WHITE);
		// In percentage Term
		data.setValueFormatter(new PercentFormatter());
		mChart.invalidate();
		mChart.setData(data);


		//Disable Hole in the Pie Chart
		mChart.setDrawHoleEnabled(false);

		mChart.animateXY(1400, 1400);
// Default value
//data.setValueFormatter(new DefaultValueFormatter(0));

	}

	private float getPercentage(int amt) {
		return (float) amt / totalLoan * 100;
	}

	@SuppressLint("NewApi")
	private int getTotalLoan(final List<Expense> items) {
		//	Observable<Integer> sourceObservable = Observable.range(1, 20);
		/*Observable<Integer> observable = Observable.fromIterable(items).subscribeOn(
				Schedulers.newThread())
				.filter(o -> o.getExpenseAmount() != null)
				.map(o -> o.getExpenseAmount().intValue());
		observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
		.subscribe(value -> {
			totalLoan += value;
			totalExpenseTextView.setText(String.valueOf(totalLoan));

		});
*/
		return items.stream()
				.filter(o -> o.getExpenseAmount() != null)
				.mapToInt(o -> o.getExpenseAmount().intValue())
				.sum();
	}

	@SuppressLint({"NewApi", "CheckResult"})
	private int getTotalCategoryAmt(final List<Expense> items, String expenseCategory) {
		/*final AtomicInteger temp = new AtomicInteger(0);
		Observable<Integer> observable = Observable.fromIterable(items).subscribeOn(
				Schedulers.newThread())
				.filter(o -> o.getExpenseAmount() != null && o.getExpenseType()
						.equals(expenseCategory))
				.map(o -> o.getExpenseAmount().intValue());

		observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
		.subscribe(value -> {

			temp.addAndGet(value);
			switch (expenseCategory) {

				case ExpenseCategory.ROOM_RENT:
					totalRoomRentAmt = temp.get();
					roomRentAmtTextView.setText(String.valueOf(
							totalRoomRentAmt));
					break;
				case ExpenseCategory.LIGHT_BILL:
					totalLightBillAmt = temp.get();
					lightBillTextView.setText(String.valueOf(
							totalLightBillAmt));
					break;
				case ExpenseCategory.PHONE_BILL:
					totalPhoneBillAmt = temp.get();
					phoneBillTextView.setText(String.valueOf(
							totalPhoneBillAmt));
					break;
				case ExpenseCategory.PAID_SALARIES:
					totalPaidSalaryAmt = temp.get();
					salaryTextView.setText(String.valueOf(
							totalPaidSalaryAmt));
					break;
				case ExpenseCategory.FUEL:
					totalFuelAmt = temp.get();
					fuelTextView.setText(String.valueOf(totalFuelAmt));
					break;
				case ExpenseCategory.OTHER:
					totalOtherAmt = temp.get();
					otherTextView.setText(String.valueOf(totalOtherAmt));
					break;
			}
			initializeChartData();

		});*/


		return items.stream()
				.filter(o -> o.getExpenseAmount() != null && o.getExpenseType()
						.equals(expenseCategory))
				.mapToInt(o -> o.getExpenseAmount().intValue())
				.sum();
	}

	private void refreshRecyclerView(List<Expense> expenses) {
		mAdapter.setExpenses(expenses);
		mAdapter.notifyDataSetChanged();
	}

	private void sort(@NonNull final List<Expense> transactions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			transactions.sort(Comparator.comparing(Expense::getExpenseDate));
		} else {
			Collections.sort(transactions,
					(m1, m2) -> m1.getExpenseDate().compareTo(m2.getExpenseDate()));
		}
	}

	@OnClick({R.id.room_rent_card, R.id.light_bill_card, R.id.phone_bill_card, R.id.salary_card, R
			.id.fuel_card, R.id.other_card})
	public void onViewClicked(@NonNull View view) {

		switch (view.getId()) {
			case R.id.room_rent_card:
				filterThenRefresh(ExpenseCategory.ROOM_RENT);
				break;
			case R.id.light_bill_card:
				filterThenRefresh(ExpenseCategory.LIGHT_BILL);
				break;
			case R.id.phone_bill_card:
				filterThenRefresh(ExpenseCategory.PHONE_BILL);
				break;
			case R.id.salary_card:
				filterThenRefresh(ExpenseCategory.PAID_SALARIES);
				break;
			case R.id.fuel_card:
				filterThenRefresh(ExpenseCategory.FUEL);
				break;
			case R.id.other_card:
				filterThenRefresh(ExpenseCategory.OTHER);
				break;
		}


	}

	private void filterThenRefresh(String selectedOption) {
		List<Expense> expenses = filterResults(selectedOption);
		refreshRecyclerView(expenses);
	}

	private List<Expense> filterResults(String selected_type) {

		return Stream.of(expenseList)
				.filter(expenseList -> expenseList.getExpenseType().equals(selected_type))
				.collect(Collectors.toList());
	}


}
