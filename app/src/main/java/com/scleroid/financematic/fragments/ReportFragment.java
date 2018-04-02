package com.scleroid.financematic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.ReportAdapter;
import com.scleroid.financematic.model.Report;
import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.DateUtils;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 2/3/18
 */

public class ReportFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {
    private static final String DIALOG_DATE = "DIALOG_DATE";
    private static final int REQUEST_DATE_FROM = 1;
    private static final int REQUEST_DATE_TO = 2;

    @Inject
    DateUtils dateUtils;

    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();

    String[] filterSuggestions = {"All Amount", "Received Amount", "Lent Amount", "Expenditure", "Interest Earned"};
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



    private List<Report> reportList = new ArrayList<>();

    private ReportAdapter mAdapter;
    private Unbinder unbinder;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        toDateTextView.setText(sdf1.format(myCalendar1.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_DATE_FROM) {
            Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            fromDateTextView.setText(dateUtils.getFormattedDate(date));
        } else if (requestCode == REQUEST_DATE_TO) {
            Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            toDateTextView.setText(dateUtils.getFormattedDate(date));
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ActivityUtils activityUtils = new ActivityUtils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        mAdapter = new ReportAdapter(reportList);

        reportRecyclerView.setHasFixedSize(true);

        /* reportRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));*/

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        setupRecyclerView();
        setupSpinner();

        prepareLoanData();

        return rootView;


    }

    private void setupSpinner() {
        ArrayAdapter<? extends String> spinnerList = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_spinner_item, filterSuggestions);
        spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerList);
        spinnerFilter.setOnItemClickListener((parent, view, position, id) -> {

        });

    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        reportRecyclerView.setLayoutManager(mLayoutManager);


        reportRecyclerView.setItemAnimator(new DefaultItemAnimator());

        reportRecyclerView.setAdapter(mAdapter);

        // row click listener
        reportRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), reportRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Report report = reportList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), report.getBalanceAmt() + " is Available Balance", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void prepareLoanData() {
        Report report = new Report("10225 ", "25000", new Date(2018, 3, 2), "3000", "15000");
        reportList.add(report);
        report = new Report("20225 ", "45000", new Date(2018, 3, 1), "2000", "5000");
        reportList.add(report);
        report = new Report("10225 ", "25000", new Date(2017, 2, 5), "2000", "5000");
        reportList.add(report);
        report = new Report("10325 ", "35000", new Date(2017, 2, 1), "2500", "1000");
        reportList.add(report);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
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
        activityUtils.loadDialogFragment(DatePickerFragment.newInstance(), this, getFragmentManager(), requestDate, DIALOG_DATE);
    }
}
