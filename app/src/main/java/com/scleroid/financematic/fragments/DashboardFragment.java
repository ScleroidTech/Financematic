package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.LoanAdapter;
import com.scleroid.financematic.model.Loan;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 2/3/18.
 */


public class DashboardFragment extends Fragment {
    TextView firstFragment;
    private List<Loan> loanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LoanAdapter mAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        LinearLayout wrapper = new LinearLayout(getActivity());
        View rootView = inflater.inflate(R.layout.dashboard, wrapper, true);


//Intend
        firstFragment = rootView.findViewById(R.id.total_amount_text_view);
        firstFragment.setOnClickListener(v -> loadFragment(new Fragment_passbook()));

        recyclerView = rootView.findViewById(R.id.recycler_view_dashboard);

        mAdapter = new LoanAdapter(loanList);

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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        // recyclerView.addItemDecoration(dividerItemDecoration);


        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Loan loan = loanList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), loan.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareLoanData();

        return wrapper;
    }


    private void prepareLoanData() {
        Loan loan = new Loan("Shahrukh Khan", "2000", "2 Days to go");
       loanList.add(loan);
        loan = new Loan("Akshay Kumar", "1000", "3 days to go");
        loanList.add(loan);
        loan = new Loan("Amitabh Bachchan", "1500", "5 days to go");
        loanList.add(loan);



        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }





    //for intend passook
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit(); // save the changes
        // load fragment

    }


}
