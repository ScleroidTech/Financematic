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
import com.scleroid.financematic.fragments.customer.CustomerFragment;
import com.scleroid.financematic.fragments.dialogs.DatePickerDialogFragment;
import com.scleroid.financematic.utils.CommonUtils;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.TextValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.OnClick;
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

	@Inject
	DateUtils dateUtils;
	String[] country =
			{LoanDurationType.MONTHLY, LoanDurationType.DAILY, LoanDurationType.WEEKLY,
					LoanDurationType.BIWEEKLY, LoanDurationType.BIMONTHLY, LoanDurationType
					.QUARTERLY, LoanDurationType.HALF_YEARLY, LoanDurationType.YEARLY};
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

	private Button b;
	private TextView ettxloan_amout;
	private TextView startDateTextView;
	private TextView endDateTextView;
	private TextView ettxrateInterest;
	private TextView ettxInstallmentAmount;
	private TextView etTotalLoanAmount;
	private TextView ettxNoofInstallment;

	private String durationType = LoanDurationType.MONTHLY;
	private Date startDate;
	private Date endDate;
	private int customerId;
	private Loan loan;
	private int noOfInstallments1;

	private BigDecimal amtOfInstallment;
	private TextView customerNameTextView;
	private long durationDivided;

	private int installmentCalculationType = InstallmentCalculationType.PREDEFINED_INTEREST;

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
		initializeAllViews(rootView);
		Bundle bundle = getArguments();
		if (bundle != null) {
			setCustomerName(customerNameTextView, bundle);
		}
		final Spinner spin = rootView.findViewById(R.id.spinnertx);
		final Spinner spin1 = rootView.findViewById(R.id.spinnercalculate);
		spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				durationType = country[position];
				ettxNoofInstallment.setText(String.valueOf(getInstallments()));
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});
		spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				installmentCalculationType = position;
				ettxInstallmentAmount.setText(String.valueOf(getAmtOfInstallment()));
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

		ArrayAdapter<String> aaa = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_item, calculate);
		aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		spin1.setAdapter(aaa);


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
						getInterestAmt(BigDecimal.valueOf(
								Double.valueOf(etTotalLoanAmount.getText().toString().trim())),
								calculateInstallments(calculateTotalDuration(),
										durationConverter(LoanDurationType.MONTHLY)),
								Double.valueOf(ettxrateInterest.getText()
										.toString().trim()));
					}
				});

		ettxInstallmentAmount.addTextChangedListener(
				new TextValidator(ettxInstallmentAmount) {
					@Override
					public void validate(TextView textView, String text) {

						if (isNotValidAmt(text)) {
							ettxInstallmentAmount.setError("Valid Interest Amount");
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
			final String installmentAmt = ettxInstallmentAmount.getText()
					.toString();
			final String loanAmt = ettxloan_amout.getText()
					.toString();
			final String noOfInstallments = ettxNoofInstallment.getText()
					.toString();

			final String totatLoanAmt = etTotalLoanAmount.getText()
					.toString();
			final String rateOfInterest = ettxrateInterest.getText()
					.toString();
			String startDateStr = startDateTextView.getText().toString();
			String endDateStr = endDateTextView
					.getText()
					.toString();
			Timber.d(
					"Printing all values " + installmentAmt + "  " + loanAmt + "  " +
							noOfInstallments + " " + totatLoanAmt + " " + rateOfInterest + " " +
							startDateStr + " " + endDateStr);
			if (TextUtils.isEmpty(loanAmt)) {
				ettxloan_amout.setError("Enter Loan Amount");
			}
			if (TextUtils.isEmpty(rateOfInterest)) {
				ettxrateInterest.setError("Enter rate Interest");
			}
			if (TextUtils.isEmpty(installmentAmt)) {
				ettxInstallmentAmount.setError("Enter Interest Amount");
			}
			if (TextUtils.isEmpty(noOfInstallments)) {
				ettxNoofInstallment.setError("Enter No of Installment");
			}

			if (TextUtils.isEmpty(startDateStr)) { startDateTextView.setError("Start Date");}
			if (TextUtils.isEmpty(endDateStr)) {endDateTextView.setError("End Date");}
			if (TextUtils.isEmpty(totatLoanAmt)) {
				etTotalLoanAmount.setError("Total Loan Amount");
				return;
			}


			int accountNo = CommonUtils.getRandomInt();
			BigDecimal loanAmt1 = new BigDecimal(loanAmt.trim());
			Float rateOfInterest1 = Float.valueOf(rateOfInterest);


			try {
				amtOfInstallment = new BigDecimal(installmentAmt.trim());
			} catch (NumberFormatException nFe) {
				amtOfInstallment = new BigDecimal(Double.valueOf(installmentAmt.trim()));
			}
			noOfInstallments1 = Integer.valueOf(noOfInstallments);

			BigDecimal repayAmt = new BigDecimal(totatLoanAmt.trim());
			addData(accountNo, loanAmt1, rateOfInterest1, repayAmt);

		});


		return rootView;
	}

	private void initializeAllViews(final View rootView) {
		startDateTextView = rootView.findViewById(R.id.txStartDate);
		endDateTextView = rootView.findViewById(R.id.txEndDate);
		ettxloan_amout = rootView.findViewById(R.id.txloan_amout);
		ettxrateInterest = rootView.findViewById(R.id.txrateInterest);
		ettxInstallmentAmount = rootView.findViewById(R.id.txInstallmentAmount);
		ettxNoofInstallment = rootView.findViewById(R.id.txNoofInstallment);
		etTotalLoanAmount = rootView.findViewById(R.id.txTotalLoanAmount);
		customerNameTextView = rootView.findViewById(R.id.reg_fullname_detaills);


	}

	private void addData(final int accountNo, final BigDecimal loanAmt1,
	                     final Float rateOfInterest1, final BigDecimal repayAmt) {

		loan = new Loan(accountNo, loanAmt1, startDate,
				endDate, rateOfInterest1, amtOfInstallment,
				noOfInstallments1, durationType,
				repayAmt, customerId);

		final List<Installment> installments = createInstallments();
		saveData(loan, installments);
	}

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

	private List<Date> calculateDates(Date installmentDate, final long durationDivided) {
		long durationTypeDivider = durationConverter(durationType);
		List<Date> dates = new ArrayList<>();
		for (int i = 0; i < durationDivided; i++) {
			installmentDate = dateUtils.findDate(installmentDate, durationTypeDivider);
			dates.add(installmentDate);
		}
		return dates;
	}

	private void saveData(final Loan loan,
	                      final List<Installment> installments) {
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

	private boolean isNotValidAmt(String loan_amountval) {
		String EMAIL_PATTERN = "^[0-9_.-]*$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(loan_amountval);
		return !matcher.matches();
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
	@Override
	public BaseViewModel getViewModel() {
		return null;
	}

	private long getInstallments() {


		return  getInstallments(durationConverter(durationType));




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

	private BigDecimal getAmtOfInstallment() {
		final String totatLoanAmt = etTotalLoanAmount.getText()
				.toString();
		final String rateOfInterest = ettxrateInterest.getText()
				.toString();
		final long
				monthlyDuration =
				getInstallments(durationConverter(LoanDurationType.MONTHLY));
		final long
				duration = getInstallments();

		if (TextUtils.isEmpty(totatLoanAmt) || TextUtils.isEmpty(
				rateOfInterest) || monthlyDuration <= 0) {
			return new BigDecimal(0);
		}
		BigDecimal loanAmt = BigDecimal.valueOf(Double.valueOf(totatLoanAmt.trim()));
		double interestRate = Double.valueOf(rateOfInterest.trim());
		BigDecimal interestAmt = getInterestAmt(loanAmt, monthlyDuration, interestRate);

		return calculateInstallmentAmt(duration, loanAmt, interestAmt);

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
		ettxNoofInstallment.setText(String.valueOf(getInstallments()));

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

	private void loadDialogFragment(final int msg) {
		activityUtils.loadDialogFragment(DatePickerDialogFragment.newInstance(), this,
				getFragmentManager(), msg, DIALOG_DATE);
	}


	private BigDecimal calculateInstallmentAmt(final long duration, final BigDecimal loanAmt,
	                                           final BigDecimal interestAmt) {
		switch (installmentCalculationType) {
			case 0:
				return getPreDefinedInterestEMI(loanAmt, duration);

			case 1:
				return getInterestPlusPrincipleEMI(loanAmt, interestAmt);

			case 2:
				return interestAmt;

			default:
				return getPreDefinedInterestEMI(loanAmt, duration);

		}
	}

	private BigDecimal getPreDefinedInterestEMI(BigDecimal loanAmt, long duration) {
		return loanAmt.divide(BigDecimal.valueOf(duration));
	}

	private BigDecimal getInterestPlusPrincipleEMI(BigDecimal loanAmt, BigDecimal interest) {
		BigDecimal emi = loanAmt.add(interest.divide(BigDecimal.valueOf(
				calculateInstallments(calculateTotalDuration(), durationConverter(durationType)))));
		return emi;
	}

	private BigDecimal getInterestAmt(BigDecimal loanAmt, long duration, double interestRate) {
		BigDecimal interestAmt =
				loanAmt.multiply(BigDecimal.valueOf((interestRate / 100) * duration));
		return interestAmt;

	}

}
