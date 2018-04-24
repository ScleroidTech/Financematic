package com.scleroid.financematic.fragments.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseDialog;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.repo.InstallmentRepo;
import com.scleroid.financematic.utils.ui.TextValidator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by scleroid on 12/4/18.
 */


public class DelayDialogFragment extends BaseDialog {
	private static final String INSTALLMENT_ID = "installment_id";



	private static final String LOAN_AC_NO = "ac_no";
	@Inject
	InstallmentRepo installmentRepo;
	Calendar myCalendar = Calendar.getInstance();
	private String installmentAmount;
	private TextView etrxDate;
	private TextView reasonEditText;
	private TextView etrxReceivedAmount;
	private Date delayedDate;
	private Installment installment;
	private Date installmentDate;
	public DelayDialogFragment() {
		// Required empty public constructor
	}

	public static DelayDialogFragment newInstance(int installmentId, int acNo) {
		DelayDialogFragment fragment = new DelayDialogFragment();
		Bundle args = new Bundle();
		args.putInt(INSTALLMENT_ID, installmentId);

		args.putInt(LOAN_AC_NO, acNo);
		fragment.setArguments(args);
		return fragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		super.onCreateDialog(savedInstanceState);
		View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delay, null);
		int installationId = getArguments().getInt(INSTALLMENT_ID);
		int accountNo = getArguments().getInt(LOAN_AC_NO);
		installmentRepo.getLocalInstallmentsLab()
				.getRxItem(installationId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(customer -> {
							installment = customer;
							installmentDate = installment.getInstallmentDate();
							installmentAmount = installment.getExpectedAmt().toPlainString();
							//TODO set TextView here, or edittext hint


						},
						throwable -> Timber.d("Not gonna show up " + throwable.getMessage()));

		etrxDate = rootView.findViewById(R.id.exp_date);
		etrxReceivedAmount = rootView.findViewById(R.id.amount_edit_text);
		reasonEditText = rootView.findViewById(R.id.reason_edit_text);


		etrxReceivedAmount.addTextChangedListener(new TextValidator(etrxReceivedAmount) {
			@Override
			public void validate(TextView textView, String text) {

				final String email = etrxReceivedAmount.getText().toString();
				if (!isValidEmail(email)) {
					etrxReceivedAmount.setError("Enter Valid Amount");
				}


			}
		});
/*		reasonEditText.addTextChangedListener(new TextValidator(reasonEditText) {
			@Override
			public void validate(TextView textView, String text) {

				final String email = reasonEditText.getText().toString();
				if (!isValidEmail(email)) {
					reasonEditText.setError("Enter Valid reason");
				}


			}
		});*/

		final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		};

		etrxDate.setOnClickListener(v -> {


			DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar
					.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.getDatePicker().setMinDate(installmentDate.getDate());
			datePickerDialog.show();
          /*  FragmentManager fragmentManager = getActivity().getFragmentManager();
            DialogFragment dialogFragment = new Fragment_datepicker_all();
          *//*  dialogFragment.setTargetFragment(fragmentManager.findFragmentByTag
			 * (CURRENT_TAG), REQUEST_DATE);*//*
            dialogFragment.show(fragmentManager, DIALOG_DATE);*/
		});


		return
				new MaterialStyledDialog.Builder(getActivity()).setTitle(R.string.delay_view)
						.setCustomView(rootView)
						.setStyle(Style.HEADER_WITH_ICON)
						.withIconAnimation(true).setCancelable(true)
						.setIcon(R.drawable.ic_stopwatch)
						.setCancelable(true)
						.setPositiveText(R.string.submit)
						.onPositive((dialog, which) -> {
							if (etrxReceivedAmount.getText() == null || delayedDate == null ||
									reasonEditText
											.getText() == null) {
								Toasty.error(getContext(), "You haven't filled all data").show();
								return;
							}
							Timber.d(
									"Your Input: \n" + etrxDate.getText().toString() + "\n" +
											reasonEditText
													.getText()
													.toString() + "\n" + etrxReceivedAmount
											.getText()
											.toString() + "\nEnd.");
							Installment installment = new Installment(installationId, delayedDate,
									new BigDecimal(etrxReceivedAmount.getText().toString()),
									accountNo);
							installmentRepo.updateItem(installment).observeOn(
									AndroidSchedulers.mainThread()).subscribe(installment1 -> {
										Toasty.success(getContext(), "Details Updated " +
												"Successfully")
												.show();
										Timber.d("data updated for Installment " +
												installment1.toString
														());
										this.dismiss();
									}, throwable -> {
										Toasty.error(getContext(), "Details Not Updated, Try " +
												"again" +
												" Later")
												.show();
										Timber.e("data  not updated for " + installment.toString
												());
									}
							);

						})
						.show();


	}

	/**
	 * Dismiss the fragment and its dialog.  If the fragment was added to the back stack, all back
	 * stack state up to and including this entry will be popped.  Otherwise, a new transaction
	 * will
	 * be committed to remove the fragment.
	 */
	@Override
	public void dismiss() {
		super.dismissAllowingStateLoss();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[0-9]*$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}


	private void updateLabel() {
		String myFormat = "MM/dd/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		delayedDate = myCalendar.getTime();
		etrxDate.setText(sdf.format(delayedDate));
	}
/*

	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
	}

*/



}
