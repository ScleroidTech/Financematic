package com.scleroid.financematic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.fragments.dashboard.DashBoardModel;
import com.scleroid.financematic.utils.CurrencyStringUtils;
import com.scleroid.financematic.utils.DateUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by scleroid on 3/3/18.
 */

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    @Inject
    DateUtils dateUtils;
    @Inject
    CurrencyStringUtils currencyStringUtils;
    private List<DashBoardModel> loanList;

    public LoanAdapter(List<DashBoardModel> loanList) {
        this.loanList = loanList;
    }

    public List<DashBoardModel> getLoanList() {
        return loanList;
    }
/*TODO Work in Progress ,Add this & remove other constructor
    public LoanAdapter(List<Loan> loanList) {
        this.loanList = loanList;
    }*/

    public void setLoanList(final List<DashBoardModel> loanList) {
        this.loanList = loanList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_dashboard, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DashBoardModel loan = loanList.get(position);
        holder.title.setText(loan.getCustomerName());
        holder.genre.setText(currencyStringUtils.bindNumber(loan.getAmtDue().intValueExact()));
        holder.year.setText(dateUtils.getFormattedDate(loan.getInstallmentDate()));
    }


    @Override
    public int getItemCount() {
        return loanList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, year, genre;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.customer_name_text_view);
            genre = view.findViewById(R.id.amount_text_view);
            year = view.findViewById(R.id.time_remaining);
        }
    }
}
