package com.scleroid.financematic.fragments.loanDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by scleroid on 6/3/18.
 */


public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {


	private List<TransactionModel> transactionList;

	public LoanAdapter(List<TransactionModel> transactionList) {
		this.transactionList = transactionList;
	}

	public List<TransactionModel> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(
			final List<TransactionModel> transactionList) {
		this.transactionList = transactionList;
		notifyDataSetChanged();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_loan_details, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		TransactionModel passbook = transactionList.get(position);
		holder.setData(passbook);

	}

	@Override
	public int getItemCount() {
		return transactionList.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		static DateUtils dateUtils = new DateUtils();
		static TextViewUtils textViewUtils = new TextViewUtils();
		//	static CurrencyStringUtils currencyStringUtils = new CurrencyStringUtils();
		@BindView(R.id.month_text_view)
		TextView monthTextView;
		@BindView(R.id.only_date_text_view)
		TextView onlyDateTextView;
		@BindView(R.id.day_text_view)
		TextView dayTextView;

		/*  @BindView(R.id.year_text_view)
		  TextView yearTextView;*/
		@BindView(R.id.summery_description)
		TextView summeryDescpription;
		@BindView(R.id.summery_amount)
		RupeeTextView summeryAmount;
		@BindView(R.id.Btn_paid_rx_summery)
		Button BtnPaidRxSummery;

		public MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);


		}

		public void setData(TransactionModel passbook) {
			//  holder.summery_date.setText(passbook.getSummery_date());
			summeryDescpription.setText(passbook.getDescription());
			summeryAmount.setText(
					String.valueOf(passbook.getReceivedAmt().intValue()));
			setDate(passbook.getTransactionDate());
			textViewUtils.textViewExperiments(summeryAmount);

		}

		private void setDate(Date date) {
			CharSequence month = dateUtils.getFormattedDate(date, "MMM. yyyy");
			CharSequence exactDate = dateUtils.getFormattedDate(date, "dd");
			CharSequence day = dateUtils.getFormattedDate(date, "EEE");
			CharSequence year = dateUtils.getFormattedDate(date, "yyyy");
			monthTextView.setText(month);
			onlyDateTextView.setText(exactDate);
			dayTextView.setText(day);
			//    yearTextView.setText(year);
		}
	}
}
