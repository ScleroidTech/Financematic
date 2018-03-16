package com.scleroid.financematic;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Created by scleroid on 10/3/18.
 */

/*current date http://srbh291.blogspot.in/2012/11/set-current-date-to-edittext-file-and.html*/

public class Fragment_datepicker1 extends Fragment {
    static final int DATE_DIALOG_ID = 0;
    private int mYear,mMonth,mDay;
    EditText editText;

    public Fragment_datepicker1() {
        // Required empty public constructor
    }

    public static Fragment_datepicker1 newInstance(String param1, String param2) {
        Fragment_datepicker1 fragment = new Fragment_datepicker1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressWarnings("deprecation")
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.frag_detepicker1, container, false);

        Calendar c= Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);
        //String dateFormat = "dd/MM/yyyy";
        editText = (EditText) rootView.findViewById(R.id.text);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        editText.setText( sdf.format(c.getTime()));

        editText.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().showDialog(DATE_DIALOG_ID);

            }
        });




        return rootView;
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(getActivity().getApplicationContext(),
                        mDateSetListener,
                        mYear, mMonth, mDay);

        }

        return null;

    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            editText.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear));

        }

    };




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }







}
