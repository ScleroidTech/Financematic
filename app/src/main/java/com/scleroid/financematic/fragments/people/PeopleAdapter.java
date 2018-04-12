package com.scleroid.financematic.fragments.people;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.CircleCustomView;
import com.scleroid.financematic.utils.ui.CustomFilter;
import com.scleroid.financematic.utils.ui.RupeeTextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by scleroid on 6/3/18.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder>
		implements Filterable {
	public List<Customer> customerList;
	Context context;
	CustomFilter filter;
	private List<Customer> filterList;

	public PeopleAdapter(Context context, List<Customer> customerList) {
		this.context = context;
		this.customerList = customerList;
		this.filterList = customerList;
	}

	public PeopleAdapter(List<Customer> customerList) {
		this.customerList = customerList;
		this.filterList = customerList;
	}


	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_people, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Customer passbook = customerList.get(position);
		if (passbook.getLoans() == null) return;
		// holder.setPassbook(passbook);
		holder.setData(passbook);


	}

	@Override
	public int getItemCount() {
		return customerList.size();
	}

	//RETURN FILTER OBJ
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new CustomFilter(filterList, this);
		}

		return filter;
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		public TextView list_person_name;
		public RupeeTextView list_total_loan, list_received_amount;
		@BindView(R.id.payment_circle_view)
		CircleCustomView paymentCircleView;
		@BindView(R.id.person_name_text_view)
		TextView personNameTextView;
		@BindView(R.id.total_amount_title_text_view)
		TextView totalAmountTitleTextView;
		@BindView(R.id.total_loan_text_view)
		RupeeTextView totalLoanTextView;
		@BindView(R.id.received_amount_title_text_view)
		TextView receivedAmountTitleTextView;
		@BindView(R.id.received_amount_text_view)
		RupeeTextView receivedAmountTextView;
		@BindView(R.id.percentage_pie_chart_text_view)
		TextView percentagePieChartTextView;
		private Customer customer;

		public MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			list_person_name = view.findViewById(R.id.person_name_text_view);
			list_total_loan = view.findViewById(R.id.total_loan_text_view);
			list_received_amount = view.findViewById(R.id.received_amount_text_view);

			//for tryintend


		}

		private void setData(final Customer customer) {
			this.customer = customer;
			personNameTextView.setText(customer.getName());
			final int receivedAmt = calculateReceivedAmt(customer.getLoans());
			final int totalAmt = calculateTotalAmt(customer.getLoans());
			totalLoanTextView.setText(String.format("%d", receivedAmt));

			receivedAmountTextView.setText(String.format("%d", totalAmt));

			drawCircle(receivedAmt, totalAmt);
		}

		private void drawCircle(final int receivedAmt, final int totalAmt) {
			float percentage = getPercentage((float) receivedAmt, totalAmt);
			String percentageString = new DecimalFormat("##").format(percentage);
			percentagePieChartTextView.setText(String.format("%s %%", percentageString));
			float angle = getAngle(percentage);
			paymentCircleView.setAngle(angle);
			paymentCircleView.invalidate();
		}

		private float getAngle(float avg) {
			int angle = (int) ((avg / 100) * 360);
			return (float) angle;
		}

		private float getPercentage(float list_received_amoun, int list_total_loan) {
			return (list_received_amoun / list_total_loan) * 100;
		}

		private int calculateTotalAmt(final List<Loan> loans) {
			return Stream.of(loans).mapToInt(loan ->
					loan.getLoanAmt().intValue()).sum();
		}

		private int calculateReceivedAmt(final List<Loan> loans) {

			return Stream.of(loans).mapToInt(loan ->
					loan.getReceivedAmt().intValue()).sum();
		}

		@OnClick({R.id.callButton, R.id.people_item_card_view})
		public void onViewClicked(View view) {
			switch (view.getId()) {
				case R.id.call_button:
					handleCallClick();
					break;

				case R.id.people_item_card_view:
					break;
			}

		}

		private void handleCallClick() {
			String phone = customer.getMobileNumber();
			Timber.d(phone + " of person " + customer.getName());
			Events.placeCall makeACall = new Events.placeCall(phone);

			GlobalBus.getBus().post(makeACall);
		}
	}

}
