package com.scleroid.financematic;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by scleroid on 9/3/18.
 */


public class Fragment_registor_given_money extends Fragment implements
        AdapterView.OnItemSelectedListener {
    Spinner spin;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    String[] country = { "Days", "Week", "Months", "year" };
    private Button b;   private Spinner spinner;
    private TextView  ettxloan_amout, ettxStartDate, ettxEndDate,ettxrateInterest,ettxInterestAmount,ettxInstallmentduration,etTotalLoanAmount ,ettxNoofInstallment,tv;



    public Fragment_registor_given_money() {
        // Required empty public constructor
    }

    public static Fragment_registor_given_money newInstance(String param1, String param2) {
        Fragment_registor_given_money fragment = new Fragment_registor_given_money();
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
        final View rootView = inflater.inflate(R.layout.registor_given_money, container, false);

        final Spinner spin = (Spinner) rootView.findViewById(R.id.spinnertx);
        spin.setOnItemSelectedListener(this);

/*        final String text = spin.getSelectedItem().toString();*/
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,country);
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
        ettxStartDate= (TextView) rootView.findViewById(R.id.txStartDate);
        ettxStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        ettxEndDate = (TextView) rootView.findViewById(R.id.txEndDate);
        ettxEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Object money;
        b = (Button) rootView.findViewById(R.id.btn_givenmoney);
        tv = (TextView) rootView.findViewById(R.id.displaytx);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ettxloan_amout = (TextView) rootView.findViewById(R.id.txloan_amout);



                ettxrateInterest = (TextView) rootView.findViewById(R.id.txrateInterest);
                ettxInterestAmount= (TextView)rootView.findViewById(R.id.txInterestAmount);
                ettxNoofInstallment= (TextView) rootView.findViewById(R.id.txNoofInstallment);
                ettxInstallmentduration= (TextView) rootView.findViewById(R.id.txInstallmentduration);
                etTotalLoanAmount = (TextView) rootView.findViewById(R.id.txTotalLoanAmount);

                tv.setText("Your Input: \n" + ettxloan_amout .getText().toString() + "\n" +  ettxStartDate.getText().toString() + "\n" +  ettxEndDate.getText().toString() + "\n" +  ettxrateInterest.getText().toString() + "\n" +  ettxInterestAmount.getText().toString() + "\n" +   ettxNoofInstallment .getText().toString() +  "\n" +ettxInstallmentduration.getText().toString() + spin.getSelectedItem().toString() +"\n"+etTotalLoanAmount.getText().toString() +"\n"+"\nEnd.");
            }
        });
        return rootView;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ettxStartDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
        ettxEndDate.setText(sdf1.format(myCalendar1.getTime()));
    }

}
