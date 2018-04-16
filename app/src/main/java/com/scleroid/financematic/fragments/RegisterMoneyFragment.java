package com.scleroid.financematic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.lab.LocalCustomerLab;
import com.scleroid.financematic.data.local.model.Customer;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.LoanDurationType;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.utils.CommonUtils;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.TextValidator;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by scleroid on 9/3/18.
 */


public class RegisterMoneyFragment extends BaseFragment {

	private static final int REQUEST_DATE = 1;
	private static final String CUSTOMER = "customer";

	private static final String DIALOG_DATE = "DIALOG_DATE";
	private static final int REQUEST_DATE_FROM = 1;
	private static final int REQUEST_DATE_TO = 2;

	@Inject
	DateUtils dateUtils;
	Spinner spin;
	Calendar myCalendar = Calendar.getInstance();
	Calendar myCalendar1 = Calendar.getInstance();
	String[] country =
			{LoanDurationType.MONTHLY, LoanDurationType.DAILY, LoanDurationType.WEEKLY,
					LoanDurationType.BIWEEKLY, LoanDurationType.BIMONTHLY, LoanDurationType
					.QUARTERLY, LoanDurationType.HALF_YEARLY, LoanDurationType.YEARLY,
					LoanDurationType.ONE_TIME};
	Unbinder unbinder;
	@Inject
	LocalCustomerLab customerLab;
	@Inject
	LoanRepo loanRepo;
	@Inject
	ActivityUtils activityUtils;
	private Button b;
	private Spinner spinner;
	private TextView ettxloan_amout, startDateTextView, endDateTextView, ettxrateInterest,
			ettxInterestAmount, ettxInstallmentduration, etTotalLoanAmount, ettxNoofInstallment,
			tv;

	private String durationType = LoanDurationType.MONTHLY;
	private Date startDate;
	private Date endDate;
	private Customer customer;
	private Loan loan;


	public RegisterMoneyFragment() {
		// Required empty public constructor
	}

