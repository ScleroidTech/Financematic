package com.scleroid.financematic.fragments.dashboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.lab.LocalLoanLab;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.repo.CustomerRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.CurrencyStringUtils;
import com.scleroid.financematic.utils.ui.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.scleroid.financematic.fragments.dashboard.DashboardViewModel.RANGE;


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {


	private final LocalLoanLab localLoanLab;
	private final LocalCustomerLab localCustomerLab;
	@Inject
	DateUtils dateUtils = new DateUtils();
	private List<Installment> installmentList;

//add manoj
	@Inject
	ActivityUtils activityUtils;


	public DashboardAdapter(List<Installment> installmentList, LocalLoanLab localLoanLab,
	                        LocalCustomerLab localCustomerLab) {
		this.installmentList = installmentList;
		this.localLoanLab = localLoanLab;
		this.localCustomerLab = localCustomerLab;
		//this.filteredInstallments = filterResults(installmentList);
	}

/*TODO Work in Progress ,Add this & remove other constructor
    public DashboardAdapter(List<Loan> installmentList) {
        this.installmentList = installmentList;
    }*/

	public List<Installment> getFilteredInstallments() {
		return filterResults(installmentList);
	}

	private List<Installment> filterResults(List<Installment> installments) {

		if (installments == null) return new ArrayList<>();
		return Stream.of(installments)
				.filter(installment -> dateUtils.isThisDateWithinRange(
						installment.getInstallmentDate(), RANGE) == true)
				.collect(Collectors.toList());
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_dashboard, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Installment dashBoardModel = installmentList.get(position);





		if (dashBoardModel.getLoan() == null) {
			Timber.wtf(" loan is empty for " + dashBoardModel.toString());
			return;
		}

		if (dashBoardModel.getLoan().getCustomer() == null) {
			Timber.wtf(" customer is empty for " + dashBoardModel.getLoan().toString());
			return;
		}
		if (
				dashBoardModel.getLoan().getCustomer().getName() == null) {
			Timber.wtf(" name is empty for " + dashBoardModel.getLoan().getCustomer().toString());
			return;
		}
		holder.setData(dashBoardModel);
		holder.itemView.setTag(dashBoardModel);



		//add manoj
		holder.dashboarditemcardview.setOnClickListener(v -> {
			Timber.wtf("It's clicked dadadad");
			Events.openCustomerFragment openCustomerFragment =
					new Events.openCustomerFragment(dashBoardModel.getLoan().getCustId());
			GlobalBus.getBus().post(openCustomerFragment);
		});



	/*	localLoanLab
				.getRxItem(dashBoardModel.getLoanAcNo())
				.subscribeOn(Schedulers.computation())
				.subscribe(loan -> {
					String name = localCustomerLab
							.getRxItem(loan.getCustId())
							.getName();
					holder.customerNameTextView.setText(name);
				})
				.dispose();*/


	}

	@Override
	public int getItemCount() {
		return installmentList.size();
	}

	public void setInstallmentList(final List<Installment> installmentList) {
		//Timber.d("What's the list" + installmentList.isEmpty() + installmentList.toString());
		List<Installment> filterResults = filterResults(installmentList);
		Timber.d("What's the list" + filterResults.isEmpty() + filterResults.toString());
		this.installmentList = filterResults;
		//this.installmentList = installmentList;
		notifyDataSetChanged();
	}


	static class MyViewHolder extends RecyclerView.ViewHolder {




		Installment installment;
		@Inject
		DateUtils dateUtils = new DateUtils();
		@Inject
		CurrencyStringUtils currencyStringUtils = new CurrencyStringUtils();
		@Inject
		CustomerRepo customerRepo;
		@Inject
		LoanRepo loanRepo;
		@BindView(R.id.customer_name_text_view)
		TextView customerNameTextView;
		@BindView(R.id.amount_text_view)
		TextView amountTextView;
		@BindView(R.id.due_date_text_view)
		TextView dueDateTextView;
		@BindView(R.id.time_remaining_text_view)
		TextView timeRemainingTextView;
		@BindView(R.id.call_button)
		Button callButton;
		@BindView(R.id.delay_button)
		Button delayButton;
		//add manoj
		@BindView(R.id.dashboard_item_cardview)
		CardView dashboarditemcardview;



		MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

		}

		private void setData(final Installment installment) {
			this.installment = installment;
			customerNameTextView.setText("loading ...");
			//TODO Remove local call

			if (installment.getLoan().getCustomer() != null) {
				customerNameTextView.setText(installment.getLoan().getCustomer().getName());
			}
			BigDecimal expectedAmt = installment.getExpectedAmt();
			if (expectedAmt == null) {
				Timber.wtf("THere's no data available " + installment.toString());
				amountTextView.setText("fetching");
			} else {
				amountTextView.setText(
						currencyStringUtils.bindNumber(
								expectedAmt.intValueExact()));
			}
			dueDateTextView.setText(
					dateUtils.getFormattedDate(installment.getInstallmentDate()));
			timeRemainingTextView.setText(
					dateUtils.differenceOfDates(installment.getInstallmentDate()));
			if (installment.getLoan().getCustomer().getMobileNumber() == null) {
				callButton.setEnabled(false);
				Events.showToast showToast =
						new Events.showToast("We don't have this number", "error");
				GlobalBus.getBus().post(showToast);

			}
		}


		@OnClick({R.id.call_button, R.id.delay_button, R.id.dashboard_item_cardview})
		public void onViewClicked(View view) {
			switch (view.getId()) {
				case R.id.call_button:
					handleCallClick();
					break;
				case R.id.delay_button:
					handleDelay();
					break;
				case R.id.dashboard_item_cardview:
					break;
			}
		}

		private void handleDelay() {
			Timber.d("delay of Payment" + installment.getLoan().getCustomer().getName());
			Events.openDelayFragment delayFragment =
					new Events.openDelayFragment(installment.getInstallmentId());

			GlobalBus.getBus().post(delayFragment);
		}

		private void handleCallClick() {
			String phone = installment.getLoan().getCustomer().getMobileNumber();
			Timber.d(phone + " of person " + installment.getLoan().getCustomer().getName());
			Events.placeCall makeACall = new Events.placeCall(phone);

			GlobalBus.getBus().post(makeACall);
		}



	}
}
