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


public class Adapter_list_all_peoples extends RecyclerView.Adapter<Adapter_list_all_peoples.MyViewHolder> {

    private List<List_all_peoples> list_all_peoplesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView list_person_name,list_total_loan,list_received_amount;

        public MyViewHolder(View view) {
            super(view);
            list_person_name= (TextView) view.findViewById(R.id.list_person_name);
            list_total_loan = (TextView) view.findViewById(R.id.list_total_loan);
            list_received_amount = (TextView) view.findViewById(R.id.list_received_amount);

        }
    }


    public Adapter_list_all_peoples(List<List_all_peoples> list_all_peoplesList) {
        this.list_all_peoplesList = list_all_peoplesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_all_peoples_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        List_all_peoples passbook = list_all_peoplesList.get(position);
        holder.list_person_name.setText(passbook .getList_person_name());
        holder.list_total_loan.setText(passbook .getList_total_loan());
        holder.list_received_amount.setText(passbook.getList_received_amoun());

    }

    @Override
    public int getItemCount() {
        return list_all_peoplesList.size();
    }
}
