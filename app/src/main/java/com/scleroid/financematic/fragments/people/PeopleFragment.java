package com.scleroid.financematic.fragments.people;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.utils.network.Resource;
import com.scleroid.financematic.utils.network.Status;
import com.scleroid.financematic.utils.ui.ActivityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by scleroid on 28/2/18.
 */

public class PeopleFragment extends BaseFragment {


	@Inject
	ActivityUtils activityUtils;


	TextView firstFragment;
	@Nullable
	@BindView(R.id.simpleSearchView)
	SearchView simpleSearchView;
	@Nullable
	@BindView(R.id.people_recycler_view)
	RecyclerView peopleRecyclerView;


	@Nullable
	@BindView(R.id.no_address_title)
	TextView noAddressTitle;
	@Nullable
	@BindView(R.id.no_address_subtitle)
	TextView noAddressSubtitle;
	@Nullable
	@BindView(R.id.empty_card)
	CardView emptyCard;
	@Nullable
	private List<Customer> customers = new ArrayList<>();
	private PeopleViewModel peopleViewModel;
	@Nullable
	private PeopleAdapter mAdapter;

	public PeopleFragment() {
		// Required empty public constructor
	}

	@NonNull
	public static PeopleFragment newInstance(String param1, String param2) {
		PeopleFragment fragment = new PeopleFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
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
		mAdapter = new PeopleAdapter(this.customers);

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

		setTitle();

		return rootView;


	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(), "List of Customers");
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_people;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	protected void subscribeToLiveData() {
		peopleViewModel.getItemList().observe(this,
				items -> {
					//	sort(items);
					updateView(items);
				});
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public PeopleViewModel getViewModel() {
		peopleViewModel =
				ViewModelProviders.of(this, viewModelFactory).get(PeopleViewModel.class);
		return peopleViewModel;
	}

	private void updateView(@Nullable final Resource<List<Customer>> items) {
		emptyCard.setVisibility(View.VISIBLE);
		peopleRecyclerView.setVisibility(View.GONE);

		if (items == null) {
			noAddressTitle.setText(items.status.toString());
			noAddressSubtitle.setText(items.message);

		} else if (items.data.isEmpty() && items.status.equals(Status.LOADING)) {
			noAddressTitle.setText(items.status.toString());
			noAddressSubtitle.setText(items.message);

		} else {
			emptyCard.setVisibility(View.GONE);
			peopleRecyclerView.setVisibility(View.VISIBLE);
			mAdapter.setCustomerList(items.data);
			customers = items.data;
		}
	}

	private void sort(@NonNull final List<Customer> transactions) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			transactions.removeIf(
					transaction -> transaction.getLoans() == null || transaction.getLoans()
							.isEmpty());
			//	transactions.sort(Comparator.comparing(Installment::getInstallmentDate));
		} else {
			for (Iterator it = transactions.iterator(); it.hasNext(); ) {

				if (predicate((Customer) it.next())) {

					it.remove();

				}

			}
		}
	}

	private boolean predicate(final Customer next) {
		return next.getLoans() == null || next.getLoans().isEmpty();
	}
}
