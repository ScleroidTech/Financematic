package com.scleroid.financematic.utils.ui;

import android.widget.Filter;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.fragments.people.PeopleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 28/3/18.
 */

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter {

	PeopleAdapter adapter;
	ArrayList<Customer> filterList;


	public CustomFilter(List<Customer> filterList, PeopleAdapter adapter) {
		this.adapter = adapter;
		this.filterList = (ArrayList<Customer>) filterList;

	}

	//FILTERING OCURS
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults results = new FilterResults();

		//CHECK CONSTRAINT VALIDITY
		if (constraint != null && constraint.length() > 0) {
			//CHANGE TO UPPER
			constraint = constraint.toString().toUpperCase();
			//STORE OUR FILTERED PLAYERS
			ArrayList<Customer> filteredPlayers = new ArrayList<>();

			for (int i = 0; i < filterList.size(); i++) {
				//CHECK
				if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
					//ADD PLAYER TO FILTERED PLAYERS
					filteredPlayers.add(filterList.get(i));
				}
			}

			results.count = filteredPlayers.size();
			results.values = filteredPlayers;
		} else {
			results.count = filterList.size();
			results.values = filterList;

		}


		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {

		adapter.customerList = (ArrayList<Customer>) results.values;

		//REFRESH
		adapter.notifyDataSetChanged();
	}
}
