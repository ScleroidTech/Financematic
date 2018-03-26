package com.scleroid.financematic.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.model.Report;

import java.util.List;

/**
 * Created by scleroid on 16/3/18.
 * <p>
 * Created by scleroid on 5/3/18.
 */
/**
 * Created by scleroid on 5/3/18.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_report, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Report report = reportList.get(position);

        holder.passbook_date.setText(report.getReport_Acc_no());
        holder.passbook_name.setText(report .getReport_Lent());

        holder.passbook_taken_money.setText(report.getReport_Interest());
        holder.passbook_received_money.setText(report.getReport_Earned());
        holder.report_report_Balance.setText(report.getReport_Balance());
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#d5e5f0"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /* for selecte row and chnage color        implements View.OnClickListener*/
        public TextView passbook_taken_money, passbook_name, passbook_date, passbook_received_money, report_report_Balance;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public MyViewHolder(View view) {
            super(view);
           /* view.setOnClickListener(this);*/
            passbook_date = view.findViewById(R.id.acc_no_text_view);
            passbook_name = view.findViewById(R.id.report_Lent);
            passbook_taken_money = view.findViewById(R.id.report_Interest);
            passbook_received_money = view.findViewById(R.id.report_Earned);
            report_report_Balance = view.findViewById(R.id.report_Balance);

        }


    }
}
