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
 * interaction events.
 */
public class AddMoneyFragment extends BaseFragment {

	@BindView(R.id.add_money_edit_text)
	EditText addMoneyEditText;
	@BindView(R.id.add_money_button)
	Button addMoneyButton;
	Unbinder unbinder;
	@Inject
	Session session;

	@Inject
	ActivityUtils activityUtils;

	public AddMoneyFragment() {
		// Required empty public constructor
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
