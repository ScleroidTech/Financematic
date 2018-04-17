package com.scleroid.financematic.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import timber.log.Timber;

/**
 * Created by scleroid on 12/4/18.
 */


public class DelayDialogFragment extends BaseDialog {


	private static final String DIALOG_DATE = "DIALOG_DATE";
	private static final int REQUEST_DATE = 0;
	private static final String INSTALLMENT_ID = "installment_id";
	private static final String LOAN_AC_NO = "ac_no";

	@Inject
	InstallmentRepo installmentRepo;
	String interesting;
	Spinner spin;
	Context context;
	EditText edittext;
	Calendar myCalendar = Calendar.getInstance();
	String[] country = {"Not now money", "", "OTHER"};
	private Button b;
	private TextView etrxDate, reasonEditText, etrxReceivedAmount, tv;
	private Date delayedDate;


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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		super.onCreateDialog(savedInstanceState);
		View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delay, null);
		int installationId = getArguments().getInt(INSTALLMENT_ID);
		int accountNo = getArguments().getInt(LOAN_AC_NO);


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
		reasonEditText.addTextChangedListener(new TextValidator(reasonEditText) {
			@Override
			public void validate(TextView textView, String text) {

				final String email = reasonEditText.getText().toString();
				if (!isValidEmail(email)) {
					reasonEditText.setError("Enter Valid reason");
				}


			}
		});

		final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		};

		etrxDate.setOnClickListener(v -> {

			new DatePickerDialog(getContext(), date, myCalendar
					.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH)).show();
          /*  FragmentManager fragmentManager = getActivity().getFragmentManager();
            DialogFragment dialogFragment = new Fragment_datepicker_all();
          *//*  dialogFragment.setTargetFragment(fragmentManager.findFragmentByTag
			 * (CURRENT_TAG), REQUEST_DATE);*//*
            dialogFragment.show(fragmentManager, DIALOG_DATE);*/
		});


		return new MaterialStyledDialog.Builder(getActivity()).setTitle(R.string.delay_view)
				.setCustomView(rootView)
				.setStyle(Style.HEADER_WITH_ICON)
				.withIconAnimation(true)
				.setIcon(R.drawable.ic_stopwatch)
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
											.toString() + "\n" + etrxReceivedAmount.getText()
									.toString() + "\nEnd.");
					Installment installment = new Installment(installationId, delayedDate,
							new BigDecimal(etrxReceivedAmount.getText().toString()), accountNo);
					installmentRepo.updateItem(installment).observeOn(
							AndroidSchedulers.mainThread()).subscribe(installment1 -> {
								Toasty.success(getContext(), "Details Updated Successfully")
										.show();
								Timber.d("data updated for Installment " + installment1.toString
										());
							}, throwable -> {
								Toasty.error(getContext(), "Details Not Updated, Try again Later")
										.show();
								Timber.e("data  not updated for " + installment.toString());
							}
					);


				})
				.show();

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
*/


}
