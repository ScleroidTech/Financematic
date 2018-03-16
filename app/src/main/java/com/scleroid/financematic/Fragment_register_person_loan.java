package com.scleroid.financematic;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by scleroid on 7/3/18.
 */


public class Fragment_register_person_loan extends Fragment {
    private Button b;
    private TextInputEditText  etmobile, etAddress, etLoan_number, etLoan_Amount, etInstallment,etInterest ;
    TextView tv,etname;


    public Fragment_register_person_loan() {
        // Required empty public constructor
    }

    public static Fragment_register_person_loan newInstance(String param1, String param2) {
        Fragment_register_person_loan fragment = new Fragment_register_person_loan();
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
        final View rootView = inflater.inflate(R.layout.reg_person_deatils, container, false);


        b = (Button) rootView.findViewById(R.id.btn_customer_name);
        tv = (TextView) rootView.findViewById(R.id.display1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etname = (TextView) rootView.findViewById(R.id.reg_fullname_detaills);
                etmobile = (TextInputEditText) rootView.findViewById(R.id.Loan_Total_Amount1);
                etAddress = (TextInputEditText) rootView.findViewById(R.id.Paid_Amount);
                etLoan_number = (TextInputEditText) rootView.findViewById(R.id.Reamining_Amount);
                etLoan_Amount = (TextInputEditText) rootView.findViewById(R.id.Due_By);
                etInstallment = (TextInputEditText) rootView.findViewById(R.id.Next_Installments);
                etInterest = (TextInputEditText) rootView.findViewById(R.id. Interest_edit_text);

                tv.setText("Your Input: \n" + etname.getText().toString() + "\n" + etAddress.getText().toString() + "\n" + etInstallment.getText().toString() + "\n" + etmobile.getText().toString() + "\n" + etLoan_number.getText().toString() + "\n" + etLoan_Amount.getText().toString() +  "\n" +  etInterest.getText().toString() + "\n"+"\nEnd.");
            }
        });

        return rootView;

    }
}
