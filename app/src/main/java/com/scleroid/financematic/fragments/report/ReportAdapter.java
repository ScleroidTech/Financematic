package com.scleroid.financematic.fragments.report;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {


	private SortedList<TransactionModel> reportList;

	public ReportAdapter() {
		this.reportList = new SortedList<TransactionModel>(TransactionModel.class,
				new SortedList.Callback<TransactionModel>() {
					@Override
					public int compare(final TransactionModel o1, final TransactionModel o2) {
						return o1.getTransactionId() == o2.getTransactionId() ? 0 : 1;
					}

					@Override
					public void onChanged(final int position, final int count) {
						notifyItemRangeChanged(position, count);

					}

					@Override
					public boolean areContentsTheSame(final TransactionModel oldItem,
					                                  final TransactionModel newItem) {
						return oldItem.equals(newItem);
					}

					@Override
					public boolean areItemsTheSame(final TransactionModel item1,
					                               final TransactionModel item2) {
						return item1.equals(item2);
					}

					@Override
					public void onInserted(final int position, final int count) {
						notifyItemRangeInserted(position, count);
					}

					@Override
					public void onRemoved(final int position, final int count) {
						notifyItemRangeRemoved(position, count);
					}

					@Override
					public void onMoved(final int fromPosition, final int toPosition) {
						notifyItemMoved(fromPosition, toPosition);
					}
				});
	}

	public void setReportList(
			final List<TransactionModel> reportList) {
		//this.reportList = reportList;
		addAll(reportList);
		//notifyDataSetChanged();
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_report, parent, false);

		return new MyViewHolder(itemView);

	}

	//conversation helpers
	public void addAll(List<TransactionModel> countries) {
		reportList.beginBatchedUpdates();
		for (int i = 0; i < countries.size(); i++) {
			reportList.add(countries.get(i));
		}
		reportList.endBatchedUpdates();
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

		TransactionModel report = reportList.get(position);

		holder.setData(report);

		if (position % 2 == 1) {
			holder.itemView.setBackgroundColor(Color.parseColor("#d5e5f0"));
			//  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		} else {
			holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
			//  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
		}
	}

	public TransactionModel get(int position) {
		return reportList.get(position);
	}

	public void clear() {
		reportList.beginBatchedUpdates();
		//remove items at end, to avoid unnecessary array shifting
		while (reportList.size() > 0) {
			reportList.removeItemAt(reportList.size() - 1);
		}
		reportList.endBatchedUpdates();
	}

	@Override
	public int getItemCount() {
		return reportList.size();
	}


	public class MyViewHolder extends RecyclerView.ViewHolder {
		DateUtils dateUtils = new DateUtils();
		/* for selecte row and chnage color        implements View.OnClickListener*/
		@BindView(R.id.acc_no_text_view)
		TextView accNoTextView;
		@BindView(R.id.transactionDate)
		TextView transactionDate;
		@BindView(R.id.lentAmt)
		TextView reportLent;
		@BindView(R.id.earnedAmt)
		TextView reportEarned;
		@BindView(R.id.receivedAmt)
		TextView receivedAmt;
		private SparseBooleanArray selectedItems = new SparseBooleanArray();
		private TransactionModel report;

		public MyViewHolder(View view) {
			super(view);
			/* view.setOnClickListener(this);*/
			ButterKnife.bind(this, view);


		}


		private void setData(TransactionModel report) {
			this.report = report;
			accNoTextView.setText(String.valueOf(report.getLoanAcNo()));
			transactionDate.setText(
					dateUtils.getFormattedDateDigitsOnly(report.getTransactionDate()));
			reportLent.setText(String.valueOf(
					report.getLentAmt() != null ? report.getLentAmt().intValue() : " "));
			reportEarned.setText(String.valueOf(
					report.getGainedAmt() != null ? report.getGainedAmt().intValue() : " "));
			receivedAmt.setText(String.valueOf(
					report.getReceivedAmt() != null ? report.getReceivedAmt().intValue() : " "));
			accNoTextView.setTextColor(Color.parseColor("#5432ff"));
			//reportBalance.setText(String.valueOf( report));
		}

		@OnClick(R.id.acc_no_text_view)
		public void onViewClicked() {
			Events.openLoanDetailsFragment openCustomerFragment =
					new Events.openLoanDetailsFragment(report.getLoanAcNo());
			GlobalBus.getBus().post(openCustomerFragment);
		}


	}
}