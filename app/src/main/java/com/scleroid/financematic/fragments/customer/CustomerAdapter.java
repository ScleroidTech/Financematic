package com.scleroid.financematic.fragments.customer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;

import java.util.List;

/**
 * Created by scleroid on 5/4/18.
 */


public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

	//private final CurrencyStringUtils currencyStringUtils = new CurrencyStringUtils();
	private final DateUtils dateUtils = new DateUtils();
	private List<Loan> loanList;

	public CustomerAdapter(List<Loan> loanList) {
		this.loanList = loanList;
	}

	public List<Loan> getLoanList() {
		return loanList;
	}
/*TODO Work in Progress ,Add this & remove other constructor
    public DashboardAdapter(List<Loan> loanList) {
        this.loanList = loanList;
    }*/

	public void setLoanList(final List<Loan> loanList) {
		this.loanList = loanList;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_profile, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Loan loan = loanList.get(position);
		holder.title.setText(String.format("Loan A/c No. %d", loan.getAccountNo()));
		holder.Total_loan.setText(loan.getLoanAmt().toString());
		holder.endDate1.setText(dateUtils.getFormattedDate(loan.getEndDate()));
		holder.startDate1.setText(dateUtils.getFormattedDate(loan.getStartDate()));
		holder.ReceivedAmt.setText(loan.getReceivedAmt().toString());
		holder.itemView.setOnClickListener(v -> {
			Events.openLoanDetailsFragment openCustomerFragment =
					new Events.openLoanDetailsFragment(loan.getAccountNo());
			GlobalBus.getBus().post(openCustomerFragment);
		});
	}


	@Override
	public int getItemCount() {
		return loanList.size();
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {
		TextView title, endDate1, startDate1;
		RupeeTextView Total_loan, ReceivedAmt;

		MyViewHolder(View view) {
			super(view);
			title = view.findViewById(R.id.Loan_no_text_view);
			startDate1 = view.findViewById(R.id.StartDate);
			endDate1 = view.findViewById(R.id.EndDate);
			Total_loan = view.findViewById(R.id.Total_loan_amount);
			ReceivedAmt = view.findViewById(R.id.ReceivedAmount);
		}
	}
}
