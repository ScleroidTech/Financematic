package com.scleroid.financematic.fragments.people;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.PeopleAdapter;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.tempModels.List_all_peoples;
import com.scleroid.financematic.utils.ActivityUtils;
import com.scleroid.financematic.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 28/2/18.
 */

public class PeopleFragment extends BaseFragment {
    private final ActivityUtils activityUtils = new ActivityUtils();
    SearchView sv;
    TextView firstFragment;
    private List<List_all_peoples> list_all_peoplesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PeopleAdapter mAdapter;

    public PeopleFragment() {
        // Required empty public constructor
    }

    public static PeopleFragment newInstance(String param1, String param2) {
        PeopleFragment fragment = new PeopleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return layout resource id
     */
    @Override
    public int getLayoutId() {
        return R.layout.list_all_peoples;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = getRootView();

        sv = rootView.findViewById(R.id.simpleSearchView);

        //Intend
     /*   firstFragment = rootView.findViewById(R.id.button_list);
        firstFragment.setOnClickListener(v -> activityUtils.loadFragment(new LoanDetailsFragment(), getFragmentManager()));*/

//recyclerView
        recyclerView = rootView.findViewById(R.id.list_all_peoples_recycler);

        mAdapter = new PeopleAdapter(this.list_all_peoplesList);

        recyclerView.setHasFixedSize(true);
        // vertical RecyclerView
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

      /*  //SET ITS PROPETRIES
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        final PeopleAdapter adapter=new PeopleAdapter(this,getPlayers());
        recyclerView.setAdapter(adapter);*/
        //SEARCH
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                mAdapter.getFilter().filter(query);
                return false;
            }
        });


        prepareLoanData();

        return rootView;


    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    private void prepareLoanData() {
        List_all_peoples loan = new List_all_peoples("Customer Name 1", 12100, 200000);
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Customer Name 2", 12100, 22100);
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Customer Name 3", 1500, 842100);
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Customer Name 4", 12500, 22100);
        list_all_peoplesList.add(loan);
        loan = new List_all_peoples("Customer Name 5", 21500, 552100);
        list_all_peoplesList.add(loan);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }


}
