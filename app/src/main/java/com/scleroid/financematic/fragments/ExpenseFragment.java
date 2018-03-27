package com.scleroid.financematic.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ExpenseFragment extends Fragment {

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
        unbinder = ButterKnife.bind(this, view);
        //      float[] data = {450, 630, 300, 200, 400};
        //    mChart.setData(data);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        //  mChart.setCenterTextTypeface(mTfLight);

        initializeChartData();
        return view;
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
}
