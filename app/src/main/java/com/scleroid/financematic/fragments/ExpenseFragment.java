package com.scleroid.financematic.fragments;

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
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.ExpenseAdapter;
import com.scleroid.financematic.model.Expense;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ExpenseFragment extends Fragment {
    @BindView(R.id.expense_recycler)
    RecyclerView expenseRecyclerView;
    private List<Expense> expenseList = new ArrayList<>();

    private ExpenseAdapter mAdapter;

    @BindView(R.id.pie_chart_expense)
    PieChart mChart;
    @BindView(R.id.toaa)
    TextView toaa;
    @BindView(R.id.totsss)
    TextView totsss;
    ArrayList<PieEntry> values = new ArrayList<>();
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
        mAdapter = new ExpenseAdapter(expenseList);
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


    private void prepareExpenseData() {
        Expense expense = new Expense(200, Expense.LIGHT_BILL, new Date("1/6/2018"));
        expenseList.add(expense);
        expense = new Expense(42642, Expense.FUEL, new Date("12/3/2018"));
        expenseList.add(expense);
        expense = new Expense(54545, Expense.PAID_SALARIES, new Date("4/15/2018"));
        expenseList.add(expense);
        expense = new Expense(2323, Expense.PHONE_BILL, new Date("7/25/2018"));
        expenseList.add(expense);
        expense = new Expense(12122, Expense.ROOM_RENT, new Date("2/15/2018"));
        expenseList.add(expense);
        expense = new Expense(45, Expense.OTHER, new Date("4/13/2018"));
        expenseList.add(expense);


        mAdapter.setExpenses(expenseList);
        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }



}
