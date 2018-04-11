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
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by scleroid on 28/2/18.
 */

public class PeopleFragment extends BaseFragment {
	@Inject
	ActivityUtils activityUtils;

	TextView firstFragment;
	@BindView(R.id.simpleSearchView)
	SearchView simpleSearchView;
	@BindView(R.id.people_recycler_view)
	RecyclerView peopleRecyclerView;
	Unbinder unbinder;
	private List<PeopleModel> peopleModelList = new ArrayList<>();

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
		return R.layout.fragment_people;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = getRootView();


		//Intend
     /*   firstFragment = rootView.findViewById(R.id.button_list);
        firstFragment.setOnClickListener(v -> activityUtils.loadFragment(new LoanDetailsFragment
        (), getFragmentManager()));*/

//peopleRecyclerView
		mAdapter = new PeopleAdapter(this.peopleModelList);

		peopleRecyclerView.setHasFixedSize(true);
		// vertical RecyclerView
		RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity());

		// horizontal RecyclerView
		// keep movie_list_row.xml width to `wrap_content`
		// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
		// (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

		peopleRecyclerView.setLayoutManager(mLayoutManager);


		peopleRecyclerView.setItemAnimator(new DefaultItemAnimator());

		peopleRecyclerView.setAdapter(mAdapter);

		// row click listener
		peopleRecyclerView.addOnItemTouchListener(
				new RecyclerTouchListener(getActivity(), peopleRecyclerView,
						new RecyclerTouchListener.ClickListener() {
							@Override
							public void onClick(View view, int position) {
								PeopleModel loan = peopleModelList.get(position);
								Toast.makeText(getActivity(),
										loan.getList_received_amoun() + " is selected!",
										Toast.LENGTH_SHORT).show();


							}

							@Override
							public void onLongClick(View view, int position) {

							}
						}));

      /*  //SET ITS PROPERTIES
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        peopleRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        final PeopleAdapter adapter=new PeopleAdapter(this,getPlayers());
        peopleRecyclerView.setAdapter(adapter);*/
		//SEARCH
		simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
		PeopleModel loan = new PeopleModel("Customer Name 1", 12100, 200000);
		peopleModelList.add(loan);
		loan = new PeopleModel("Customer Name 2", 12100, 22100);
		peopleModelList.add(loan);
		loan = new PeopleModel("Customer Name 3", 1500, 842100);
		peopleModelList.add(loan);
		loan = new PeopleModel("Customer Name 4", 12500, 22100);
		peopleModelList.add(loan);
		loan = new PeopleModel("Customer Name 5", 21500, 552100);
		peopleModelList.add(loan);


		// notify adapter about data set changes
		// so that it will render the list with new data
		mAdapter.notifyDataSetChanged();
	}


}
