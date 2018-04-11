package com.scleroid.financematic.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.utils.ui.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by scleroid on 9/3/18.
 */


public class RegisterMoneyFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    private static final int REQUEST_DATE = 1;
    @Inject
    DateUtils dateUtils;
    Spinner spin;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    String[] country = { "Days", "Week", "Months", "year" };
    private Button b;   private Spinner spinner;
    private TextView  ettxloan_amout, ettxStartDate, ettxEndDate,ettxrateInterest,ettxInterestAmount,ettxInstallmentduration,etTotalLoanAmount ,ettxNoofInstallment,tv;


    public RegisterMoneyFragment() {
        // Required empty public constructor
    }

    public static RegisterMoneyFragment newInstance(String param1, String param2) {
        RegisterMoneyFragment fragment = new RegisterMoneyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            dateUtils.setDate(date);
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
        final View rootView = inflater.inflate(R.layout.registor_given_money, container, false);

        final Spinner spin = rootView.findViewById(R.id.spinnertx);
        spin.setOnItemSelectedListener(this);

/*        final String text = spin.getSelectedItem().toString();*/
        //Creating the ArrayAdapter instance having the filterSuggestions list
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
        ettxStartDate = rootView.findViewById(R.id.txStartDate);
        ettxStartDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        final DatePickerDialog.OnDateSetListener date1 = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar1.set(Calendar.YEAR, year);
            myCalendar1.set(Calendar.MONTH, monthOfYear);
            myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        };
        ettxEndDate = rootView.findViewById(R.id.txEndDate);
        ettxEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ettxloan_amout = rootView.findViewById(R.id.txloan_amout);
        ettxrateInterest = rootView.findViewById(R.id.txrateInterest);
        ettxInterestAmount = rootView.findViewById(R.id.txInterestAmount);
        ettxNoofInstallment = rootView.findViewById(R.id.txNoofInstallment);
        ettxInstallmentduration = rootView.findViewById(R.id.txInstallmentduration);
        etTotalLoanAmount = rootView.findViewById(R.id.txTotalLoanAmount);
        ettxloan_amout.addTextChangedListener(new RegisterCustomerFragment.TextValidator(  ettxloan_amout) {
            @Override public void validate(TextView textView, String text) {

                final String loan_amountval =   ettxloan_amout.getText().toString();
                if (!isValidLoan_amountval(loan_amountval)) {
                    ettxloan_amout.setError("Valid total given money");
                }
            }
        });

        ettxrateInterest.addTextChangedListener(new RegisterCustomerFragment.TextValidator(  ettxrateInterest) {
            @Override public void validate(TextView textView, String text) {

                final String rateInterestval =   ettxrateInterest.getText().toString();
                if (!isValidrateInterest(rateInterestval)) {
                    ettxrateInterest.setError("Valid valid Rate in %");
                }
            }
        });
        ettxInterestAmount.addTextChangedListener(new RegisterCustomerFragment.TextValidator(   ettxInterestAmount) {
            @Override public void validate(TextView textView, String text) {

                final String InterestAmountval =    ettxInterestAmount.getText().toString();
                if (!isValidInterestAmountval(InterestAmountval)) {
                    ettxInterestAmount.setError("Valid Interest Amount");
                }
            }
        });
        ettxNoofInstallment.addTextChangedListener(new RegisterCustomerFragment.TextValidator(    ettxNoofInstallment) {
            @Override public void validate(TextView textView, String text) {

                final String  NoofInstallmentval =     ettxNoofInstallment.getText().toString();
                if (!isValidNoofInstallmentvalval(NoofInstallmentval)) {
                    ettxNoofInstallment.setError("valid No of Installment");
                }
            }
        });
        ettxInstallmentduration.addTextChangedListener(new RegisterCustomerFragment.TextValidator(     ettxInstallmentduration) {
            @Override public void validate(TextView textView, String text) {

                final String  Installmentdurationval =      ettxInstallmentduration.getText().toString();
                if (!isValidInstallmentdurationval(Installmentdurationval)) {
                    ettxInstallmentduration.setError("Valid Installment Duration ");
                }
            }
        });
        etTotalLoanAmount.addTextChangedListener(new RegisterCustomerFragment.TextValidator(      etTotalLoanAmount) {
            @Override public void validate(TextView textView, String text) {

                final String  TotalLoanAmountval =       etTotalLoanAmount.getText().toString();
                if (!isValidTotalLoanAmount(TotalLoanAmountval)) {
                    etTotalLoanAmount.setError("Valid Total Amount");
                }
            }
        });


        Object money;
        b = rootView.findViewById(R.id.btn_givenmoney);
        tv = rootView.findViewById(R.id.displaytx);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strloan_amout= ettxloan_amout.getText().toString();
                String strtxrateInterest= ettxrateInterest.getText().toString();
                String strInterestAmount= ettxInterestAmount.getText().toString();
                String strNoofInstallment=  ettxNoofInstallment.getText().toString();
                String strInstallmentdurationt=    ettxInstallmentduration.getText().toString();
                String strTotalLoanAmount=    etTotalLoanAmount.getText().toString();
                String strStartDate=   ettxStartDate.getText().toString();
                String strEndDate=   ettxEndDate.getText().toString();
                if(TextUtils.isEmpty(strloan_amout)) {ettxloan_amout.setError("Enter Loan Amount");}
                if(TextUtils.isEmpty(strtxrateInterest)) {ettxrateInterest.setError("Enter rate Interest");}
                if(TextUtils.isEmpty(strInterestAmount)) { ettxInterestAmount.setError("Enter Interest Amount");}
                if(TextUtils.isEmpty(strNoofInstallment)) { ettxNoofInstallment.setError("Enter No of Installment");}
                if(TextUtils.isEmpty(strInstallmentdurationt)) { ettxInstallmentduration.setError("Installment duration");}
                if(TextUtils.isEmpty(strStartDate)) { ettxStartDate.setError("Start Date");}
                if(TextUtils.isEmpty(strEndDate)) {ettxEndDate.setError("End Date");}
                if(TextUtils.isEmpty(strTotalLoanAmount)) {  etTotalLoanAmount.setError("Total Loan Amount");return;}



                tv.setText("Your Input: \n" + ettxloan_amout .getText().toString() + "\n" +  ettxStartDate.getText().toString() + "\n" +  ettxEndDate.getText().toString() + "\n" +  ettxrateInterest.getText().toString() + "\n" +  ettxInterestAmount.getText().toString() + "\n" +   ettxNoofInstallment .getText().toString() +  "\n" +ettxInstallmentduration.getText().toString() + spin.getSelectedItem().toString() +"\n"+etTotalLoanAmount.getText().toString() +"\n"+"\nEnd.");
            }
        });
        return rootView;
    }
    private boolean isValidLoan_amountval(String loan_amountval) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(loan_amountval);
        return matcher.matches();
    }
    private boolean isValidrateInterest(String rateInterestval) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(rateInterestval);
        return matcher.matches();
    }
    private boolean isValidInterestAmountval(String InterestAmountval) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(InterestAmountval);
        return matcher.matches();
    }
    private boolean isValidNoofInstallmentvalval(String NoofInstallmentval) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(NoofInstallmentval);
        return matcher.matches();
    }
    private boolean isValidInstallmentdurationval(String Installmentdurationval) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Installmentdurationval);
        return matcher.matches();
    }
    private boolean isValidTotalLoanAmount(String TotalLoanAmountval) {
        String EMAIL_PATTERN = "^[0-9]+$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(TotalLoanAmountval);
        return matcher.matches();
    }
    public abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }



        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged (CharSequence s,int start, int before, int count)
        { /* Don't care */}
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
