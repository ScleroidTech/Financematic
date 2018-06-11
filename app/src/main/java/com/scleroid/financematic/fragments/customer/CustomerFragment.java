package com.scleroid.financematic.fragments.customer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.fragments.RegisterLoanFragment;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by scleroid on 4/4/18.
 */


public class CustomerFragment extends BaseFragment {
	@BindView(R.id.fab)
	FloatingActionButton fab;

	private ActivityUtils activityUtils = new ActivityUtils();
	private static final String CUSTOMER_ID = "customer_id";
	@BindView(R.id.name_text_view)
	TextView nameTextView;
	@BindView(R.id.mobile_text_view)
	TextView mobileTextView;
	@BindView(R.id.address_text_view)
	TextView addressTextView;

	@BindView(R.id.total_loan_text_view)
	RupeeTextView totalLoanTextView;
	private List<Loan> loanList = new ArrayList<>();
	private RecyclerView recyclerView;
	private CustomerAdapter mAdapter;
	private CustomerViewModel customerViewModel;
	private int customerId;
	private Customer theCustomer;
	private int totalLoan;


	public CustomerFragment() {
		// Required empty public constructor
	}

	public static CustomerFragment newInstance(int customerId) {
		CustomerFragment fragment = new CustomerFragment();
		Bundle args = new Bundle();
		args.putInt(CUSTOMER_ID, customerId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@BindView(R.id.empty_card)
	CardView emptyCard;


	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_profile;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View rootView = getRootView();
		Bundle bundle = getArguments();
		if (bundle != null) customerId = bundle.getInt(CUSTOMER_ID);
		customerViewModel.setCurrentCustomerId(customerId);

		Timber.d("whats the rootview" + rootView);
		recyclerView = rootView.findViewById(R.id.profile_my_recycler);

		mAdapter = new CustomerAdapter(loanList);

		recyclerView.setHasFixedSize(true);

		//	updateUi();

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


		updateView(loanList);

		return rootView;


	}

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {
		customerViewModel.getLoanList().observe(this, items -> {
			updateView(items.data);

		});

		customerViewModel.getCustomerLiveData().observe(this, item -> {
			theCustomer = item.data;
			updateUi();
		});
	}

	private void updateView(final List<Loan> items) {
		if (items == null || items.isEmpty()) {
			emptyCard.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else {
			emptyCard.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);

			loanList = items;
			mAdapter.setLoanList(loanList);
			updateTotalLoanAmt();

		}
	}

	private void updateUi() {
		if (theCustomer == null) return;
		nameTextView.setText(theCustomer.getName());
		mobileTextView.setText(theCustomer.getMobileNumber());
		addressTextView.setText(theCustomer.getAddress());
		setTitle();
	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(),
				"Customer Id." + theCustomer.getCustomerId());
	}

	private void updateTotalLoanAmt() {
	/*	for (Loan loan : loanList) {
			totalLoan += loan.getLoanAmt().intValue();
		}
		totalLoanTextView.setText(totalLoan + "");*/

		totalLoan =
				loanList.stream()
				.filter(o -> o.getLoanAmt() != null)
				.mapToInt(o -> o.getLoanAmt().intValue())
				.sum();
		totalLoanTextView.setText(totalLoan + "");
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public BaseViewModel getViewModel() {
		customerViewModel =
				ViewModelProviders.of(this, viewModelFactory).get(CustomerViewModel.class);

		return customerViewModel;
	}


	@OnClick(R.id.fab)
	public void onViewClicked() {
		activityUtils.loadFragment(RegisterLoanFragment.newInstance(customerId),
				getFragmentManager());
	}


	@OnClick({R.id.fab, R.id.mobile_text_view, R.id.address_text_view})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.fab:
				activityUtils.loadFragment(RegisterLoanFragment.newInstance(customerId),
						getFragmentManager());
				break;
			case R.id.mobile_text_view:
				handleCallClick();
				break;
			case R.id.address_text_view:
				handleAddressClick();
				break;
		}
	}

	private void handleCallClick() {
		String phone = theCustomer.getMobileNumber();
		Timber.d(phone + " of person " + theCustomer.getName());
		Events.placeCall makeACall = new Events.placeCall(phone);

		GlobalBus.getBus().post(makeACall);
	}

	private void handleAddressClick() {
		String phone = theCustomer.getAddress();
		Timber.d(phone + " of person " + theCustomer.getName());
		Events.goToAddress makeACall = new Events.goToAddress(phone);

		GlobalBus.getBus().post(makeACall);
	}
}
