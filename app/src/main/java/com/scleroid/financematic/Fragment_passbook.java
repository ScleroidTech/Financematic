package com.scleroid.financematic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scleroid on 3/3/18.
 * <p>
 * Created by scleroid on 2/3/18.
 * <p>
 * Created by scleroid on 2/3/18.
 * <p>
 * Created by scleroid on 2/3/18.
 */
/**
 * Created by scleroid on 2/3/18.
 */
/**
 * Created by scleroid on 2/3/18.
 */
/**
 * Created by scleroid on 2/3/18.
 */

public class Fragment_passbook  extends Fragment {
    private List<Passbook> passbookList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter_passbook mAdapter;

    public Fragment_passbook () {
        // Required empty public constructor
    }

    public static Fragment_passbook  newInstance(String param1, String param2) {
        Fragment_passbook  fragment = new Fragment_passbook ();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.passbook, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.passbook_my_recycler);

        mAdapter = new Adapter_passbook(passbookList);

        recyclerView.setHasFixedSize(true);

       /* recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this.getContext()));*/

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Passbook passbook = passbookList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), passbook.getPassbook_received_money() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareLoanData();

        return rootView;



    }

    private void prepareLoanData() {
        Passbook passbook = new Passbook("14/6/2018 ", "Person Name 1", "50,000","");
        passbookList.add(passbook);
        passbook = new Passbook("12/6/2018 ", "Person Name 2", "","3000");
        passbookList.add(passbook);
        passbook = new Passbook("12/6/2018 ", "Person Name 3", "","4000");
        passbookList.add(passbook);
        passbook = new Passbook("12/6/2018 ", "Person Name 1", "1200","");
        passbookList.add(passbook);
        passbook = new Passbook("1/2/2018 ", "Person Name 2", "4000","");
        passbookList.add(passbook);

        passbook = new Passbook("10/1/2018 ", "Person Name 3", "","2000");
        passbookList.add(passbook);

        passbook = new Passbook("12/1/2018 ", "Person Name 1", "4000","");
        passbookList.add(passbook);





        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }


}
