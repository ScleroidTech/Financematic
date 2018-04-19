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
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.LoanDurationType;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.fragments.dialogs.DatePickerDialogFragment;
import com.scleroid.financematic.utils.CommonUtils;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.TextValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by scleroid on 9/3/18.
 */


public class RegisterLoanFragment extends BaseFragment {

	private static final String CUSTOMER_ID = "customer_id";

	private static final String DIALOG_DATE = "DIALOG_DATE";
	private static final int REQUEST_DATE_FROM = 1;
	private static final int REQUEST_DATE_TO = 2;

	@Inject
	DateUtils dateUtils;
	String[] country =
			{LoanDurationType.MONTHLY, LoanDurationType.DAILY, LoanDurationType.WEEKLY,
					LoanDurationType.BIWEEKLY, LoanDurationType.BIMONTHLY, LoanDurationType
					.QUARTERLY, LoanDurationType.HALF_YEARLY, LoanDurationType.YEARLY};
	@Inject
	LocalCustomerLab customerLab;
	@Inject
	LoanRepo loanRepo;

	@Inject
	InstallmentRepo installmentRepo;
	@Inject
	ActivityUtils activityUtils;
	private Button b;
	private TextView ettxloan_amout;
	private TextView startDateTextView;
	private TextView endDateTextView;
	private TextView ettxrateInterest;
	private TextView ettxInterestAmount;
	private TextView ettxInstallmentduration;
	private TextView etTotalLoanAmount;
	private TextView ettxNoofInstallment;

	private String durationType = LoanDurationType.MONTHLY;
	private Date startDate;
	private Date endDate;
	private int customerId;
	private Loan loan;
	private int noOfInstallments1;
	private int duration1;
	private BigDecimal amtOfInterest;


	public RegisterLoanFragment() {
		// Required empty public constructor
	}

