package com.scleroid.financematic.fragments;

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

import com.scleroid.financematic.R;
import com.scleroid.financematic.RecyclerTouchListener;
import com.scleroid.financematic.adapter.Adapter_list_all_peoples;
import com.scleroid.financematic.model.List_all_peoples;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 28/2/18.
 */

public class Fragment_list_all_peoples extends Fragment {
    TextView firstFragment;
    private List<List_all_peoples> list_all_peoplesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter_list_all_peoples mAdapter;

    public Fragment_list_all_peoples() {
        // Required empty public constructor
    }

    public static Fragment_list_all_peoples newInstance(String param1, String param2) {
        Fragment_list_all_peoples fragment = new Fragment_list_all_peoples();
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
        View rootView = inflater.inflate(R.layout.list_all_peoples, container, false);

        //Intend
        firstFragment = rootView.findViewById(R.id.button_list);
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new Fragment_personal_loan_details());
            }
        });


        recyclerView = rootView.findViewById(R.id.list_all_peoples_recycler);

        mAdapter = new Adapter_list_all_peoples(list_all_peoplesList);

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
                List_all_peoples loan = list_all_peoplesList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), loan.getList_received_amoun() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareLoanData();

        return rootView;


    }

    private void prepareLoanData() {
        List_all_peoples loan = new List_all_peoples("Person Name 1", "12100", "Rs200000");
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Person Name 2", "12100", "22100");
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Person Name 3", " 1500", "842100");
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Person Name 4", " 12500", "22100");
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Person Name 5", " 21500", "552100");
        list_all_peoplesList.add(loan);


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
