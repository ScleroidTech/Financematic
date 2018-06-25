package com.scleroid.financematic.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseDialog;
import com.scleroid.financematic.data.local.Session;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.data.repo.LoanRepo;
import com.scleroid.financematic.data.repo.TransactionsRepo;
import com.scleroid.financematic.utils.CommonUtils;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.TextValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by scleroid on 9/3/18.
 */

public class RegisterReceivedDialogFragment extends BaseDialog {


	private static final String INSTALLMENT_ID = "installment_id";
	private static final String ACCOUNT_NO = "account_no";
	Calendar myCalendar = Calendar.getInstance();
	@NonNull
	String[] country = {"Received on Time", "Delayed payment", "Less amount", "Other"};
	@Inject
	InstallmentRepo installmentRepo;
	@Inject
	LoanRepo loanRepo;
	@Inject
	DateUtils dateUtils;

	@Inject
	Session sesson;

	@Inject
	TransactionsRepo transactionsRepo;
	private TextView etrxDate;
	private TextView etrxReceivedAmount;

	private EditText etrxOtherDescription;

	private LinearLayout otherDescLayout;
	private int accountNo;
	private int installmentId;
	private Date paymentDate;
	private String description;
	private Loan loan;
	private Installment currentInstallment;
	private Date installmentDate;

	public RegisterReceivedDialogFragment() {
		// Required empty public constructor
	}

	@NonNull
	public static RegisterReceivedDialogFragment newInstance(int accountNo, int installmentId) {
		RegisterReceivedDialogFragment fragment = new RegisterReceivedDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ACCOUNT_NO, accountNo);
		args.putInt(INSTALLMENT_ID, installmentId);
		fragment.setArguments(args);
		return fragment;
	}

