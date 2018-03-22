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
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.Adapter_report;
import com.scleroid.financematic.model.Report;
import com.scleroid.financematic.utils.DateUtils;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;


/**
 * Copyright (C) 2018
 * @author Ganesh Kaple
 * @since 2/3/18
 */


public class FragmentReport extends Fragment {
    private static final String DIALOG_DATE = "DIALOG_DATE";
    private static final int REQUEST_DATE_FROM = 1;
    private static final int REQUEST_DATE_TO = 2;

    @Inject
    DateUtils dateUtils;
    TextView fromDateEditText, toDateEditText;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();

    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter_report mAdapter;

    public FragmentReport() {
        // Required empty public constructor
    }

    public static FragmentReport newInstance(String param1, String param2) {
        FragmentReport fragment = new FragmentReport();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        toDateEditText.setText(sdf1.format(myCalendar1.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_DATE_FROM) {
            Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            fromDateEditText.setText(dateUtils.getFormattedDate(date));
        } else if (requestCode == REQUEST_DATE_TO) {
            Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            toDateEditText.setText(dateUtils.getFormattedDate(date));
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, rootView);



        mAdapter = new Adapter_report(reportList);

        recyclerView.setHasFixedSize(true);

       /* recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));*/

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Report report = reportList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), report.getReport_Balance() + " is Available Balance", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareLoanData();

        return rootView;



    }

    private void prepareLoanData() {
        Report report = new Report("10225 ", "Rs 25000", "3%","3000","Rs 15000");
        reportList.add(report);
        report = new Report("20225 ", "Rs 45000", "2%","2000","Rs 5000");
        reportList.add(report);
        report = new Report("10225 ", "Rs 25000", "3%","2000","Rs 5000");
        reportList.add(report);
        report = new Report("10325 ", "Rs 35000", "4%","2500","Rs 1000");
        reportList.add(report);






        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }


}
