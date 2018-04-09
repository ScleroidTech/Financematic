package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.utils.ActivityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scleroid on 2/3/18.
 * <p>
 * Created by scleroid on 2/3/18.
 */
/**
 * Copyright (C) 2018
 * @author Ganesh Kaple
 * @since 2/3/18
 */

public class RegisterCustomerFragment extends Fragment {
    TextView tv;
    Button firstFragment;
    private Button b;
    private EditText etname, etmobile, etAddress, etLoan_number, etIDproofno;

    private ActivityUtils activityUtils = new ActivityUtils();

    public RegisterCustomerFragment() {
        // Required empty public constructor
    }

    public static RegisterCustomerFragment newInstance(String param1, String param2) {
        RegisterCustomerFragment fragment = new RegisterCustomerFragment();
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
        final View rootView = inflater.inflate(R.layout.reg_new_customer, container, false);

//Intend
        firstFragment = rootView.findViewById(R.id.btn_new_customer_Register1);

        etname = rootView.findViewById(R.id.coustomer_Name_EditText);
        etmobile = rootView.findViewById(R.id.Mobile_Number_EditText);
        etAddress = rootView.findViewById(R.id.Address_EditText);
        etLoan_number = rootView.findViewById(R.id.Loan_number_EditText);
        etIDproofno = rootView.findViewById(R.id.IDproofno);
        etname.addTextChangedListener(new TextValidator(  etname) {
            @Override public void validate(TextView textView, String text) {

                final String nameval =   etname.getText().toString();
                if (!isValidEmail(nameval)) {
                    etname.setError("Enter Valid Full name");
                }
            }
        });
        etmobile.addTextChangedListener(new TextValidator(  etmobile) {
            @Override public void validate(TextView textView, String text) {

                final String mobileval =  etmobile.getText().toString();
                if (!isValidMobile(mobileval)) {
                    etmobile.setError("Enter Valid Full name");
                }
            }
        });
        etAddress.addTextChangedListener(new TextValidator(  etAddress) {
            @Override public void validate(TextView textView, String text) {

                final String addressval = etAddress.getText().toString();
                if (!isValidAddress(addressval)) {
                    etAddress.setError("Enter Valid Full name");
                }
            }
        });
        /*firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
*/
       /* b=(Button)rootView.findViewById(R.id.btn_new_customer_Register);*/
     /* tv=(TextView)rootView.findViewById(R.id.display);*/
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                activityUtils.loadFragment(new RegisterMoneyFragment(), getFragmentManager());
               /* tv.setText("Your Input: \n"+etname.getText().toString()+"\n"+etAddress.getText().toString()+"\n"+etmobile.getText().toString()+"\n"+etLoan_number.getText().toString()+"\n"+etIDproofno.getText().toString()+"\n"+"\nEnd.");*/
            }
        });



        return rootView;
    }

    private boolean isValidEmail(String nameval) {
        String EMAIL_PATTERN = "[a-zA-Z]+\\.?";//only number 10

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(nameval);
        return matcher.matches();
    }
    private boolean isValidMobile(String mobileval) {
        String EMAIL_PATTERN = "^[0-9]{10}$";//only alpha space

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(mobileval);
        return matcher.matches();
    }
    private boolean isValidAddress(String addressval) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9.-\\s]+$";//only alpha space

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(addressval);
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



}
