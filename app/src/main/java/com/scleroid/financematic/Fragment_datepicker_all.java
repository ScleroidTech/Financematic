package com.scleroid.financematic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by scleroid on 10/3/18.
 */

//import android.support.v7.app.AlertDialog;

/**
 * Created by Ganesh on 09-01-2016.
 */
public class Fragment_datepicker_all extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.ganesh.criminalintent.date";
    public static final String EXTRA_SERIAL = "serial_no";
    public static final String EXTRA_PARCEL = "oneMoreParcel";
    private static final String TAG = "DatePickerFragment";
    private static final String EXTRA_SOURCE_PIN = "source";
    private static final String EXTRA_PACKAGE = "packageType";
    private static final String EXTRA_DELIVERY_TYPE = "delivery";
    private static final String EXTRA_DEST_PIN = "destination";
    private static final String EXTRA_DESC = "description";
    private static final String EXTRA_INVOICE = "invoice";
    private static final String EXTRA_WEIGHT = "weight";
    private static final String EXTRA_HEIGHT = "height";
    private static final String EXTRA_WIDTH = "width";
    private static final String EXTRA_LENGTH = "length";

    long serialNo;
    String source, destination, deliveryType, packageType, desc;
    int height, weight, width, length, invoice;
    private DatePicker mDatePicker;
    private Parcel parcel;

    Date tempDate = new Date();

 /*   public static Fragment_datepicker_all newInstance(String param1, String param2) {
        Fragment_datepicker_all fragment = new Fragment_datepicker_all();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Bundle bundle = getArguments();
        if (bundle != null) {
            parcel = bundle.getParcelable(EXTRA_PARCEL);

        }

        Calendar calendar = Calendar.getInstance();
        Log.d(TAG, calendar.toString());
        Log.d(TAG, tempDate + "THis should not be empty ");
        calendar.setTime(tempDate);


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        mDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
              /*  parcel.setParcelDate(date);*//*
                Parcel parcel = new Parcel(source, destination, deliveryType, packageType, weight, invoice, length, width, height, desc, date, serialNo); */
            /*    Bundle bundle = createBundle(parcel);*/
                //sendResult(Activity.RESULT_OK,date);
              /*  Events.DateMessage addressMessage = new Events.DateMessage(bundle);
                GlobalBus.getBus().post(addressMessage);*/
            }
        }).create();
    }
}