/*
	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
	}
*/


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		super.onCreateDialog(savedInstanceState);
		View rootView =
				LayoutInflater.from(getActivity()).inflate(R.layout.registor_received_amount,
						null);
		Bundle bundle = getArguments();
		etrxDate = rootView.findViewById(R.id.rxDate);
		etrxReceivedAmount = rootView.findViewById(R.id.rxReceivedAmount);
		etrxOtherDescription = rootView.findViewById(R.id.rxdescriptionother);
		otherDescLayout = rootView.findViewById(R.id.other_reason_view);
		final Spinner spin = rootView.findViewById(R.id.spinnerrx);
		if (bundle != null) {
			accountNo = bundle.getInt(ACCOUNT_NO);
			installmentId = bundle.getInt(INSTALLMENT_ID);
			getInstallment();
		}


		otherDescLayout.setVisibility(View.GONE);
		spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				description = country[position];
				if (position < 3) {
					otherDescLayout.setVisibility(View.GONE);
				} else { otherDescLayout.setVisibility(View.VISIBLE); }
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


		etrxReceivedAmount.addTextChangedListener(new TextValidator(etrxReceivedAmount) {
			@Override
			public void validate(TextView textView, String text) {

				final String email = etrxReceivedAmount.getText().toString();
				if (!isValidEmail(email)) {
					etrxReceivedAmount.setError("Enter Amount");
				}
			}
		});

		final DatePickerDialog.OnDateSetListener dateSetListener =
				(view, year, monthOfYear, dayOfMonth) -> {
					// TODO Auto-generated method stub
					myCalendar.set(Calendar.YEAR, year);
					myCalendar.set(Calendar.MONTH, monthOfYear);
					myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					updateLabel();
				};

		etrxDate.setOnClickListener(v -> {

			new DatePickerDialog(getBaseActivity(), dateSetListener, myCalendar
					.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH)).show();

		});
		MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(getActivity());
		updateTitle(builder);


		return builder
				.setCustomView(rootView)
				.setStyle(Style.HEADER_WITH_ICON)
				.withIconAnimation(true)
				.setIcon(R.drawable.ic_stopwatch)
				.setPositiveText(R.string.submit)
				.onPositive((MaterialDialog dialog, DialogAction which) -> {
					if (etrxReceivedAmount.getText() == null) {
						Toasty.error(getBaseActivity(), "You haven't filled Amount").show();
						return;
					} else if (paymentDate == null) {
						Toasty.error(getBaseActivity(), "You haven't set the date").show();
						return;
					} else if (description
							== null) {
						Toasty.error(getBaseActivity(), "You haven't selected description").show();
						return;
					} else if ((otherDescLayout.getVisibility() == View.VISIBLE &&
							etrxOtherDescription
									.getText() == null)) {
						Toasty.error(getBaseActivity(),
								"You haven't added the reason for description").show();
						return;
					}
					Timber.d(
							"Your Input: \n" + etrxDate.getText()
									.toString() + "\n" + "\n" + etrxReceivedAmount.getText()
									.toString() + "\nEnd.");
					BigDecimal receivedAmt =
							new BigDecimal(etrxReceivedAmount.getText().toString());
					Installment expense = createInstallment(receivedAmt);
					TransactionModel transaction = createTransaction(receivedAmt);
					updateReceivedAmount(receivedAmt);
					updateLoan(expense, transaction);

				})
				.show();

	}

	@SuppressLint("CheckResult")
	private void getInstallment() {
		installmentRepo.getLocalInstallmentsLab()
				.getRxItem(installmentId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(customer -> {
							currentInstallment = customer;
							Timber.d("data received, displaying " + customer.toString());
							final String installmentAmt = currentInstallment.getExpectedAmt()
									.toPlainString();
							installmentDate = currentInstallment.getInstallmentDate();
							myCalendar.setTime(installmentDate);

							etrxDate.setText(dateUtils.getFormattedDate(installmentDate));
							etrxReceivedAmount.setText(installmentAmt);
							updateLabel();

						},
						throwable -> Timber.d("Not gonna show up " + throwable.getMessage()));
	}

	private void updateLabel() {
		String myFormat = "MM/dd/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		paymentDate = myCalendar.getTime();
		etrxDate.setText(sdf.format(paymentDate));
	}

	private boolean isValidEmail(@NonNull String email) {
		String EMAIL_PATTERN = "^[0-9_.]*$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@NonNull
	private TransactionModel createTransaction(final BigDecimal receivedAmt) {
		if (description.equals(country[3])) {
			description = etrxOtherDescription.getText().toString();
		}
		return new TransactionModel(CommonUtils.getRandomInt(), paymentDate, null,
				getEarnedAmount(),
				receivedAmt, description, accountNo);
	}

	private BigDecimal getEarnedAmount() {
		return loan.getInstallmentAmt()
				.divide(BigDecimal.valueOf(loan.getNoOfInstallments()), 2, RoundingMode.HALF_EVEN);
	}

	private void updateTitle(@NonNull final MaterialStyledDialog.Builder builder) {
		loanRepo.getLocalLoanLab()
				.getRxItem(accountNo)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(customer -> {
							loan = customer;
							Timber.d("data received, displaying " + customer.toString());
							builder.setTitle("Receive Payment for " + customer.getAccountNo());

						},
						throwable -> Timber.d("Not gonna show up " + throwable.getMessage()));
	}

	@NonNull
	private Installment createInstallment(final BigDecimal receivedAmt) {
		return new Installment(installmentId, paymentDate,
				receivedAmt,
				accountNo);
	}

	private void updateReceivedAmount(final BigDecimal receivedAmt) {
		loan.setReceivedAmt(loan.getReceivedAmt().add(receivedAmt));

	}

	private void updateLoan(@NonNull final Installment expense,
	                        @NonNull final TransactionModel transaction) {
		loanRepo.updateItem(loan)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(() -> {
					Timber.d(
							"data updated for loan ");

					saveTransaction(expense, transaction);

				});
	}

	@NonNull
	private Disposable saveTransaction(@NonNull final Installment expense,
	                                   @NonNull final TransactionModel transaction) {
		return transactionsRepo.saveItem(transaction).observeOn(AndroidSchedulers
				.mainThread())
				.subscribe(() -> {
					Toasty.success(
							Objects.requireNonNull(
									RegisterReceivedDialogFragment.this
											.getContext()),
							"Transaction has been recorded")
							.show();
					Timber.d(
							"data added transaction ");
					sesson.updateAmount(transaction.getReceivedAmt().floatValue());
					deleteInstallment(expense, transaction);
				});
	}

	private void deleteInstallment(final Installment expense,
	                               @NonNull final TransactionModel transaction) {
		if (expense.getExpectedAmt()
				.intValue() > currentInstallment.getExpectedAmt().intValue()) {
			installmentRepo.deleteItem(expense)
					.observeOn(
							AndroidSchedulers.mainThread())
					.subscribe(() -> {
								Toasty.success(
										Objects.requireNonNull(
												RegisterReceivedDialogFragment.this
														.getContext()),
										"Installment has been paid" +
												" Successfully")
										.show();
								Timber.d(
										"data removed for Installment ");
						transactionsRepo.saveItem(transaction).observeOn(
								AndroidSchedulers.mainThread()).subscribe(() -> {
							updateInstallmentViaEventBus(expense.getExpectedAmt()
									.subtract(currentInstallment.getExpectedAmt()));

						});


							}, throwable -> {
								Toasty.error(getBaseActivity(),
										"Details Not Updated, Try" +
												" " +
												"again" +
												" Later")
										.show();
								Timber.e("data  not updated for " + expense
								);
							}
					);
		} else if (expense.getExpectedAmt()
				.equals(currentInstallment.getExpectedAmt())) {
			installmentRepo.deleteItem(expense)
					.observeOn(
							AndroidSchedulers.mainThread())
					.subscribe(() -> {
								Toasty.success(
										Objects.requireNonNull(
												RegisterReceivedDialogFragment.this
														.getContext()),
										"Installment has been paid" +
												" Successfully")
										.show();
								Timber.d(
										"data removed for Installment ");
								transactionsRepo.saveItem(transaction);


							}, throwable -> {
								Toasty.error(getBaseActivity(),
										"Details Not Updated, Try" +
												" " +
												"again" +
												" Later")
										.show();
								Timber.e("data  not updated for " + expense
								);
							}
					);
		} else {
			currentInstallment.setDelayReason("Less amount");
			currentInstallment.setExpectedAmt(
					currentInstallment.getExpectedAmt().subtract(expense.getExpectedAmt()));
			installmentRepo.updateItem(currentInstallment)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(() -> {
								Toasty.success(
										Objects.requireNonNull(
												RegisterReceivedDialogFragment.this
														.getContext()),
										"Installment has been paid" +
												" Successfully")
										.show();
								Timber.d(
										"data updated for Installment ");
								transactionsRepo.saveItem(transaction);


							}, throwable -> {
								Toasty.error(getBaseActivity(),
										"Details Not Updated, Try" +
												" " +
												"again" +
												" Later")
										.show();
								Timber.e("data  not updated for " + expense
								);
							}
					);
		}
	}

	private void updateInstallmentViaEventBus(final BigDecimal expectedAmt) {

		Timber.d(
				"ABCD Updating all installments, Amount to be deducted" + expectedAmt
						.toPlainString());
		Events.newAmt makeACall = new Events.newAmt(expectedAmt);

		GlobalBus.getBus().post(makeACall);
	}

	private void updateInstallments(final BigDecimal expectedAmt) {
		installmentRepo.getLocalInstallmentsLab()
				.getRxItemsForLoan(loan.getAccountNo())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(installments -> {
							Timber.d("Updating all installments amounts " + installments.size());
							double sum = com.annimon.stream.Stream.of(installments)
									.withoutNulls()
									.mapToDouble(installment ->
											installment.getExpectedAmt().doubleValue())
									.sum();
							final BigDecimal newTotalRemainingAmt =
									BigDecimal.valueOf(sum).subtract(expectedAmt);
							final BigDecimal newInstallmentAmount;
							if (installments.size() != 0) {
								newInstallmentAmount =
										newTotalRemainingAmt.divide(BigDecimal.valueOf(installments
														.size()), 2,
												RoundingMode.HALF_EVEN);
							} else { newInstallmentAmount = newTotalRemainingAmt; }
							for (Installment installmentFresh : installments) {
								installmentFresh.setExpectedAmt(newInstallmentAmount);
								installmentRepo.saveItem(installmentFresh);
							}


						},
						throwable -> Timber.e(
								"Updating all installments amount failed" + throwable.getMessage
										()));
	}


}

