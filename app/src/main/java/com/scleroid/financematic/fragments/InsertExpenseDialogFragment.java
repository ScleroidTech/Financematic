package com.scleroid.financematic.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseDialog;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.ExpenseCategory;
import com.scleroid.financematic.data.repo.ExpenseRepo;

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
 * Created by scleroid on 10/4/18.
 */

/**
 * Created by scleroid on 9/3/18.
 */

public class InsertExpenseDialogFragment extends BaseDialog {


	@Inject
	ExpenseRepo expenseRepo;
	String interesting;
	Spinner spin;
	Context context;
	EditText edittext;
	Calendar myCalendar = Calendar.getInstance();
	String[] country =
			{ExpenseCategory.ROOM_RENT, ExpenseCategory.LIGHT_BILL, ExpenseCategory.PHONE_BILL,
					ExpenseCategory.PAID_SALARIES, ExpenseCategory.FUEL, ExpenseCategory.OTHER};
	private Button b;
	private TextView etrxDate, etrxTotalInterestAmount, etrxReceivedAmount, tv;
	private String expenseType;
	private Date expenseDate;


	public InsertExpenseDialogFragment() {
		// Required empty public constructor
	}

	public static InsertExpenseDialogFragment newInstance(String param1, String param2) {
		InsertExpenseDialogFragment fragment = new InsertExpenseDialogFragment();
		Bundle args = new Bundle();
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
		View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.insert_expense, null);

		etrxDate = rootView.findViewById(R.id.exp_date);
		etrxReceivedAmount = rootView.findViewById(R.id.amount_edit_text);



		final Spinner spin = rootView.findViewById(R.id.spinnerexp);
		ArrayAdapter aa = new ArrayAdapter(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item, country);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		spin.setAdapter(aa);

		spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view,
			                           final int position, final long id) {
				expenseType = country[position];

			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});


		etrxReceivedAmount.addTextChangedListener(
				new com.scleroid.financematic.utils.ui.TextValidator(etrxReceivedAmount) {
			@Override
			public void validate(TextView textView, String text) {

				final String email = etrxReceivedAmount.getText().toString();
				if (!isValidEmail(email)) {
					etrxReceivedAmount.setError("Enter Valid Amount");
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
					Timber.d(
							"Your Input: \n" + etrxDate.getText()
									.toString() + "\n" + "\n" + etrxReceivedAmount.getText()
									.toString() + "\nEnd.");
					Expense expense =
							new Expense(new BigDecimal(etrxReceivedAmount.getText().toString()),
									expenseType, expenseDate);
					expenseRepo.saveItem(expense).observeOn(
							AndroidSchedulers.mainThread()).subscribe(installment1 -> {
								Toasty.success(getContext(), "Details Updated Successfully")
										.show();
								Timber.d("data updated for Installment " + installment1.toString
										());
							}, throwable -> {
								Toasty.error(getContext(), "Details Not Updated, Try again Later")
										.show();
								Timber.e("data  not updated for " + expense.toString());
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

		expenseDate = myCalendar.getTime();
		etrxDate.setText(sdf.format(expenseDate));
	}



}

