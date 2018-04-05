package com.scleroid.financematic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.Profile;
import com.scleroid.financematic.utils.CurrencyStringUtils;

import java.util.List;

/**
 * Created by scleroid on 5/4/18.
 */



public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private final CurrencyStringUtils currencyStringUtils = new CurrencyStringUtils();

    private List<Profile> loanList2;
/*TODO Work in Progress ,Add this & remove other constructor
    public LoanAdapter(List<Loan> loanList) {
        this.loanList = loanList;
    }*/

    public ProfileAdapter(List<Profile> loanList) {
        this.loanList2 = loanList;
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
        Profile loan = loanList2.get(position);
        holder.title.setText(loan.getTitle());
        holder.Total_loan.setText(currencyStringUtils.bindNumber(loan.getTotal_loan()));
        holder.endDate1.setText(loan.getEndDate1());
        holder. startDate1.setText(loan.getStartDate1());
        holder. ReceivedAmt.setText(currencyStringUtils.bindNumber(loan.getReceivedAmt()));
    }


    @Override
    public int getItemCount() {
        return loanList2.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, endDate1, startDate1, Total_loan,ReceivedAmt;

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
