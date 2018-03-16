package com.scleroid.financematic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by scleroid on 6/3/18.
 */


public class Adapter_personal_summery_details extends RecyclerView.Adapter<Adapter_personal_summery_details.MyViewHolder> {

    private List<Personal_summery_loan_details> summeryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView summery_date,summery_amount,summery_descpription;

        public MyViewHolder(View view) {
            super(view);
            summery_date= (TextView) view.findViewById(R.id.summery_date);
            summery_descpription = (TextView) view.findViewById(R.id.summery_descpription);
            summery_amount = (TextView) view.findViewById(R.id.summery_amount);

        }
    }


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
        holder.summery_date.setText(passbook .getSummery_date());
        holder.summery_descpription.setText(passbook .getSummery_descpription());
        holder.summery_amount.setText(passbook.getSummery_amount());

    }

    @Override
    public int getItemCount() {
        return summeryList.size();
    }
}
