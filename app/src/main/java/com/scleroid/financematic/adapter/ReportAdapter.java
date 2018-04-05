package com.scleroid.financematic.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.tempModels.Report;
import com.scleroid.financematic.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {


    private List<Report> reportList;


    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_report, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Report report = reportList.get(position);

        holder.setData(report);

        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#d5e5f0"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
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
        @BindView(R.id.balanceAmt)
        TextView reportBalance;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public MyViewHolder(View view) {
            super(view);
            /* view.setOnClickListener(this);*/
            ButterKnife.bind(this, view);


        }


        private void setData(Report report) {
            accNoTextView.setText(report.getAccNo());
            transactionDate.setText(dateUtils.getFormattedDate(report.getTransactionDate()));
            reportLent.setText(report.getLentAmt());
            reportEarned.setText(report.getEarnedAmt());
            reportBalance.setText(report.getBalanceAmt());
        }


    }
}
