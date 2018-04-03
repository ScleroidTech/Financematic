package com.scleroid.financematic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.TempDashBoardModel;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.utils.CurrencyStringUtils;

import java.util.List;

/**
 * Created by scleroid on 3/3/18.
 */

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    private final CurrencyStringUtils currencyStringUtils = new CurrencyStringUtils();
    private List<Loan> loanList;
    private List<TempDashBoardModel> loanList2;
/*TODO Work in Progress ,Add this & remove other constructor
    public LoanAdapter(List<Loan> loanList) {
        this.loanList = loanList;
    }*/

    public LoanAdapter(List<TempDashBoardModel> loanList) {
        this.loanList2 = loanList;
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
        TempDashBoardModel loan = loanList2.get(position);
        holder.title.setText(loan.getTitle());
        holder.genre.setText(currencyStringUtils.bindNumber(loan.getGenre()));
        holder.year.setText(loan.getYear());
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
