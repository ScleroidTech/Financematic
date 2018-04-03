package com.scleroid.financematic.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.scleroid.financematic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by scleroid on 9/3/18.
 */


public class RegistorReceivedFragment extends Fragment implements
        AdapterView.OnItemSelectedListener{
    private static final String DIALOG_DATE = "DIALOG_DATE";
    private static final int REQUEST_DATE = 0;
    String interesting;
    Spinner spin;
    Context context;
    EditText edittext;
    Calendar myCalendar = Calendar.getInstance();
    String[] country = { "Received", "Late payment", "Less amount", "Other" };
    private Button b;
    private TextView  etrxDate, etrxTotalInterestAmount, etrxReceivedAmount,tv;


    public RegistorReceivedFragment() {
        // Required empty public constructor
    }

    public static RegistorReceivedFragment newInstance(String param1, String param2) {
        RegistorReceivedFragment fragment = new RegistorReceivedFragment();
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
        final View rootView = inflater.inflate(R.layout.registor_received_amount, container, false);


        final Spinner spin = rootView.findViewById(R.id.spinnerrx);
        spin.setOnItemSelectedListener(this);

/*        final String text = spin.getSelectedItem().toString();*/
        //Creating the ArrayAdapter instance having the filterSuggestions list
        ArrayAdapter aa = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        etrxDate = rootView.findViewById(R.id.rxDate);
        etrxReceivedAmount = rootView.findViewById(R.id.rxReceivedAmount);

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

        etrxDate.setOnClickListener(new View.OnClickListener() {
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
        b = rootView.findViewById(R.id.btn_customer_name);
        tv = rootView.findViewById(R.id.displayrx);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*   spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> adapterView, View view,
                            int i, long l) {
                        interesting =  spin.getItemAtPosition(i).toString();
                    }

                    public void onNothingSelected(
                            AdapterView<?> adapterView) {

                    }
                });
*/

                tv.setText("Your Input: \n" + etrxDate.getText().toString()  + "\n" +spin.getSelectedItem().toString() + "\n" +etrxReceivedAmount.getText().toString()+"\nEnd.");
            }
        });



        return rootView;

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etrxDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

}