	public static RegisterLoanFragment newInstance(int customer_id) {
		RegisterLoanFragment fragment = new RegisterLoanFragment();
		Bundle args = new Bundle();
		args.putInt(CUSTOMER_ID, customer_id);
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
			setCustomerName(customerNameTextView, bundle);
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
		ettxInterestAmount = rootView.findViewById(R.id.txInstallmentAmount);
		ettxNoofInstallment = rootView.findViewById(R.id.txNoofInstallment);
		ettxInstallmentduration = rootView.findViewById(R.id.txInstallmentduration);
		etTotalLoanAmount = rootView.findViewById(R.id.txTotalLoanAmount);

		ettxloan_amout.addTextChangedListener(
				new TextValidator(ettxloan_amout) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(text)) {
							ettxloan_amout.setError("Valid total given money");
						}
					}
				});


		ettxrateInterest.addTextChangedListener(
				new TextValidator(ettxrateInterest) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(text)) {
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

						if (isNotValidAmt(text)) {
							ettxInterestAmount.setError("Valid Interest Amount");
						}
					}
				});

		ettxNoofInstallment.addTextChangedListener(
				new TextValidator(ettxNoofInstallment) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(text)) {
							ettxNoofInstallment.setError("valid No of Installment");
						}
					}
				});

		ettxInstallmentduration.addTextChangedListener(
				new TextValidator(ettxInstallmentduration) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(text)) {
							ettxInstallmentduration.setError("Valid Installment Duration ");
						}
					}
				});

		etTotalLoanAmount.addTextChangedListener(
				new TextValidator(etTotalLoanAmount) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(text)) {
							etTotalLoanAmount.setError("Valid Total Amount");
						}
					}
				});


		b = rootView.findViewById(R.id.btn_givenmoney);
		b.setOnClickListener(v -> {
			final String loanAmt = ettxloan_amout.getText()
					.toString();
			final String noOfInstallments = ettxNoofInstallment.getText()
					.toString();
			final String duration = ettxInstallmentduration.getText()
					.toString();
			final String totatLoanAmt = etTotalLoanAmount.getText()
					.toString();
			final String rateOfInterest = ettxrateInterest.getText()
					.toString();
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


			int accountNo = CommonUtils.getRandomInt();
			BigDecimal loanAmt1 = new BigDecimal(loanAmt);
			Float rateOfInterest1 = Float.valueOf(rateOfInterest);
			amtOfInterest = new BigDecimal(interestAmt);
			noOfInstallments1 = Integer.valueOf(noOfInstallments);
			duration1 = Integer.valueOf(duration);
			BigDecimal repayAmt = new BigDecimal(totatLoanAmt);
			addData(accountNo, loanAmt1, rateOfInterest1, repayAmt);
		});


		return rootView;
	}

	private void addData(final int accountNo, final BigDecimal loanAmt1,
	                     final Float rateOfInterest1, final BigDecimal repayAmt) {
		final List<Installment> installments = createInstallments();

		loan = new Loan(accountNo, loanAmt1, startDate,
				endDate, rateOfInterest1, amtOfInterest,

				noOfInstallments1, duration1, durationType,

				repayAmt, customerId);
		saveData(loan, installments);
	}

	private List<Installment> createInstallments() {
		List<Date> dates = calculateInstallmentsDates();
		List<Installment> installments = Collections.EMPTY_LIST;
		for (Date date : dates) {
			Installment installment =
					new Installment(CommonUtils.getRandomInt(), date, amtOfInterest,
							loan.getAccountNo());
			installments.add(installment);
		}
		return installments;
	}

	private List<Date> calculateInstallmentsDates() {
		List<Date> dates = Collections.emptyList();
		Date installmentDate = startDate;
		final long totalDuration;
		totalDuration = dateUtils.differenceOfDates(startDate, endDate);
		long durationTypeDivider = durationConverter(durationType);
		final long durationDivided = convertTime(totalDuration);
		for (int i = 1; i < durationDivided; i++) {


			installmentDate = dateUtils.findDate(installmentDate, durationTypeDivider);
			dates.add(installmentDate);
		}
		return dates;


	}


	private long convertTime(long timeDiff) {

		long divider = durationConverter(durationType);


		return (TimeUnit.MILLISECONDS.toDays(timeDiff) / divider);
	}

	private long durationConverter(final String durationType) {
		long divider = 0;
		switch (durationType) {
			case LoanDurationType.MONTHLY:
				divider = 30;
				break;
			case LoanDurationType.DAILY:
				divider = 1;
				break;
			case LoanDurationType.WEEKLY:
				divider = 7;
				break;
			case LoanDurationType.BIWEEKLY:
				divider = 15;
				break;
			case LoanDurationType.BIMONTHLY:
				divider = 60;
				break;
			case LoanDurationType.QUARTERLY:
				divider = 90;
				break;
			case LoanDurationType.HALF_YEARLY:
				divider = 180;
				break;
			case LoanDurationType.YEARLY:
				divider = 365;
				break;

		}
		return divider;
	}

	private void saveData(final Loan loan,
	                      final List<Installment> installments) {
		loanRepo.saveItem(loan).observeOn(AndroidSchedulers.mainThread()).subscribe(loan1 -> {
			Timber.d("Loan Data Saved " + loan1.toString());
			installmentRepo.saveItems(installments)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(() -> Timber.d("Installments Created " + loan1.toString()));

		}, throwable -> Timber.d(
				"Loan Data not Saved " + throwable.getMessage() + " errors are " + loan
						.toString()));
	}

	private void setCustomerName(final TextView customerNameTextView, final Bundle bundle) {
		customerId = bundle.getInt(CUSTOMER_ID);

		/*Observable.just(customerLab.getRxItem(customerId))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe((Single<Customer> customer) -> {
					customer.subscribe(customer1 ->customerNameTextView.setText(customer1.getName
					()) );
							Timber.d("data received, displaying");
							//customerNameTextView.setText(customer.g);
						},
						throwable -> Timber.d("Not gonna show up")
				);*/
		Disposable subscribe = customerLab
				.getRxItem(customerId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(customer -> {
							Timber.d("data received, displaying " + customer.toString());
							customerNameTextView.setText("Customer Name : " + customer.getName());
						},
						throwable -> Timber.d("Not gonna show up " + throwable.getMessage()));
		//	customerNameTextView.setText(customer.getName());
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.registor_given_money;
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
		String EMAIL_PATTERN ="^[a-zA-Z0-9_.-]*$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(loan_amountval);
		return !matcher.matches();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_DATE_FROM) {
			startDate = (Date) intent.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
			startDateTextView.setText(dateUtils.getFormattedDate(startDate));
		} else if (requestCode == REQUEST_DATE_TO) {
			endDate = (Date) intent.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
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
		activityUtils.loadDialogFragment(DatePickerDialogFragment.newInstance(), this,
				getFragmentManager(), requestDate, DIALOG_DATE);
	}
}
