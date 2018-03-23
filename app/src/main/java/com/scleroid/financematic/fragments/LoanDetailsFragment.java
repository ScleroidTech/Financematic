package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.SummeryAdapter;
import com.scleroid.financematic.model.Personal_summery_loan_details;
import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.RecyclerTouchListener;
import com.scleroid.financematic.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by scleroid on 2/3/18.
 */

public class LoanDetailsFragment extends Fragment {


    TextViewUtils textViewUtils = new TextViewUtils();
    @BindView(R.id.total_amount_text_view)
    TextView totalAmountTextView;
    @BindView(R.id.duration_text_view)
    TextView durationTextView;
    @BindView(R.id.paid_amount_text_view)
    TextView paidAmountTextView;
    @BindView(R.id.installment_text_view)
    TextView installmentTextView;
    @BindView(R.id.card_view)
    CardView cardView;



    @BindView(R.id.pesonal_summery_details_recycler)
    RecyclerView pesonalSummeryDetailsRecycler;
    Unbinder unbinder;
    private List<Personal_summery_loan_details> summeryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SummeryAdapter mAdapter;
    private ActivityUtils activityUtils = new ActivityUtils();

    public LoanDetailsFragment() {
        // Required empty public constructor
    }

    public static LoanDetailsFragment newInstance(String param1, String param2) {
        LoanDetailsFragment fragment = new LoanDetailsFragment();
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
        View rootView = inflater.inflate(R.layout.try4, container, false);
        unbinder = ButterKnife.bind(this, rootView);
/*        View rootView =  inflater.inflate(R.layout.personal_loan_aacount_details, container, false);*/




        recyclerView = rootView.findViewById(R.id.pesonal_summery_details_recycler);

        mAdapter = new SummeryAdapter(summeryList);

        recyclerView.setHasFixedSize(true);


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
                Personal_summery_loan_details loan = summeryList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), loan.getSummery_amount() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareLoanData();

        //  textViewUtils.textViewExperiments(totalAmountTextView);
        return rootView;


    }


    private void prepareLoanData() {
        Personal_summery_loan_details loan = new Personal_summery_loan_details("Received", "2000", new Date("1/6/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "5000", new Date("12/3/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "1000", new Date("4/15/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Not Received", "7000", new Date("7/25/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "1000", new Date("2/15/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "3000", new Date("4/13/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "5000", new Date("12/3/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "1000", new Date("4/15/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Not Received", "7000", new Date("7/25/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "1000", new Date("2/15/2018"));
        summeryList.add(loan);
        loan = new Personal_summery_loan_details("Received", "3000", new Date("4/13/2018"));
        summeryList.add(loan);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
