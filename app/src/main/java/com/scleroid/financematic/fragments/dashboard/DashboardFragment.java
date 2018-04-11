package com.scleroid.financematic.fragments.dashboard;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.fragments.passbook.PassbookFragment;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RecyclerTouchListener;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;


public class DashboardFragment extends BaseFragment<DashboardViewModel> {

    @Inject
    TextViewUtils textViewUtils;
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
    @Inject
    ActivityUtils activityUtils;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LoanAdapter mAdapter;
    private DashboardViewModel dashBoardViewModel;
	@Inject
	LocalCustomerLab localCustomerLab;
	@Inject
	LocalLoanLab localLoanLab;

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @return layout resource id
     */
    @Override
    public int getLayoutId() {
        return R.layout.dashboard;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.setDebug(true);
        // Inflate the layout for this fragment
        View rootView = getRootView();
        // unbinder = ButterKnife.bind(this, rootView);


//Intent
        firstFragment = rootView.findViewById(R.id.total_amount_text_view);
        firstFragment.setOnClickListener(v -> activityUtils.loadFragment(new PassbookFragment(), getFragmentManager()));

        // recyclerView = rootView.findViewById(R.id.recycler_view_dashboard);

        // prepareTempDashBoardModelData();


        setupRecyclerView();
        textViewUtils.textViewExperiments(upcomingEventsTextView);
        textViewUtils.textViewExperiments(totalAmountTextView);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public DashboardViewModel getViewModel() {
        dashBoardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        return dashBoardViewModel;
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy
     * instantiated.  It can be used to do final initialization once these pieces are in place, such
     * as retrieving views or restoring state.  It is also useful for fragments that use {@link
     * #setRetainInstance(boolean)} to retain their instance, as this callback tells the fragment
     * when it is fully associated with the new activity instance.  This is called after {@link
     * #onCreateView} and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeToLiveData();
    }

	private List<Installment> installments = new ArrayList<>();

	public DashboardFragment() {
		// Required empty public constructor
	}

    private void subscribeToLiveData() {
	    dashBoardViewModel.getUpcomingInstallments().observe(this,
                items -> {
	                mAdapter.setLoanList(items);
	                installments = items;
                });
    }

    private void setupRecyclerView() {
	    mAdapter = new LoanAdapter(new ArrayList<>(), localLoanLab, localCustomerLab);

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
	            if (installments.isEmpty()) {
		            Toasty.error(getActivity(), "THe list is empty, something wrong here").show();
	            }
	            Installment loan = installments.get(position);
	            Toast.makeText(getActivity().getApplicationContext(),
			            loan.getLoan().getCustomer().getName() + " is selected!",
			            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }




}
