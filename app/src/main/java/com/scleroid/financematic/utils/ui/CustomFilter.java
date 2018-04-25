package com.scleroid.financematic.utils.ui;

import android.widget.Filter;

import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.fragments.people.PeopleAdapter;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by scleroid on 28/3/18.
 */

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter {

	PeopleAdapter adapter;
	List<Customer> filterList;


	public CustomFilter(List<Customer> filterList, PeopleAdapter adapter) {
		this.adapter = adapter;
		this.filterList = filterList;

	}

	//FILTERING OCURS
	@DebugLog
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults results = new FilterResults();

	/*	//CHECK CONSTRAINT VALIDITY
		if (constraint != null && constraint.length() > 0) {
			//CHANGE TO UPPER
			constraint = constraint.toString().toUpperCase();
			//STORE OUR FILTERED PLAYERS
			List<Customer> filteredPlayers = new ArrayList<>();

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

		}*/
		final String finalConstraint = constraint.toString().toLowerCase();
		List<Customer> collect = Stream.of(filterList)
				.filter(human -> human.getName().toLowerCase().contains(
						finalConstraint))
				.collect(Collectors.toList());
		results.values = collect;
		results.count = collect.size();


		return results;
	}

	@DebugLog
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {

		adapter.setFilterList((List<Customer>) results.values);


	}
}
