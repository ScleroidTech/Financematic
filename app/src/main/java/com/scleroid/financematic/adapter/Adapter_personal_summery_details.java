package com.scleroid.financematic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.model.Personal_summery_loan_details;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by scleroid on 6/3/18.
 */


public class Adapter_personal_summery_details extends RecyclerView.Adapter<Adapter_personal_summery_details.MyViewHolder> {


    private List<Personal_summery_loan_details> summeryList;

    public Adapter_personal_summery_details(List<Personal_summery_loan_details> summeryList) {
        this.summeryList = summeryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_personal_summery_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Personal_summery_loan_details passbook = summeryList.get(position);
        holder.setData(passbook);

    }

    @Override
    public int getItemCount() {
        return summeryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.month_text_view)
        TextView monthTextView;
        @BindView(R.id.only_date_text_view)
        TextView onlyDateTextView;
        @BindView(R.id.day_text_view)
        TextView dayTextView;
        @BindView(R.id.year_text_view)
        TextView yearTextView;
        @BindView(R.id.summery_descpription)
        TextView summeryDescpription;
        @BindView(R.id.summery_amount)
        TextView summeryAmount;
        @BindView(R.id.Btn_paid_rx_summery)
        Button BtnPaidRxSummery;

        public MyViewHolder(View view) {
            super(view);


        }

        public void setData(Personal_summery_loan_details passbook) {
            //  holder.summery_date.setText(passbook.getSummery_date());
            summeryDescpription.setText(passbook.getSummery_descpription());
            summeryAmount.setText(passbook.getSummery_amount());
            setDate(passbook.getSummeryDate());

        }

        private void setDate(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            monthTextView.setText(calendar.get(Calendar.MONTH));
            onlyDateTextView.setText(calendar.get(Calendar.DATE));
            dayTextView.setText(calendar.get(Calendar.DAY_OF_MONTH));
            yearTextView.setText(calendar.get(Calendar.YEAR));
        }
    }
}
