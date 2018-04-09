package com.scleroid.financematic.fragments.expense;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.scleroid.financematic.adapter.ExpenseAdapter;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ExpenseFragment extends Fragment {
    @BindView(R.id.expense_recycler)
    RecyclerView expenseRecyclerView;
    @BindView(R.id.room_rent_amt_text_view)
    TextView roomRentAmtTextViewTextView;
    @BindView(R.id.light_bill_text_view)
    TextView lightBillTextView;
    @BindView(R.id.phone_bill_text_view)
    TextView phoneBillTextView;
    @BindView(R.id.salary_text_view)
    TextView salaryTextView;
    @BindView(R.id.fuel_text_view)
    TextView fuelTextView;
    @BindView(R.id.other_text_view)
    TextView otherTextView;
    @BindView(R.id.room_rent_card)
    LinearLayout roomRentCard;
    @BindView(R.id.light_bill_card)
    LinearLayout lightBillCard;
    @BindView(R.id.phone_bill_card)
    LinearLayout phoneBillCard;
    @BindView(R.id.salary_card)
    LinearLayout salaryCard;
    @BindView(R.id.fuel_card)
    LinearLayout fuelCard;
    @BindView(R.id.other_card)
    LinearLayout otherCard;
    @BindView(R.id.pie_chart_expense)
    PieChart mChart;
    @BindView(R.id.toaa)
    TextView toaa;
    @BindView(R.id.totsss)
    TextView totsss;
    ArrayList<PieEntry> values = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();
    private ExpenseAdapter mAdapter;
    private Unbinder unbinder;

    public ExpenseFragment() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);


        //


        unbinder = ButterKnife.bind(this, view);
        initializeRecyclerView();
        //      float[] data = {450, 630, 300, 200, 400};
        //    mChart.setData(data);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        //  mChart.setCenterTextTypeface(mTfLight);

        initializeChartData();
        return view;
    }

    private void initializeRecyclerView() {
        mAdapter = new ExpenseAdapter(expenseList, getContext());
        /* recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));*/

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        expenseRecyclerView.setLayoutManager(mLayoutManager);


        expenseRecyclerView.setItemAnimator(new DefaultItemAnimator());

        expenseRecyclerView.setAdapter(mAdapter);

        // row click listener
        expenseRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), expenseRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Expense expense = expenseList.get(position);
                // Toast.makeText(getActivity().getApplicationContext(), expense.getExpense_received_money() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareExpenseData();
    }

    private void prepareExpenseData() {
        Expense expense = new Expense(new BigDecimal(2000), Expense.LIGHT_BILL, new Date(2018, 6, 1));
        expenseList.add(expense);
        expense = new Expense(new BigDecimal(42642), Expense.FUEL, new Date(2018, 3, 12));
        expenseList.add(expense);
        expense = new Expense(new BigDecimal(54545), Expense.PAID_SALARIES, new Date(2018, 4, 15));
        expenseList.add(expense);
        expense = new Expense(new BigDecimal(2323), Expense.PHONE_BILL, new Date(2018, 7, 25));
        expenseList.add(expense);
        expense = new Expense(new BigDecimal(12122), Expense.ROOM_RENT, new Date(2018, 2, 15));
        expenseList.add(expense);
        expense = new Expense(new BigDecimal(4500), Expense.OTHER, new Date(2018, 4, 13));
        expenseList.add(expense);


        refreshRecyclerView(expenseList);
    }

    private void refreshRecyclerView(List<Expense> expenses) {
        mAdapter.setExpenses(expenses);
        mAdapter.notifyDataSetChanged();
    }

    private void initializeChartData() {
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(8f, "Jan"));
        yvalues.add(new PieEntry(15f, "Feb"));
        yvalues.add(new PieEntry(12f, "March"));
        yvalues.add(new PieEntry(25f, "April"));
        yvalues.add(new PieEntry(23f, "June"));
        yvalues.add(new PieEntry(17f, "August"));

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
        List<String> xVals = new ArrayList<String>();

        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");
        xVals.add("May");
        xVals.add("June");
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

//        mChart.getXAxis().setTextColor(Color.GRAY);
        mChart.getLegend().setTextColor(Color.DKGRAY);
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);
        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        mChart.setData(data);


        //Disable Hole in the Pie Chart
        mChart.setDrawHoleEnabled(false);
        mChart.animateXY(1400, 1400);
// Default value
//data.setValueFormatter(new DefaultValueFormatter(0));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.room_rent_card, R.id.light_bill_card, R.id.phone_bill_card, R.id.salary_card, R.id.fuel_card, R.id.other_card})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.room_rent_card:
                filterThenRefresh(1);
                break;
            case R.id.light_bill_card:
                filterThenRefresh(2);
                break;
            case R.id.phone_bill_card:
                filterThenRefresh(3);
                break;
            case R.id.salary_card:
                filterThenRefresh(4);
                break;
            case R.id.fuel_card:
                filterThenRefresh(5);
                break;
            case R.id.other_card:
                filterThenRefresh(0);
                break;
        }


    }

    private void filterThenRefresh(int selectedOption) {
        List<Expense> expenses = filterResults(selectedOption);
        refreshRecyclerView(expenses);
    }

    private List<Expense> filterResults(int selected_type) {

        return Stream.of(expenseList)
                .filter(expenseList -> expenseList.getExpenseType() == selected_type)
                .collect(Collectors.toList());
    }
}
