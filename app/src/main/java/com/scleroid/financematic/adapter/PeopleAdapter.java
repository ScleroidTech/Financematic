package com.scleroid.financematic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.fragments.people.PeopleModel;
import com.scleroid.financematic.utils.ui.CircleCustomView;
import com.scleroid.financematic.utils.ui.CustomFilter;
import com.scleroid.financematic.utils.ui.RupeeTextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by scleroid on 6/3/18.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> implements Filterable {
    Context c;
    public List<PeopleModel> peopleModelList;
    private List<PeopleModel> filterList;
    CustomFilter filter;

    public PeopleAdapter(Context ctx, List<PeopleModel> peopleModelList) {
        this.c=ctx;
        this.peopleModelList = peopleModelList;
        this.filterList = peopleModelList;
    }

    public PeopleAdapter(List<PeopleModel> peopleModelList) {
        this.peopleModelList = peopleModelList;
        this.filterList = peopleModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_people, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PeopleModel passbook = peopleModelList.get(position);
        // holder.setPassbook(passbook);
        holder.list_person_name.setText(passbook.getList_person_name());
        holder.list_total_loan.setText(String.format("%d", passbook.getList_total_loan()));
        holder.list_received_amount.setText(String.format("%d", passbook.getList_received_amoun()));
        holder.drawCircle(passbook);



    }

    @Override
    public int getItemCount() {
        return peopleModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView list_person_name;
        public RupeeTextView list_total_loan, list_received_amount;
        @BindView(R.id.payment_circle_view)
        CircleCustomView paymentCircleView;
        @BindView(R.id.person_name_text_view)
        TextView personNameTextView;
        @BindView(R.id.total_amount_title_text_view)
        TextView totalAmountTitleTextView;
        @BindView(R.id.total_loan_text_view)
        RupeeTextView totalLoanTextView;
        @BindView(R.id.received_amount_title_text_view)
        TextView receivedAmountTitleTextView;
        @BindView(R.id.received_amount_text_view)
        RupeeTextView receivedAmountTextView;
        @BindView(R.id.percentage_pie_chart_text_view)
        TextView percentagePieChartTextView;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            list_person_name = view.findViewById(R.id.person_name_text_view);
            list_total_loan = view.findViewById(R.id.total_loan_text_view);
            list_received_amount = view.findViewById(R.id.received_amount_text_view);

           //for tryintend



        }

        private void drawCircle(PeopleModel passbook) {
            float percentage = getPercentage(passbook.getList_received_amoun(), passbook.getList_total_loan());
            String percentageString = new DecimalFormat("##").format(percentage);
            percentagePieChartTextView.setText(String.format("%s %%", percentageString));
            float angle = getAngle(percentage);
            paymentCircleView.setAngle(angle);
            paymentCircleView.invalidate();
        }

        private float getAngle(float avg) {
            int angle = (int) ((avg / 100) * 360);
            return (float) angle;
        }

        private float getPercentage(float list_received_amoun, int list_total_loan) {
            return (list_received_amoun / list_total_loan) * 100;
        }
    }


    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }




}
