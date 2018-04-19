package com.scleroid.financematic.fragments.passbook;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.tempModels.Passbook;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 2/3/18
 */
@Deprecated
public class PassbookFragment extends BaseFragment {
	private List<Passbook> passbookList = new ArrayList<>();
	private RecyclerView recyclerView;
	private PassbookAdapter mAdapter;

	public PassbookFragment() {
		// Required empty public constructor
	}

	public static PassbookFragment newInstance(String param1, String param2) {
		PassbookFragment fragment = new PassbookFragment();
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
		recyclerView = rootView.findViewById(R.id.passbook_my_recycler);

		mAdapter = new PassbookAdapter(passbookList);

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
								Passbook passbook = passbookList.get(position);
								Toast.makeText(getActivity().getApplicationContext(),
										passbook.getPassbook_received_money() + " is selected!",
										Toast.LENGTH_SHORT).show();
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
		return R.layout.passbook;
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
		Passbook passbook = new Passbook("14/6/2018 ", "Person Name 1", "50,000", "");
		passbookList.add(passbook);
		passbook = new Passbook("12/6/2018 ", "Person Name 2", "", "3000");
		passbookList.add(passbook);
		passbook = new Passbook("12/6/2018 ", "Person Name 3", "", "4000");
		passbookList.add(passbook);
		passbook = new Passbook("12/6/2018 ", "Person Name 1", "1200", "");
		passbookList.add(passbook);
		passbook = new Passbook("1/2/2018 ", "Person Name 2", "4000", "");
		passbookList.add(passbook);

		passbook = new Passbook("10/1/2018 ", "Person Name 3", "", "2000");
		passbookList.add(passbook);

		passbook = new Passbook("12/1/2018 ", "Person Name 1", "4000", "");
		passbookList.add(passbook);


		// notify adapter about data set changes
		// so that it will render the list with new data
		mAdapter.notifyDataSetChanged();
	}


}
