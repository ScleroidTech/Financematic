package com.scleroid.financematic.fragments.customer;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.adapter.ProfileAdapter;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.tempModels.Profile;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by scleroid on 4/4/18.
 */


public class CustomerFragment extends BaseFragment {
	private List<Profile> profileList = new ArrayList<>();
	private RecyclerView recyclerView;
	private ProfileAdapter mAdapter;

	public CustomerFragment() {
		// Required empty public constructor
	}

	public static CustomerFragment newInstance(String param1, String param2) {
		CustomerFragment fragment = new CustomerFragment();
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
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View rootView = getRootView();

		Timber.d("whats the rootview" + rootView);
		recyclerView = rootView.findViewById(R.id.profile_my_recycler);

		mAdapter = new ProfileAdapter(profileList);

		recyclerView.setHasFixedSize(true);

		/* recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));*/

		// vertical RecyclerView
		// keep movie_list_row.xml width to `match_parent`
		RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity().getApplicationContext());

		// horizontal RecyclerView
		// keep movie_list_row.xml width to `wrap_content`
		// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
		// (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

		recyclerView.setLayoutManager(mLayoutManager);


		recyclerView.setItemAnimator(new DefaultItemAnimator());

		recyclerView.setAdapter(mAdapter);

		// row click listener
		recyclerView.addOnItemTouchListener(
				new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView,
						new RecyclerTouchListener.ClickListener() {
							@Override
							public void onClick(View view, int position) {
								Profile profile = profileList.get(position);
								Toast.makeText(getActivity().getApplicationContext(),
										profile.getTitle() + " is selected!", Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onLongClick(View view, int position) {

							}
						}));

		prepareLoanData();

		return rootView;


	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_profile;
	}

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {

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
		Profile profile = new Profile("Loan No 1", "22 Feb 2018", "20000", "1000", "28 dec 2018");
		profileList.add(profile);
		profile = new Profile("Loan No 2", "2 Jan 2018", "5000", "3000", "6 Jun 2018");
		profileList.add(profile);
		profile = new Profile("Loan No 2", "1 Jan 2018", "10000", "4000", "6 Aug 2018");
		profileList.add(profile);


		mAdapter.notifyDataSetChanged();
	}
}