	public static RegisterMoneyFragment newInstance(Customer customer_id) {
		RegisterMoneyFragment fragment = new RegisterMoneyFragment();
		Bundle args = new Bundle();
		args.putParcelable(CUSTOMER, customer_id);
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
		super.onCreateView(inflater, container, savedInstanceState);
		final View rootView = getRootView();

		TextView customerNameTextView = rootView.findViewById(R.id.reg_fullname_detaills);
		Bundle bundle = getArguments();
		if (bundle != null) {
			customer = bundle.getParcelable(CUSTOMER);

			customerLab
					.getRxItem(customer.getCustomerId())
					.subscribe(customer -> {
								Timber.d("data received, displaying");
								customerNameTextView.setText("Got Reply");
							},
							throwable -> Timber.d("Not gonna show up")
					).dispose();
			customerNameTextView.setText(customer.getName());
		}
		final Spinner spin = rootView.findViewById(R.id.spinnertx);
		spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				durationType = country[position];
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});

		/*        final String text = spin.getSelectedItem().toString();*/
		//Creating the ArrayAdapter instance having the filterSuggestions list
		ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_item, country);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		spin.setAdapter(aa);


		startDateTextView = rootView.findViewById(R.id.txStartDate);


		endDateTextView = rootView.findViewById(R.id.txEndDate);

		ettxloan_amout = rootView.findViewById(R.id.txloan_amout);
		ettxrateInterest = rootView.findViewById(R.id.txrateInterest);
		ettxInterestAmount = rootView.findViewById(R.id.txInterestAmount);
		ettxNoofInstallment = rootView.findViewById(R.id.txNoofInstallment);
		ettxInstallmentduration = rootView.findViewById(R.id.txInstallmentduration);
		etTotalLoanAmount = rootView.findViewById(R.id.txTotalLoanAmount);
		final String loanAmt = ettxloan_amout.getText()
				.toString();
		ettxloan_amout.addTextChangedListener(
				new TextValidator(ettxloan_amout) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(loanAmt)) {
							ettxloan_amout.setError("Valid total given money");
						}
					}
				});

		final String rateOfInterest = ettxrateInterest.getText()
				.toString();
		ettxrateInterest.addTextChangedListener(
				new TextValidator(ettxrateInterest) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(rateOfInterest)) {
							ettxrateInterest.setError("Valid valid Rate in %");
						}
					}
				});
		final String interestAmt = ettxInterestAmount.getText()
				.toString();
		ettxInterestAmount.addTextChangedListener(
				new TextValidator(ettxInterestAmount) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(interestAmt)) {
							ettxInterestAmount.setError("Valid Interest Amount");
						}
					}
				});
		final String noOfInstallments = ettxNoofInstallment.getText()
				.toString();
		ettxNoofInstallment.addTextChangedListener(
				new TextValidator(ettxNoofInstallment) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(noOfInstallments)) {
							ettxNoofInstallment.setError("valid No of Installment");
						}
					}
				});
		final String duration = ettxInstallmentduration.getText()
				.toString();
		ettxInstallmentduration.addTextChangedListener(
				new TextValidator(ettxInstallmentduration) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(duration)) {
							ettxInstallmentduration.setError("Valid Installment Duration ");
						}
					}
				});
		final String totatLoanAmt = etTotalLoanAmount.getText()
				.toString();
		etTotalLoanAmount.addTextChangedListener(
				new TextValidator(etTotalLoanAmount) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(totatLoanAmt)) {
							etTotalLoanAmount.setError("Valid Total Amount");
						}
					}
				});


		b = rootView.findViewById(R.id.btn_givenmoney);
		tv = rootView.findViewById(R.id.displaytx);
		b.setOnClickListener(v -> {
			String startDateStr = startDateTextView.getText().toString();
			String endDateStr = endDateTextView
					.getText()
					.toString();
			if (TextUtils.isEmpty(loanAmt)) {
				ettxloan_amout.setError("Enter Loan Amount");
			}
			if (TextUtils.isEmpty(rateOfInterest)) {
				ettxrateInterest.setError("Enter rate Interest");
			}
			if (TextUtils.isEmpty(interestAmt)) {
				ettxInterestAmount.setError("Enter Interest Amount");
			}
			if (TextUtils.isEmpty(noOfInstallments)) {
				ettxNoofInstallment.setError("Enter No of Installment");
			}
			if (TextUtils.isEmpty(duration)) {
				ettxInstallmentduration.setError("Installment duration");
			}
			if (TextUtils.isEmpty(startDateStr)) { startDateTextView.setError("Start Date");}
			if (TextUtils.isEmpty(endDateStr)) {endDateTextView.setError("End Date");}
			if (TextUtils.isEmpty(totatLoanAmt)) {
				etTotalLoanAmount.setError("Total Loan Amount");
				return;
			}


			tv.setText(String.format("Your Input: \n%s\n%s\n%s\n%s\n%s\n%s\n%s%s\n%s\n\nEnd.",
					loanAmt, startDateStr, endDateStr, rateOfInterest, interestAmt,
					noOfInstallments, duration, spin.getSelectedItem()
							.toString(), totatLoanAmt));
			loan = new Loan(CommonUtils.getRandomInt(), new BigDecimal(loanAmt), startDate,
					endDate, Float.valueOf(rateOfInterest), new BigDecimal(interestAmt),
					Integer.valueOf(noOfInstallments), Integer.valueOf(duration), durationType,
					new BigDecimal(totatLoanAmt), customer.getCustomerId());
			saveLoan(loan);
		});


		unbinder = ButterKnife.bind(this, rootView);
		return rootView;
	}

	private void saveLoan(final Loan loan) {
		loanRepo.saveItem(loan).subscribe(loan1 -> {
			Timber.d("Loan Data Saved " + loan1.toString());
		}, throwable -> {
			Timber.d(
					"Loan Data not Saved " + throwable.getMessage() + " errors are " + loan
							.toString());
		});
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.registor_given_money;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
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

	private boolean isNotValidAmt(String loan_amountval) {
		String EMAIL_PATTERN = "(?:\\\\d+(?:\\\\.\\\\d+)?|\\\\.\\\\d+)";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(loan_amountval);
		return !matcher.matches();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_DATE_FROM) {
			startDate = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			startDateTextView.setText(dateUtils.getFormattedDate(startDate));
		} else if (requestCode == REQUEST_DATE_TO) {
			endDate = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			endDateTextView.setText(dateUtils.getFormattedDate(endDate));
		}

	}

	@OnClick({R.id.txStartDate, R.id.txEndDate})

	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.txStartDate:
				loadDialogFragment(REQUEST_DATE_FROM);
				break;
			case R.id.txEndDate:
				loadDialogFragment(REQUEST_DATE_TO);
				break;
		}
	}

	private void loadDialogFragment(int requestDate) {
		activityUtils.loadDialogFragment(DatePickerFragment.newInstance(), this,
				getFragmentManager(), requestDate, DIALOG_DATE);
	}
}
