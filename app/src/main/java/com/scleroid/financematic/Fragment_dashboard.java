package com.scleroid.financematic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 2/3/18.
 */




public class Fragment_dashboard   extends Fragment {
    private List<Loan> loanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LoanesAdapter mAdapter;
    TextView firstFragment;
    public Fragment_dashboard  () {
        // Required empty public constructor
    }

    public static Fragment_dashboard   newInstance(String param1, String param2) {
        Fragment_dashboard   fragment = new Fragment_dashboard  ();
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
        View rootView = inflater.inflate(R.layout.dashboard, container, false);


//Intend
        firstFragment = (TextView) rootView.findViewById(R.id.cardview_list_title);
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new Fragment_passbook());
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.dash_my_recycler);

        mAdapter = new LoanesAdapter(loanList);

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
                Loan loan = loanList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), loan.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareLoanData();

        return rootView;
    }


    private void prepareLoanData() {
        Loan loan = new Loan("Person Name 1", "Amount 2000", "14/6/2018");
       loanList.add(loan);
          loan = new Loan("Person Name 2", "Amount 1000", "11/5/2018");
        loanList.add(loan);
        loan = new Loan("Person Name 3", "Amount 1500", "1/4/2018");
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
