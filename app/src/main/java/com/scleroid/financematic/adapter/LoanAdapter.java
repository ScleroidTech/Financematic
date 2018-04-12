package com.scleroid.financematic.adapter;

import android.support.annotation.NonNull;
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


public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {


	private final LocalLoanLab localLoanLab;
	private final LocalCustomerLab localCustomerLab;
	@Inject
	DateUtils dateUtils = new DateUtils();
	private List<Installment> installmentList;


	public LoanAdapter(List<Installment> installmentList, LocalLoanLab localLoanLab,
	                   LocalCustomerLab localCustomerLab) {
		this.installmentList = installmentList;
		this.localLoanLab = localLoanLab;
		this.localCustomerLab = localCustomerLab;
		//this.filteredInstallments = filterResults(installmentList);
	}

/*TODO Work in Progress ,Add this & remove other constructor
    public LoanAdapter(List<Loan> installmentList) {
        this.installmentList = installmentList;
    }*/

	public List<Installment> getFilteredInstallments() {
		return filterResults(installmentList);
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_dashboard, parent, false);

		return new MyViewHolder(itemView);
	}

	private List<Installment> filterResults(List<Installment> installments) {

		if (installments == null) return new ArrayList<>();
		return Stream.of(installments)
				.filter(installment -> dateUtils.isThisDateWithinRange(
						installment.getInstallmentDate(), RANGE) == true)
				.collect(Collectors.toList());
	}

	public void setInstallmentList(final List<Installment> installmentList) {
		//Timber.d("What's the list" + installmentList.isEmpty() + installmentList.toString());
		List<Installment> filterResults = filterResults(installmentList);
		Timber.d("What's the list" + filterResults.isEmpty() + filterResults.toString());
		this.installmentList = filterResults;
		//this.installmentList = installmentList;
		notifyDataSetChanged();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Installment dashBoardModel = installmentList.get(position);
		holder.itemView.setTag(dashBoardModel);
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
		/*TODO NO longer needed i guess
		localLoanLab
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


	static class MyViewHolder extends RecyclerView.ViewHolder {
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

		MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);

		}

		private void setData(final Installment dashBoardModel) {
			customerNameTextView.setText("loading ...");
			//TODO Remove local call

			if (dashBoardModel.getLoan().getCustomer() != null) {
				customerNameTextView.setText(dashBoardModel.getLoan().getCustomer().getName());
			}
			BigDecimal expectedAmt = dashBoardModel.getExpectedAmt();
			if (expectedAmt == null) {
				Timber.wtf("THere's no data available " + dashBoardModel.toString());
				amountTextView.setText("fetching");
			} else {
				amountTextView.setText(
						currencyStringUtils.bindNumber(
								expectedAmt.intValueExact()));
			}
			dueDateTextView.setText(
					dateUtils.getFormattedDate(dashBoardModel.getInstallmentDate()));
			timeRemainingTextView.setText(
					dateUtils.differenceOfDates(dashBoardModel.getInstallmentDate()));
		}

		@OnClick(R.id.call_button)
		public void onCallButtonClicked() {
		}

		@OnClick(R.id.delay_button)
		public void onDelayButtonClicked() {
		}

	}
}
