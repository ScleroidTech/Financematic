package com.scleroid.financematic.fragments.loandetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.utils.eventbus.Events;
import com.scleroid.financematic.utils.eventbus.GlobalBus;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by scleroid on 6/3/18.
 */


public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {


	private List<TransactionModel> transactionList = new ArrayList<>();
	private List<Installment> installmentList = new ArrayList<>();

	public LoanAdapter(List<TransactionModel> transactionList,
	                   final List<Installment> installmentList) {
		this.transactionList = transactionList;
		this.installmentList = installmentList;
	}

	public List<TransactionModel> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(
			final List<TransactionModel> transactionList) {
		this.transactionList = transactionList;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_loan_details, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		if (position < installmentList.size()) {
			Timber.e(position + "  " + installmentList.size() + "");
			Installment installment = installmentList.get(position);
			holder.setInstallment(installment);
		} else {
			Timber.e(position + "  " + transactionList.size() + "");
			TransactionModel passbook = transactionList.get(position - installmentList.size());

			holder.setData(passbook);
		}
	}

	@Override
	public int getItemCount() {
		return transactionList.size() + installmentList.size();
	}

	public List<Installment> getInstallmentList() {
		return installmentList;
	}

	void setInstallmentList(final List<Installment> installmentList) {
		this.installmentList = installmentList;
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		@NonNull
		static DateUtils dateUtils = new DateUtils();
		@NonNull
		static TextViewUtils textViewUtils = new TextViewUtils();
		@NonNull
		static ActivityUtils activityUtils = new ActivityUtils();
		//	static CurrencyStringUtils currencyStringUtils = new CurrencyStringUtils();
		@Nullable
		@BindView(R.id.month_text_view)
		TextView monthTextView;
		@Nullable
		@BindView(R.id.only_date_text_view)
		TextView onlyDateTextView;
		@Nullable
		@BindView(R.id.day_text_view)
		TextView dayTextView;

		/*  @BindView(R.id.year_text_view)
		  TextView yearTextView;*/
		@Nullable
		@BindView(R.id.summery_description)
		TextView summeryDescpription;
		@Nullable
		@BindView(R.id.summery_amount)
		RupeeTextView summeryAmount;
		@Nullable
		@BindView(R.id.Btn_paid_rx_summery)
		Button BtnPaidRxSummery;
		private Installment installment;

		public MyViewHolder(@NonNull View view) {
			super(view);
			ButterKnife.bind(this, view);


		}

		public void setData(@NonNull TransactionModel passbook) {
			//  holder.summery_date.setText(passbook.getSummery_date());
			itemView.setTag(passbook);
			summeryDescpription.setText(passbook.getDescription());
			if (passbook.getReceivedAmt() == null) {
				//	itemView.setBackgroundColor(0xFF999999);
				summeryAmount.setText(passbook.getLentAmt().toPlainString());
				setDate(passbook.getTransactionDate());
				textViewUtils.textViewExperimentsRed(summeryAmount);
				BtnPaidRxSummery.setBackgroundResource(R.drawable.button_rounded_gray);
				BtnPaidRxSummery.setText("Lent");
			} else {
				summeryAmount.setText(passbook.getReceivedAmt().toPlainString());
				setDate(passbook.getTransactionDate());
				textViewUtils.textViewExperiments(summeryAmount);
				BtnPaidRxSummery.setBackgroundResource(R.drawable.button_rounded_green);
				BtnPaidRxSummery.setText("Paid");
			}

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

		private void setInstallment(@NonNull final Installment passbook) {
			BtnPaidRxSummery.setBackgroundResource(R.drawable.button_rounded_red);
			BtnPaidRxSummery.setText("Pay");
			installment = passbook;
			itemView.setTag(passbook);
			if (passbook.getDelayReason() != null) {
				summeryDescpription.setText(passbook.getDelayReason());
				BtnPaidRxSummery.setBackgroundResource(R.drawable.button_rounded_yellow);
			} else {
				summeryDescpription.setText("Yet to come");
			}

			String text = passbook.getExpectedAmt().toPlainString();
			Timber.d("ABCD Installment Amount" + text);
			summeryAmount.setText(text);
			setDate(passbook.getInstallmentDate());
			textViewUtils.textViewExperiments(summeryAmount);

		}

		@OnClick(R.id.Btn_paid_rx_summery)
		public void onViewClicked() {
			if (BtnPaidRxSummery.getText().toString().equals("Pay")) {
				Events.openReceiveMoneyFragment openCustomerFragment =
						new Events.openReceiveMoneyFragment(installment.getLoanAcNo(),
								installment.getInstallmentId());
				GlobalBus.getBus().post(openCustomerFragment);
			}

		}

	}
}
