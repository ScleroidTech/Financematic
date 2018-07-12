package com.scleroid.financematic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.Session;
import com.scleroid.financematic.utils.ui.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment interface to handle
 * interaction events. Use the {@link AddMoneyFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class AddMoneyFragment extends BaseFragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	@BindView(R.id.add_money_edit_text)
	EditText addMoneyEditText;
	@BindView(R.id.add_money_button)
	Button addMoneyButton;
	Unbinder unbinder;
	@Inject
	Session session;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	@Inject
	ActivityUtils activityUtils;

	public AddMoneyFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment using the provided
	 * parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment AddMoneyFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddMoneyFragment newInstance(String param1, String param2) {
		AddMoneyFragment fragment = new AddMoneyFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View view = getRootView();
		setTitle();

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(), "Add Money");
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_add_money;
	}

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {

	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public BaseViewModel getViewModel() {
		return null;
	}

	@OnClick(R.id.add_money_button)
	public void onViewClicked() {

		if (addMoneyEditText.getText() == null || addMoneyEditText.getText().toString() == null) {
			addMoneyEditText.setError("Not a valid Amount");
			return;
		}

		session.updateAmount(Float.parseFloat(addMoneyEditText.getText().toString().trim()));
		Toasty.success(getContext(),
				"Amount Saved Successfully, Continue to browse through the application").show();
		addMoneyEditText.setText("");
	}


}
