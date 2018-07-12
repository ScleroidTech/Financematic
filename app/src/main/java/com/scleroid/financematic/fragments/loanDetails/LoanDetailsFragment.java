package com.scleroid.financematic.fragments.loanDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.base.BaseFragment;
import com.scleroid.financematic.base.BaseViewModel;
import com.scleroid.financematic.data.local.model.Installment;
import com.scleroid.financematic.data.local.model.Loan;
import com.scleroid.financematic.data.local.model.TransactionModel;
import com.scleroid.financematic.utils.eventBus.Events;
import com.scleroid.financematic.utils.eventBus.GlobalBus;
import com.scleroid.financematic.utils.ui.ActivityUtils;
import com.scleroid.financematic.utils.ui.DateUtils;
import com.scleroid.financematic.utils.ui.RupeeTextView;
import com.scleroid.financematic.utils.ui.TextViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * Created by scleroid on 2/3/18.
 */

public class LoanDetailsFragment extends BaseFragment {


	private static final String ACCOUNT_NO = "account_no";
	@Inject
	TextViewUtils textViewUtils;

	@Nullable
	@BindView(R.id.total_amount_text_view)
	RupeeTextView totalAmountTextView;
	@Nullable
	@BindView(R.id.duration_text_view)
	TextView durationTextView;


	@Nullable
	@BindView(R.id.pesonal_summery_details_recycler)
	RecyclerView pesonalSummeryDetailsRecycler;

	@Nullable
	@BindView(R.id.interest_text_view)
	RupeeTextView interestTextView;

	@Nullable
	@BindView(R.id.card_loan)
	View cardLoan;

	@Nullable
	@BindView(R.id.empty_card)
	CardView emptyCard;
	@Inject
	DateUtils dateUtils;
	EventBus eventBus = GlobalBus.getBus();
	@Nullable
	private List<TransactionModel> transactionList = new ArrayList<>();
	private RecyclerView recyclerView;
	@Nullable
	private LoanAdapter mAdapter;
	@NonNull
	private ActivityUtils activityUtils = new ActivityUtils();
	private int accountNo;
	private LoanDetailsViewModel loanViewModel;
	@Nullable
	private Loan theLoan;
	private CardHolder cardHolder;
	@Nullable
	private List<Installment> installmentList = new ArrayList<>();

	public LoanDetailsFragment() {
		// Required empty public constructor
	}

	@NonNull
	public static LoanDetailsFragment newInstance(int accountNo) {
		LoanDetailsFragment fragment = new LoanDetailsFragment();
		Bundle args = new Bundle();
		args.putInt(ACCOUNT_NO, accountNo);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		eventBus.register(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View rootView = getRootView();
		cardHolder = new CardHolder();
		ButterKnife.bind(cardHolder, cardLoan);
		/*        View rootView =  inflater.inflate(R.layout.personal_loan_aacount_details,
		container, false);*/

		Bundle bundle = getArguments();
		if (bundle != null) accountNo = bundle.getInt(ACCOUNT_NO);
		loanViewModel.setCurrentAccountNo(accountNo);
		recyclerView = rootView.findViewById(R.id.pesonal_summery_details_recycler);
		mAdapter = new LoanAdapter(transactionList, installmentList);
		recyclerView.setHasFixedSize(true);


		// vertical RecyclerView
		// keep movie_list_row.xml width to `match_parent`
		RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity().getApplicationContext());

		// horizontal RecyclerView
		// keep movie_list_row.xml width to `wrap_content`
		// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
		// (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

		recyclerView.setLayoutManager(mLayoutManager);


		recyclerView.setItemAnimator(new DefaultItemAnimator());

		recyclerView.setAdapter(mAdapter);

		// row click listener


		//  textViewUtils.textViewExperiments(totalAmountTextView);

		return rootView;


	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.fragment_loan_details;
	}

	@Override
	public void onDetach() {
		eventBus.unregister(this);
		super.onDetach();
	}

	/**
	 * Override so you can observe your viewModel
	 */
	@Override
	protected void subscribeToLiveData() {
		loanViewModel.getTransactionList().observe(this, items -> {
			if (items != null) {
				updateView(installmentList, items.data);
			}

			//updateTotalLoanAmt();

		});

		loanViewModel.getInstallmentList().observe(this, items -> {
			if (items != null) {
				updateView(items.data, transactionList);
			}

			//updateTotalLoanAmt();

		});

		loanViewModel.getLoanLiveData().observe(this, item -> {
			theLoan = item.data;
			updateUi();
		});
	}

