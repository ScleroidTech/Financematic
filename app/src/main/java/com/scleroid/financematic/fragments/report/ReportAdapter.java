package com.scleroid.financematic.fragments.report;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {


	private List<TransactionModel> reportList = new ArrayList<>();

	private ReportFilterType filterType;

	ReportAdapter() {
		/*this.reportList = new SortedList<TransactionModel>(TransactionModel.class,
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
				});*/
	}

	public ReportFilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(final ReportFilterType filterType) {
		this.filterType = filterType;
	}

	public void setReportList(
			final List<TransactionModel> reportList) {
		this.reportList = reportList;
		//addAll(reportList);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_report, parent, false);

		return new MyViewHolder(itemView);

	}

	/*//conversation helpers
	public void addAll(List<TransactionModel> countries) {
		reportList.beginBatchedUpdates();
		for (int i = 0; i < countries.size(); i++) {
			reportList.add(countries.get(i));
		}
		reportList.endBatchedUpdates();
	}
*/
	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

		TransactionModel report = reportList.get(position);
		holder.setFilterType(filterType);
		holder.setData(report);


		/*if (position % 2 == 1) {
			holder.itemView.setBackgroundColor(Color.parseColor("#d5e5f0"));
			//  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		} else {
			holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
			//  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
		}*/
	}

	@Override
	public int getItemCount() {
		return reportList.size();
	}

	/*public void clear() {
		reportList.beginBatchedUpdates();
		//remove items at end, to avoid unnecessary array shifting
		while (reportList.size() > 0) {
			reportList.removeItemAt(reportList.size() - 1);
		}
		reportList.endBatchedUpdates();
	}*/

	public TransactionModel get(int position) {
		return reportList.get(position);
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		@NonNull
		DateUtils dateUtils = new DateUtils();

		ReportFilterType filterType = ReportFilterType.ALL_TRANSACTIONS;
		/* for selected row and change color        implements View.OnClickListener*/
		@Nullable
		@BindView(R.id.acc_no_text_view)
		TextView accNoTextView;
		@Nullable
		@BindView(R.id.transactionDate)
		TextView transactionDate;
		@Nullable
		@BindView(R.id.lentAmt)
		TextView reportLent;
		@Nullable
		@BindView(R.id.earnedAmt)
		TextView reportEarned;
		@Nullable
		@BindView(R.id.receivedAmt)
		TextView receivedAmt;
		//private SparseBooleanArray selectedItems = new SparseBooleanArray();
		private TransactionModel report;

		public MyViewHolder(@NonNull View view) {
			super(view);
			/* view.setOnClickListener(this);*/
			ButterKnife.bind(this, view);


		}

		private void setData(TransactionModel report) {
			this.report = report;
			accNoTextView.setText(String.valueOf(report.getLoanAcNo()));
			transactionDate.setText(
					dateUtils.getFormattedDateDigitsOnly(report.getTransactionDate()));
			reportLent.setText(
					report.getLentAmt() != null ? report.getLentAmt().toString() : " ");
			reportEarned.setText(
					report.getGainedAmt() != null ? report.getGainedAmt().toString() : " ");
			receivedAmt.setText(
					report.getReceivedAmt() != null ? report.getReceivedAmt().toString() : " ");
			accNoTextView.setTextColor(Color.parseColor("#5432ff"));
			filterData(getFilterType());

			//reportBalance.setText(String.valueOf( report));
		}

		private void filterData(final ReportFilterType filterType) {
			switch (filterType) {
				case ALL_TRANSACTIONS:
					makeItVisible();
					break;
				case RECEIVED_AMOUNT:
					updateUI(receivedAmt);
					break;
				case LENT_AMOUNT:
					updateUI(reportLent);
					break;
				case EARNED_AMOUNT:
					updateUI(reportEarned);
					break;
				default:
					makeItVisible();
					break;

			}
		}

		private void makeItVisible() {
			receivedAmt.setVisibility(View.VISIBLE);
			reportLent.setVisibility(View.VISIBLE);
			reportEarned.setVisibility(View.VISIBLE);
		}

		private void updateUI(final TextView amt) {
			//First Enable any previously disabled views
			visibilityToggle();
			amt.setVisibility(View.VISIBLE);

		}

		private void visibilityToggle() {
			receivedAmt.setVisibility(View.GONE);
			reportLent.setVisibility(View.GONE);
			reportEarned.setVisibility(View.GONE);
		}

		public ReportFilterType getFilterType() {
			return filterType;
		}

		public void setFilterType(final ReportFilterType filterType) {
			this.filterType = filterType;
		}

		@OnClick(R.id.acc_no_text_view)
		public void onViewClicked() {
			Events.openLoanDetailsFragment openCustomerFragment =
					new Events.openLoanDetailsFragment(report.getLoanAcNo());
			GlobalBus.getBus().post(openCustomerFragment);
		}


	}
}
