package com.scleroid.financematic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.fragments.dashboard.DashBoardModel;
import com.scleroid.financematic.utils.CurrencyStringUtils;
import com.scleroid.financematic.utils.DateUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {


	private List<DashBoardModel> loanList;

	public LoanAdapter(List<DashBoardModel> loanList) {
		this.loanList = loanList;
	}

	public List<DashBoardModel> getLoanList() {
		return loanList;
	}
/*TODO Work in Progress ,Add this & remove other constructor
    public LoanAdapter(List<Loan> loanList) {
        this.loanList = loanList;
    }*/

	public void setLoanList(final List<DashBoardModel> loanList) {
		this.loanList = loanList;
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
		DashBoardModel dashBoardModel = loanList.get(position);
		holder.itemView.setTag(dashBoardModel);
		holder.setData(dashBoardModel);

	}


	@Override
	public int getItemCount() {
		return loanList.size();
	}


	static class MyViewHolder extends RecyclerView.ViewHolder {
		@Inject
		DateUtils dateUtils;
		@Inject
		CurrencyStringUtils currencyStringUtils;
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

		}

		private void setData(final DashBoardModel dashBoardModel) {
			customerNameTextView.setText(dashBoardModel.getCustomerName());
			amountTextView.setText(
					currencyStringUtils.bindNumber(dashBoardModel.getAmtDue().intValueExact()));
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