	private void updateView(@Nullable final List<Installment> items,
	                        @Nullable List<TransactionModel>
			                        transactionList) {

		if ((items == null || transactionList == null)) {
			emptyCard.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else if ((items.isEmpty() && transactionList.isEmpty())) {
			emptyCard.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else {
			emptyCard.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			sort(items);
			sortReverse(transactionList);
			mAdapter.setInstallmentList(items);
			mAdapter.setTransactionList(transactionList);
			installmentList = items;
			this.transactionList = transactionList;
		}

	}

	private void sort(@NonNull final List<Installment> transactions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			transactions.sort(Comparator.comparing(Installment::getInstallmentDate));
		} else {
			Collections.sort(transactions,
					(m1, m2) -> m1.getInstallmentDate().compareTo(m2.getInstallmentDate()));
		}
	}

	private void sortReverse(@NonNull final List<TransactionModel> transactions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			transactions.sort(
					Comparator.comparing(TransactionModel::getTransactionDate).reversed());
		} else {
			Collections.sort(transactions,
					(m1, m2) -> m2.getTransactionDate().compareTo(m1.getTransactionDate()));
		}
	}

	private void updateUi() {
		if (theLoan == null) return;
		totalAmountTextView.setText(theLoan.getLoanAmt().toPlainString());
		interestTextView.setText(String.valueOf(theLoan.getInterestAmt().toPlainString()));
		final long duration =
				dateUtils.differenceOfDates(theLoan.getStartDate(), theLoan.getEndDate());
		long months = TimeUnit.MILLISECONDS.toDays(duration) / 30;
		durationTextView.setText(String.format("%d Months",
				months));
		cardHolder.paidAmountTextView.setText(theLoan.getReceivedAmt().toPlainString());
		cardHolder.installmentTextView.setText(theLoan.getInstallmentAmt().toPlainString());
		setTitle();
		//	activityUtils.useUpButton((MainActivity) getActivity(),true);

		setHasOptionsMenu(true);
	}

	private void setTitle() {
		activityUtils.setTitle((AppCompatActivity) getActivity(),
				"A/c No." + theLoan.getAccountNo());
	}

	/**
	 * Override for set view model
	 *
	 * @return view model instance
	 */
	@Override
	public BaseViewModel getViewModel() {
		loanViewModel =
				ViewModelProviders.of(this, viewModelFactory).get(LoanDetailsViewModel.class);

		return loanViewModel;
	}

	@Subscribe
	public void onUpdatingInstallments(@NonNull Events.newAmt loanBundle) {

		BigDecimal amount = loanBundle.getNumber();
		Timber.d("ABCD I'm in the eventBus with amount" + amount.toPlainString());
		if (installmentList != null) {
			double sum = com.annimon.stream.Stream.of(installmentList)
					.withoutNulls()
					.mapToDouble(installment ->
							installment.getExpectedAmt().doubleValue())
					.sum();
			final BigDecimal newTotalRemainingAmt =
					BigDecimal.valueOf(sum).subtract(amount);
			Timber.d(
					"ABCD And Now the new amount should be  " + newTotalRemainingAmt.toPlainString
							() +
							" which is " + sum + "- " + amount
							.toPlainString());
			final BigDecimal newInstallmentAmount;
			if (installmentList.size() != 0) {
				newInstallmentAmount =
						newTotalRemainingAmt.divide(BigDecimal.valueOf(installmentList
										.size()), 2,
								RoundingMode.HALF_EVEN);
				Timber.d(
						"ABCD And This is new Installment Amount  " + newInstallmentAmount
								.toPlainString() + " which is divided by " + installmentList
								.size());

			} else {
				newInstallmentAmount = amount;
			}
			List<Installment> newList = new ArrayList<>();
			for (Installment installment : installmentList
					) {
				installment.setExpectedAmt(newInstallmentAmount);
				newList.add(installment);

			}
			loanViewModel.saveInstallmentsList(newList);
		}


	}

	static class CardHolder {

		@Nullable
		@BindView(R.id.paid_amount_text_view)
		RupeeTextView paidAmountTextView;
		@Nullable
		@BindView(R.id.installment_text_view)
		RupeeTextView installmentTextView;
	}

}
