package com.scleroid.financematic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.data.local.Session;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by scleroid on 16/4/18.
 */

public class NotificationActivity extends BaseFragment<NotificationViewModel> {


	@Inject
	TextViewUtils textViewUtils;
	@Nullable
	@BindView(R.id.total_amount_text_view)
	RupeeTextView totalAmountTextView;
	@Nullable
	@BindView(R.id.remaining_amount_text_view)
	RupeeTextView remainingAmountTextView;
	@Nullable
	@BindView(R.id.lent_amount_text_view)
	RupeeTextView lentAmountTextView;

	@Nullable
	@BindView(R.id.upcoming_events_text_view)
	TextView upcomingEventsTextView;
	@Nullable
	@BindView(R.id.recycler_view_dashboard)
	RecyclerView recyclerViewDashboard;

	@Inject
	ActivityUtils activityUtils;

	@Inject
	LocalCustomerLab localCustomerLab;
	@Inject
	LocalLoanLab localLoanLab;

	@Nullable
	@BindView(R.id.total_amount_title_text_view)
	TextView totalAmountTitleTextView;

	@Nullable
	@BindView(R.id.lent_amount_title_text_view)
	TextView lentAmountTitleTextView;
	@Nullable
	@BindView(R.id.empty_card)
	CardView emptyCard;
	@Inject
	Session session;
	private NotificationAdapter mAdapter;
	private NotificationViewModel notificationViewModel;
	@Nullable
	private List<Installment> installments = new ArrayList<>();
	@Nullable
	private List<Loan> loanList = new ArrayList<>();

	public NotificationActivity() {
		// Required empty public constructor
	}

	@NonNull
	public static NotificationActivity newInstance() {
		NotificationActivity fragment = new NotificationActivity();
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


		noticationmethod();
		// recyclerView = rootView.findViewById(R.id.recycler_view_dashboard);11

		// prepareTempDashBoardModelData();


		setupRecyclerView();
			/*textViewUtils.textViewExperiments(upcomingEventsTextView);
			textViewUtils.textViewExperiments(totalAmountTextView);
*/
		setTitle();
		updateView(installments);
		return rootView;
	}

	public void noticationmethod() {
		//Notification code
		AlarmManager alarmManager =
				(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		//AlarmReceiver notificationIntent = new AlarmReceiver();
		Intent notificationIntent = new Intent(this.getActivity(), AlarmReceiver.class);
		PendingIntent broadcast =
				PendingIntent.getBroadcast(this.getActivity(), 100, notificationIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 3);
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

		// Intent i=new Intent(this.getActivity(),AlarmReceiver.class);
		/* notificationIntent.putExtra("id", installment.getLoan().getCustomer()+"");
		 notificationIntent.putExtra("name", user.getUserFullName());
		 this.getActivity().startActivity(notificationIntent);
*/
		return;
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.notification;
	}

	@Override
	protected void subscribeToLiveData() {
		notificationViewModel.getUpcomingInstallments().observe(this,
				items -> {
					updateView(items);
				});

		notificationViewModel.getLoans().observe(this, items -> {
			if (items.data == null) {
				return;
			}
			loanList = items.data;
			updateUi();
		});
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public NotificationViewModel getViewModel() {
		notificationViewModel =
				ViewModelProviders.of(this, viewModelFactory)
						.get(NotificationViewModel.class);
		return notificationViewModel;
	}

	private void updateUi() {

	}

	private void setupRecyclerView() {
		mAdapter = new NotificationAdapter(new ArrayList<>());

		recyclerViewDashboard.setHasFixedSize(true);


		// vertical RecyclerView
		// keep movie_list_row.xml width to `match_parent`
		RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity().getApplicationContext());

		// horizontal RecyclerView
		// keep movie_list_row.xml width to `wrap_content`


		recyclerViewDashboard.setLayoutManager(mLayoutManager);


		recyclerViewDashboard.setItemAnimator(new DefaultItemAnimator());

		recyclerViewDashboard.setAdapter(mAdapter);

	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(), "Notifaction");
	}

	private void updateView(@Nullable final List<Installment> items) {
		if (items == null || items.isEmpty()) {
			emptyCard.setVisibility(View.VISIBLE);
			recyclerViewDashboard.setVisibility(View.GONE);
		} else {
			emptyCard.setVisibility(View.GONE);
			recyclerViewDashboard.setVisibility(View.VISIBLE);
			sort(items);
			mAdapter.setInstallmentList(items);
			installments = items;
		}

	}

	private void sort(@Nullable final List<Installment> transactions) {
		if (transactions == null) return;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

			transactions.sort(Comparator.comparing(Installment::getInstallmentDate));
		} else {

			Collections.sort(transactions,
					(m1, m2) -> m1.getInstallmentDate().compareTo(m2.getInstallmentDate()));
		}
	}

}
