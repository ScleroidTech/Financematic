package com.scleroid.financematic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by scleroid on 3/3/18.
 */

public class LoanesAdapter extends RecyclerView.Adapter<LoanesAdapter.MyViewHolder> {

    private List<Loan> loanList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public LoanesAdapter(List<Loan> loanList) {
        this.loanList = loanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_list_row_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       Loan loan = loanList.get(position);
        holder.title.setText(loan .getTitle());
        holder.genre.setText(loan .getGenre());
        holder.year.setText(loan .getYear());
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }
}
