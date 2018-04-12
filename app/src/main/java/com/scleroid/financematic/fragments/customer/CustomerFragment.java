package com.scleroid.financematic.fragments.customer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;
import com.scleroid.financematic.utils.ui.RupeeTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by scleroid on 4/4/18.
 */


public class CustomerFragment extends BaseFragment {
	private static final String CUSTOMER_ID = "customer_id";
	@BindView(R.id.name_text_view)
	TextView nameTextView;
	@BindView(R.id.mobile_text_view)
	TextView mobileTextView;
	@BindView(R.id.address_text_view)
	TextView addressTextView;
	Unbinder unbinder;
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

		// row click listener
		RecyclerTouchListener listener =
				new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView,
						new RecyclerTouchListener.ClickListener() {
							@Override
							public void onClick(View view, int position) {
								Loan profile = loanList.get(position);
								Toast.makeText(getActivity().getApplicationContext(),
										profile.getCustId() + " is selected!", Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onLongClick(View view, int position) {

							}
						});
		//	recyclerView.addOnItemTouchListener(listener);


		unbinder = ButterKnife.bind(this, rootView);
		return rootView;


	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
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
		customerViewModel.getItemList().observe(this, items -> {
			loanList = items;
			mAdapter.setLoanList(loanList);
			updateTotalLoanAmt();

		});

		customerViewModel.getCustomerLiveData().observe(this, item -> {
			theCustomer = item;
			updateUi();
		});
	}

	private void updateUi() {
		nameTextView.setText(theCustomer.getName());
		mobileTextView.setText(theCustomer.getMobileNumber());
		addressTextView.setText(theCustomer.getAddress());
	}

	private void updateTotalLoanAmt() {
		for (Loan loan : loanList) {
			totalLoan += loan.getLoanAmt().intValue();
		}
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
}
