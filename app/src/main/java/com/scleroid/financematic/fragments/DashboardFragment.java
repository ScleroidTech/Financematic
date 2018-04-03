package com.scleroid.financematic.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.LoanAdapter;
import com.scleroid.financematic.data.TempDashBoardModel;
import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.RecyclerTouchListener;
import com.scleroid.financematic.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by scleroid on 2/3/18.
 */


public class DashboardFragment extends Fragment {

    TextViewUtils textViewUtils = new TextViewUtils();
    TextView firstFragment;
    @BindView(R.id.total_amount_text_view)
    TextView totalAmountTextView;
    @BindView(R.id.remaining_amount_text_view)
    TextView remainingAmountTextView;
    @BindView(R.id.lent_amount_text_view)
    TextView lentAmountTextView;
    @BindView(R.id.available_amount_text_view)
    TextView availableAmountTextView;
    @BindView(R.id.upcoming_events_text_view)
    TextView upcomingEventsTextView;
    @BindView(R.id.recycler_view_dashboard)
    RecyclerView recyclerViewDashboard;
    Unbinder unbinder;
    private List<TempDashBoardModel> loanList = new ArrayList<>();
    private LoanAdapter mAdapter;
    private ActivityUtils activityUtils = new ActivityUtils();

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ButterKnife.setDebug(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dashboard, container, false);
        unbinder = ButterKnife.bind(this, rootView);


//Intend
        firstFragment = rootView.findViewById(R.id.total_amount_text_view);
        firstFragment.setOnClickListener(v -> activityUtils.loadFragment(new PassbookFragment(), getFragmentManager()));

        // recyclerView = rootView.findViewById(R.id.recycler_view_dashboard);

        prepareTempDashBoardModelData();


        setupRecyclerView();
        textViewUtils.textViewExperiments(upcomingEventsTextView);
        textViewUtils.textViewExperiments(totalAmountTextView);

        return rootView;
    }


    private void setupRecyclerView() {
        mAdapter = new LoanAdapter(loanList);

        recyclerViewDashboard.setHasFixedSize(true);


        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewDashboard.setLayoutManager(mLayoutManager);


        recyclerViewDashboard.setItemAnimator(new DefaultItemAnimator());

        recyclerViewDashboard.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewDashboard.getContext(),
                DividerItemDecoration.VERTICAL);
        //  recyclerView.addItemDecoration(dividerItemDecoration);


        // row click listener
        recyclerViewDashboard.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerViewDashboard, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TempDashBoardModel loan = loanList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), loan.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    private void prepareTempDashBoardModelData() {
        TempDashBoardModel loan = new TempDashBoardModel("Shahrukh Khan", "2000", "5 Feb 2018");
        loanList.add(loan);
        loan = new TempDashBoardModel("Akshay Kumar", "1000", "2 Feb 2018");
        loanList.add(loan);
        loan = new TempDashBoardModel("Amitabh Bachchan", "1500", "1 Feb 2018");
        loanList.add(loan);


        // notify adapter about data set changes
        // so that it will render the list with new data
        //mAdapter.notifyDataSetChanged();
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
