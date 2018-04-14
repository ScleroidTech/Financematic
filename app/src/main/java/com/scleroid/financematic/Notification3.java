package com.scleroid.financematic;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.fragments.dashboard.DashboardAdapter;
import com.scleroid.financematic.fragments.dashboard.DashboardViewModel;
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

/**
 * Created by scleroid on 14/4/18.
 */



        import android.arch.lifecycle.ViewModelProviders;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
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


public class Notification3 extends BaseFragment<DashboardViewModel> {

    @Inject
    TextViewUtils textViewUtils;
    TextView firstFragment;



    @BindView(R.id.recycler_view_dashboard)
    RecyclerView recyclerViewDashboard;
    Unbinder unbinder;


    @Inject
    LocalCustomerLab localCustomerLab;
    @Inject
    LocalLoanLab localLoanLab;
    private DashboardAdapter mAdapter;
    private DashboardViewModel dashBoardViewModel;
    private List<Installment> installments = new ArrayList<>();

    public Notification3() {
        // Required empty public constructor
    }

    public static com.scleroid.financematic.fragments.dashboard.DashboardFragment newInstance(String param1, String param2) {
        com.scleroid.financematic.fragments.dashboard.DashboardFragment fragment = new com.scleroid.financematic.fragments.dashboard.DashboardFragment();
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
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.setDebug(true);
        // Inflate the layout for this fragment
        View rootView = getRootView();
        // unbinder = ButterKnife.bind(this, rootView);



        // recyclerView = rootView.findViewById(R.id.recycler_view_dashboard);

        // prepareTempDashBoardModelData();


        setupRecyclerView();



        return rootView;
    }

    /**
     * @return layout resource id
     */
    @Override
    public int getLayoutId() {
        return R.layout.notification;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    @Override
    public DashboardViewModel getViewModel() {
        dashBoardViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        return dashBoardViewModel;
    }

    private void setupRecyclerView() {
        mAdapter = new DashboardAdapter(new ArrayList<>(), localLoanLab, localCustomerLab);

        recyclerViewDashboard.setHasFixedSize(true);


        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
        // (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewDashboard.setLayoutManager(mLayoutManager);


        recyclerViewDashboard.setItemAnimator(new DefaultItemAnimator());

        recyclerViewDashboard.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerViewDashboard.getContext(),
                        DividerItemDecoration.VERTICAL);
        //  recyclerView.addItemDecoration(dividerItemDecoration);


        // row click listener
        RecyclerTouchListener recyclerTouchListener =
                new RecyclerTouchListener(getActivity().getApplicationContext(),
                        recyclerViewDashboard, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (installments.isEmpty()) {
                            Toasty.error(getActivity(), "THe list is empty, something wrong here")
                                    .show();
                        }
                        Installment loan = installments.get(position);
                        Toast.makeText(getActivity().getApplicationContext(),
                                loan.getLoan().getCustomer().getName() + " is selected!",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                });
        // TODO not needed, should be removed recyclerViewDashboard.addOnItemTouchListener
        // (recyclerTouchListener);
    }


    @Override
    protected void subscribeToLiveData() {
        dashBoardViewModel.getUpcomingInstallments().observe(this,
                items -> {
                    mAdapter.setInstallmentList(items);
                    installments = items;
                });
    }


}
