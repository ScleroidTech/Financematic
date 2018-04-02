package com.scleroid.financematic.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.ReportAdapter;
import com.scleroid.financematic.model.Report;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



/**
 * Created by scleroid on 2/3/18.
 */

public class ReportFragment1 extends Fragment implements
        AdapterView.OnItemSelectedListener {
    private static final String DIALOG_DATE = "DIALOG_DATE";
    private static final int REQUEST_DATE = 0;
    TextView etfromDate, ettoDate;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();

    String[] country = {"All Amount", "Received Amount", "Lent Amount", "Expenditure", "Interest Earned"};
    private boolean sortAscending=true;
    private boolean unSorted=true;
    Spinner spin;
    private Spinner spinner;

    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportAdapter mAdapter;

    public ReportFragment1() {
        // Required empty public constructor
    }

    public static ReportFragment1 newInstance(String param1, String param2) {
        ReportFragment1 fragment = new ReportFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        etfromDate = rootView.findViewById(R.id.from_date_text_view);
        final Spinner spin = rootView.findViewById(R.id.spinnerr);
        spin.setOnItemSelectedListener(this);
       /* spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortData();
            }
        });*/
/*        final String text = spin.getSelectedItem().toString();*/
        //Creating the ArrayAdapter instance having the filterSuggestions list
        ArrayAdapter aa = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        etfromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
              /*  FragmentManager fragmentManager = getActivity().getFragmentManager();
                DialogFragment dialogFragment = new Fragment_datepicker_all();
              *//*  dialogFragment.setTargetFragment(fragmentManager.findFragmentByTag(CURRENT_TAG), REQUEST_DATE);*//*
                dialogFragment.show(fragmentManager, DIALOG_DATE);*/
            }
        });
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };
        ettoDate = rootView.findViewById(R.id.to_date_text_view);
        ettoDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        recyclerView = rootView.findViewById(R.id.report_recycler_view);

        mAdapter = new ReportAdapter(reportList);

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
        Report report = new Report("10225 ", "25000", "3/3/2018", "3000", "15000");
        reportList.add(report);
        report = new Report("20225 ", "45000", "1/3/2018", "2000", "5000");
        reportList.add(report);
        report = new Report("10225 ", "25000", "5/2/2017", "2000", "5000");
        reportList.add(report);
        report = new Report("10325 ", "35000", "1/2/2017", "2500", "1000");
        reportList.add(report);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etfromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        ettoDate.setText(sdf1.format(myCalendar1.getTime()));
    }

  /*  private void sortByName() {
        Collections.sort(laptops,  (l1, l2) -> l1.getName().compareTo(l2.getName()));
    }
    private void sortByPrice() {
        Collections.sort(report, (l1, l2) -> {
            if (l1.getReport_Acc_no() > l2.getReport_Acc_no()) {
                return 1;
            } else if (l1.getPrice() < l2.getPrice()) {
                return -1;
            } else {
                return 0;
            }
        });
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
/*    private void sortData()
    {
        List<String> galaxiesList= Arrays.asList(filterSuggestions);

        if(unSorted)Collections.sort(galaxiesList);
        else Collections.reverse(galaxiesList);

        sortAscending=!sortAscending;
        unSorted=false;

        spin.setAdapter(new ArrayAdapter(this,android.R.layout.recycler_report,galaxiesList));
    }*/
}
