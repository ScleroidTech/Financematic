package com.scleroid.financematic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.scleroid.financematic.fragments.customer.CustomerFragment;
import com.scleroid.financematic.fragments.dialogs.DatePickerDialogFragment;
import com.scleroid.financematic.utils.CommonUtils;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.TextValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
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
	private static final String[] EMPTY_ARRAY = new String[0];
	@Inject
	DateUtils dateUtils;
	@NonNull
	List<String> country;
	@Nullable
	@BindView(R.id.spinnertx)
	Spinner spinnerTypeOfInstallment;
	@Nullable
	@BindView(R.id.spinnercalculate)
	Spinner spinnerCalculate;
	@NonNull
	String[] calculate =
			{"Pre-Defined Interest", "Interest including Principal", "Interest only"};

	@Inject
	LocalCustomerLab customerLab;
	@Inject
	LoanRepo loanRepo;

	@Inject
	InstallmentRepo installmentRepo;
	@Inject
	ActivityUtils activityUtils;
	@Nullable
	@BindView(R.id.reg_fullname_detaills)
	TextView customerNameTextView;

	@Nullable
	@BindView(R.id.txloan_amout)
	EditText ettxloan_amout;
	@Nullable
	@BindView(R.id.txStartDate)
	TextView startDateTextView;
	@Nullable
	@BindView(R.id.txEndDate)
	TextView endDateTextView;
	@Nullable
	@BindView(R.id.interest_radio_button)
	RadioButton interestRadioButton;
	@Nullable
	@BindView(R.id.rate_of_interest_radio_button)
	RadioButton rateOfInterestRadioButton;
	@Nullable
	@BindView(R.id.radio_interest)
	RadioGroup radioInterest;
	@Nullable
	@BindView(R.id.txrateInterest)
	EditText ettxrateInterest;
	@Nullable
	@BindView(R.id.txinterestamount)
	EditText txInterestAmount;
	private ArrayAdapter<String> adapterInstallmentType;
	@Nullable
	@BindView(R.id.txNoofInstallment)
	TextView ettxNoofInstallment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		super.onCreateView(inflater, container, savedInstanceState);
		final View rootView = getRootView();
		//	unbinder = ButterKnife.bind(this, rootView);
		//initializeAllViews(rootView);
		Bundle bundle = getArguments();
		if (bundle != null) {
			setCustomerName(customerNameTextView, bundle);
		}


		spinnerTypeOfInstallment.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				durationType = getCountry(position);
				ettxNoofInstallment.setText(String.valueOf(getInstallments()));
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});
		spinnerCalculate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				Timber.d("Position of Spinner" + position);
				installmentCalculationType = position;
				String s = updateInstallmentAmount();
				Timber.d("Value of Decimal" + s);
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});

		/*        final String text = spinnerTypeOfInstallment.getSelectedItem().toString();*/
		//Creating the ArrayAdapter instance having the filterSuggestions list
		setCountry(calculateDurationDifferenceInDays());
		adapterInstallmentType = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_item, country);
		adapterInstallmentType.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		spinnerTypeOfInstallment.setAdapter(adapterInstallmentType);

		ArrayAdapter<String> aaa = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_item, calculate);
		aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		spinnerCalculate.setAdapter(aaa);


		ettxloan_amout.addTextChangedListener(
				new TextValidator(ettxloan_amout) {
					@Override
					public void validate(TextView textView, @NonNull String text) {

						if (isNotValidAmt(text)) {
							ettxloan_amout.setError("Valid total given money");
						} else {
							updateInterestAmt();
							updateInstallmentAmount();
						}
					}
				});


		ettxInstallmentAmount.addTextChangedListener(
				new TextValidator(ettxInstallmentAmount) {
					@Override
					public void validate(TextView textView, @NonNull String text) {

						if (isNotValidAmt(text)) {
							ettxInstallmentAmount.setError("Valid Interest Amount");
						} else {
							updateInterestAmt();
							updateInstallmentAmount();
						}
					}
				});

		ettxNoofInstallment.addTextChangedListener(
				new TextValidator(ettxNoofInstallment) {
					@Override
					public void validate(TextView textView, @NonNull String text) {

						if (isNotValidAmt(text)) {
							ettxNoofInstallment.setError("valid No of Installment");
						} else {
							updateInterestAmt();
							updateInstallmentAmount();
						}
					}
				});



		radioInterest.setOnCheckedChangeListener((group, checkedId) -> {
			if (interestRadioButton.getId() == checkedId && interestRadioButton.isChecked()) {
				rateOfInterestLayout.setVisibility(View.GONE);
				txInterestAmount.setEnabled(true);
			} else if (rateOfInterestRadioButton.getId() == checkedId && rateOfInterestRadioButton
					.isChecked()) {
				rateOfInterestLayout.setVisibility(View.VISIBLE);
				txInterestAmount.setEnabled(false);
				setRateOfInterest();
				updateInterestAmt();
				updateInstallmentAmount();
			}

		});




		return rootView;
	}

	@Nullable
	@BindView(R.id.txInstallmentAmount)
	EditText ettxInstallmentAmount;

	//EditText etTotalLoanAmount;
	@Nullable
	@BindView(R.id.btn_givenmoney)
	Button btnGiveMoney;
	@Nullable
	@BindView(R.id.displaytx)
	TextView displaytx;
	Unbinder unbinder;
	@BindView(R.id.rate_of_interest_layout)
	LinearLayout rateOfInterestLayout;
	Unbinder unbinder1;


	private String durationType = LoanDurationType.MONTHLY;
	private Date startDate;
	private Date endDate;
	private int customerId;
	private Loan loan;
	private int noOfInstallments1;

	private BigDecimal amtOfInstallment;
	private long durationDivided;

	private int installmentCalculationType = InstallmentCalculationType.PREDEFINED_INTEREST;

	@NonNull
	public String getCountry(final int position) {
		return country.get(position);
	}

	public RegisterLoanFragment() {
		// Required empty public constructor
	}

	@NonNull
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

	public void setCountry(@NonNull final long duration) {
		country = Collections.emptyList();

		if (duration >= 1) country.add(LoanDurationType.DAILY);
		if (duration >= 30 || duration == 0) country.add(LoanDurationType.MONTHLY);
		if (duration >= 7) country.add(LoanDurationType.WEEKLY);
		if (duration >= 15) country.add(LoanDurationType.BIWEEKLY);
		if (duration >= 60) country.add(LoanDurationType.BIMONTHLY);
		if (duration >= 90) {
			country.add(LoanDurationType
					.QUARTERLY);
		}
		if (duration >= 183) country.add(LoanDurationType.HALF_YEARLY);
		if (duration >= 365) country.add(LoanDurationType.YEARLY);
	}

	private String updateInstallmentAmount() {
		String s = getAmtOfInstallment().toPlainString();
		ettxInstallmentAmount.setText(s);
		return s;
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_new_loan_register;
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
	@Nullable
	@Override
	public BaseViewModel getViewModel() {
		return null;
	}

	private void addData(final int accountNo, final BigDecimal loanAmt1,
	                     final BigDecimal interestAmt) {

		loan = new Loan(accountNo, loanAmt1, startDate,
				endDate, interestAmt, amtOfInstallment,
				noOfInstallments1, durationType, customerId);

		final List<Installment> installments = createInstallments();
		saveData(loan, installments);
	}

	private void setCustomerName(@NonNull final TextView customerNameTextView,
	                             final Bundle bundle) {
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

	private void setRateOfInterest() {
		if (rateOfInterestLayout.getVisibility() == View.VISIBLE) {

			ettxrateInterest.addTextChangedListener(
					new TextValidator(ettxrateInterest) {
						@Override
						public void validate(TextView textView, @NonNull String text) {

							if (isNotValidAmt(text)) {
								ettxrateInterest.setError("Valid valid Rate in %");
							} else {
								updateInterestAmt();
								updateInstallmentAmount();
							}
						}
					});
		}

	}

	private boolean isNotValidAmt(@NonNull String loan_amountval) {
		String EMAIL_PATTERN = "^[0-9_.-]*$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(loan_amountval);
		return !matcher.matches();
	}

	@NonNull
	private List<Installment> createInstallments() {
		List<Date> dates = calculateDates(startDate, durationDivided);
		List<Installment> installments = new ArrayList<>();
		for (Date date : dates) {
			Installment installment =
					new Installment(CommonUtils.getRandomInt(), date, amtOfInstallment,
							loan.getAccountNo());
			installments.add(installment);
		}
		return installments;
	}

	private BigDecimal getAmtOfInstallment() {
		final String totatLoanAmt = ettxloan_amout.getText()
				.toString();

		final long
				duration = getInstallments();

		if (TextUtils.isEmpty(totatLoanAmt)) {
			return getBigDecimal();
		}
		BigDecimal loanAmt = getBigDecimal(Double.valueOf(totatLoanAmt.trim()));

		BigDecimal interestAmt = getInterestAmt(loanAmt);
		if (interestAmt.intValue() == 0) return getBigDecimal();

		return calculateInstallmentAmt(duration, loanAmt, interestAmt);

	}

	@NonNull
	private List<Date> calculateDates(Date installmentDate, final long durationDivided) {
		long durationTypeDivider = durationConverter(durationType);
		List<Date> dates = new ArrayList<>();
		for (int i = 0; i < durationDivided; i++) {
			installmentDate = dateUtils.findDate(installmentDate, durationTypeDivider);
			dates.add(installmentDate);
		}
		return dates;
	}

	private BigDecimal calculateInstallmentAmt(final long duration,
	                                           @NonNull final BigDecimal loanAmt,
	                                           @NonNull final BigDecimal interestAmt) {
		switch (installmentCalculationType) {
			case 0:
				return divideBigDecimal(loanAmt, duration);

			case 1:
				return getInterestPlusPrincipleEMI(loanAmt, interestAmt, duration);

			case 2:
				return divideBigDecimal(interestAmt, duration);

			default:
				return divideBigDecimal(loanAmt, duration);

		}
	}

	private BigDecimal divideBigDecimal(BigDecimal loanAmt, long duration) {
		return loanAmt.divide(BigDecimal.valueOf(duration), 2, RoundingMode.HALF_EVEN);
	}

	private void saveData(@NonNull final Loan loan,
	                      @NonNull final List<Installment> installments) {
		Disposable subscribe = loanRepo.saveItem(loan)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(() -> {
					Timber.d("Loan Data Saved ");
					Toasty.success(getBaseActivity(), "Loan Created successfully").show();
					installmentRepo.saveItems(installments)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(() -> {
										Timber.d("Installments Created ");
										activityUtils.loadFragment(
												CustomerFragment.newInstance(loan.getCustId()),
												getFragmentManager());
									}
							);

				}, throwable -> {
					Timber.d(
							"Loan Data not Saved " + throwable.getMessage() + " errors are " + loan
									.toString());
					Toasty.error(getBaseActivity(),
							"Loan Data not Saved " + throwable.getMessage() + " errors are " + loan
									.toString()).show();
				});
	}


	private long calculateDurationDifferenceInDays() {
		if (startDate == null || endDate == null) return 0;


		durationDivided = endDate.getDate() - startDate.getDate();
		long diffInDays = TimeUnit.MILLISECONDS.toDays(durationDivided);


		return diffInDays;

	}

	private BigDecimal getInterestPlusPrincipleEMI(BigDecimal loanAmt, BigDecimal interest,
	                                               final long duration) {
		BigDecimal emi = divideBigDecimal(loanAmt.add(interest), duration);
		return emi;
	}

	private BigDecimal getInterestOnly(final long duration, final @NonNull BigDecimal
			interestAmt) {
		return divideBigDecimal(interestAmt, duration);
	}

	private void updateInterestAmt() {
		if (TextUtils.isEmpty(ettxloan_amout.getText().toString())) {
			ettxloan_amout.setError("Loan Amount cannot be empty");
			return;
		}
		BigDecimal interestAmt = getInterestAmt(
				getBigDecimal(Double.valueOf(ettxloan_amout.getText().toString().trim
						())));
		txInterestAmount.setText(
				interestAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN).toPlainString());
	}

	private long getInstallments() {


		return getInstallments(durationConverter(durationType));


	}

	private long getInstallments(long converter) {
		if (startDate == null || endDate == null) return 0;

		durationDivided = calculateInstallments(calculateTotalDuration(), converter);


		return durationDivided;
	}

	private long calculateInstallments(long timeDiff, long divider) {


		return (TimeUnit.MILLISECONDS.toDays(timeDiff) / divider);
	}

	private long calculateTotalDuration() {
		return dateUtils.differenceOfDates(startDate, endDate);
	}

	private long durationConverter(final String durationType) {

		switch (durationType) {
			case LoanDurationType.MONTHLY:
				return 30;

			case LoanDurationType.DAILY:
				return 1;

			case LoanDurationType.WEEKLY:
				return 7;

			case LoanDurationType.BIWEEKLY:
				return 15;

			case LoanDurationType.BIMONTHLY:
				return 60;

			case LoanDurationType.QUARTERLY:
				return 90;

			case LoanDurationType.HALF_YEARLY:
				return 180;

			case LoanDurationType.YEARLY:
				return 365;
		}
		return 0;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @NonNull Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_DATE_FROM) {
			startDate = (Date) intent.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
			startDateTextView.setText(dateUtils.getFormattedDate(startDate));
		} else if (requestCode == REQUEST_DATE_TO) {
			endDate = (Date) intent.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
			endDateTextView.setText(dateUtils.getFormattedDate(endDate));
		}
		ettxNoofInstallment.setText(String.valueOf(getInstallments()));
		updateInterestAmt();
		setCountry(calculateDurationDifferenceInDays());

	}

	private BigDecimal getInterestAmt(@NonNull BigDecimal loanAmt) {

		if (rateOfInterestLayout.getVisibility() == View.VISIBLE) {

			String rateOfInterest = ettxrateInterest.getText()
					.toString();
			final double
					monthlyDuration =
					convertTime(calculateTotalDuration(),
							durationConverter(LoanDurationType.MONTHLY));
			if (TextUtils.isEmpty(
					rateOfInterest) || monthlyDuration <= 0) {
				return getBigDecimal();
			}
			double interestRate = Double.valueOf(rateOfInterest.trim());
			BigDecimal interestAmt =
					calculateInterestAmt(loanAmt, monthlyDuration, interestRate);
			return interestAmt;
		} else if (!TextUtils.isEmpty(txInterestAmount.getText().toString())) {
			return getBigDecimal(Double.parseDouble(txInterestAmount.getText().toString
					()));
		}
		return getBigDecimal();
	}

	private double convertTime(long timeDiff, long divider) {


		return (double) TimeUnit.MILLISECONDS.toDays(timeDiff) / (double) divider;
	}

	private BigDecimal getBigDecimal() {
		return getBigDecimal(0.0);
	}

	private BigDecimal getBigDecimal(final Double value) {
		return BigDecimal.valueOf(value).setScale(2,
				BigDecimal.ROUND_HALF_EVEN);
	}

	private BigDecimal calculateInterestAmt(BigDecimal loanAmt, double duration,
	                                        double interestRate) {

		return loanAmt.multiply(getBigDecimal((interestRate / 100) * duration));
	}

	@OnClick({R.id.txStartDate, R.id.txEndDate})

	public void onViewClicked(@NonNull View view) {
		switch (view.getId()) {
			case R.id.txStartDate:
				loadDialogFragment(REQUEST_DATE_FROM);
				break;
			case R.id.txEndDate:
				loadDialogFragment(REQUEST_DATE_TO, startDate);
				break;
		}
	}

	@Deprecated
	private void initializeAllViews(final View rootView) {
		startDateTextView = rootView.findViewById(R.id.txStartDate);
		endDateTextView = rootView.findViewById(R.id.txEndDate);
		ettxloan_amout = rootView.findViewById(R.id.txloan_amout);
		ettxrateInterest = rootView.findViewById(R.id.txrateInterest);
		ettxInstallmentAmount = rootView.findViewById(R.id.txInstallmentAmount);
		ettxNoofInstallment = rootView.findViewById(R.id.txNoofInstallment);
		//etTotalLoanAmount = rootView.findViewById(R.id.txTotalLoanAmount);
		customerNameTextView = rootView.findViewById(R.id.reg_fullname_detaills);


	}

	private void loadDialogFragment(final int msg, Date minDate) {
		if (minDate != null) {
			activityUtils.loadDialogFragment(DatePickerDialogFragment.newInstance(minDate, true),
					this,
					getFragmentManager(), msg, DIALOG_DATE);
		} else {
			activityUtils.loadDialogFragment(DatePickerDialogFragment.newInstance(), this,
					getFragmentManager(), msg, DIALOG_DATE);
		}
	}

	private void loadDialogFragment(final int msg) {
		activityUtils.loadDialogFragment(DatePickerDialogFragment.newInstance(), this,
				getFragmentManager(), msg, DIALOG_DATE);
	}

	@OnClick(R.id.btn_givenmoney)
	public void onViewClicked() {
		final String installmentAmt = ettxInstallmentAmount.getText()
				.toString();
		final String loanAmt = ettxloan_amout.getText()
				.toString();
		final String noOfInstallments = ettxNoofInstallment.getText()
				.toString();

		/*	final String totatLoanAmt = etTotalLoanAmount.getText()
					.toString();*/
		final String rateOfInterest = ettxrateInterest.getText()
				.toString();
		final String interestAmt = txInterestAmount.getText()
				.toString();
		String startDateStr = startDateTextView.getText().toString();
		String endDateStr = endDateTextView
				.getText()
				.toString();
		Timber.d(
				"Printing all values " + installmentAmt + "  " + loanAmt + "  " +
						noOfInstallments + " " + rateOfInterest + " " +
						startDateStr + " " + endDateStr);
		if (TextUtils.isEmpty(loanAmt)) {
			ettxloan_amout.setError("Enter Loan Amount");
		}
		if (TextUtils.isEmpty(
				rateOfInterest) && rateOfInterestLayout.getVisibility() == View.VISIBLE) {
			ettxrateInterest.setError("Enter rate Interest");
		} else if (TextUtils.isEmpty(interestAmt)) {
			txInterestAmount.setError("Enter Interest Amount");
		}
		if (TextUtils.isEmpty(installmentAmt)) {
			ettxInstallmentAmount.setError("Enter Installment Amount");
		}
		if (TextUtils.isEmpty(noOfInstallments)) {
			ettxNoofInstallment.setError("Enter No of Installment");
		} else if (noOfInstallments.contains("0")) {
			ettxNoofInstallment.setError("No of installment must not be 0");
		}

		if (TextUtils.isEmpty(startDateStr)) { startDateTextView.setError("Start Date");}
		if (TextUtils.isEmpty(endDateStr)) {endDateTextView.setError("End Date");}


		int accountNo = CommonUtils.getRandomInt();
		BigDecimal loanAmt1 = new BigDecimal(loanAmt.trim());
		BigDecimal interestAmt1 = new BigDecimal(interestAmt.trim());


		try {
			amtOfInstallment = new BigDecimal(installmentAmt.trim());
		} catch (NumberFormatException nFe) {
			amtOfInstallment = new BigDecimal(Double.valueOf(installmentAmt.trim()));
		}
		noOfInstallments1 = Integer.valueOf(noOfInstallments);

		//	BigDecimal repayAmt = new BigDecimal(totatLoanAmt.trim());
		addData(accountNo, loanAmt1, interestAmt1);

	}


}
