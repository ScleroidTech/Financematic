package com.scleroid.financematic.fragments.expense;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scleroid.financematic.R;
import com.scleroid.financematic.data.local.model.Expense;
import com.scleroid.financematic.data.local.model.ExpenseCategory;
import com.scleroid.financematic.utils.ui.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by scleroid on 27/3/18.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
	List<Expense> expenses;

	Context context;

	public ExpenseAdapter(List<Expense> expenses, Context context) {
		this.expenses = expenses;
		this.context = context;
	}


	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_fragment_expense, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

		holder.setData(context, expenses.get(position));

	}


	@Override
	public int getItemCount() {
		//TODO change according to type
		return expenses.size();
	}


	static class ViewHolder extends RecyclerView.ViewHolder {

		static DateUtils dateUtils = new DateUtils();
		@BindView(R.id.month_text_view)
		TextView monthTextView;
		@BindView(R.id.only_date_text_view)
		TextView onlyDateTextView;
		@BindView(R.id.day_text_view)
		TextView dayTextView;

		@BindView(R.id.expense_image)
		ImageView expenseImage;
		@BindView(R.id.expense_type_text_view)
		TextView expenseTypeTextView;
		@BindView(R.id.expense_amount_text_view)
		TextView expenseAmount;

		ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void setData(Context context, Expense expense) {
			BigDecimal expenseAmount = expense.getExpenseAmount();
			if (expenseAmount == null) return;
			this.expenseAmount.setText(expenseAmount.toString());
			setDate(expense.getExpenseDate());

			String expenseType = expense.getExpenseType();
			setExpenseType(expenseType, context);


		}

		private void setExpenseType(String expenseType, Context context) {
			String expenseTypeText;
			int expenseTypeImageSrc;
			int expenseColor;

			switch (expenseType) {
				case ExpenseCategory.ROOM_RENT:
					expenseTypeText = "Room Rent";
					expenseTypeImageSrc = R.drawable.ic_home_black_24dp;
					expenseColor = context.getResources().getColor(R.color.exp_card1);

					break;
				case ExpenseCategory.LIGHT_BILL:
					expenseTypeText = "Light Bill";
					expenseTypeImageSrc = R.drawable.ic_lightbulb_outline_black_24dp;
					expenseColor = context.getResources().getColor(R.color.exp_card2);

					break;
				case ExpenseCategory.PHONE_BILL:
					expenseTypeText = "Mobile Bill";
					expenseTypeImageSrc = R.drawable.ic_phone_android_black_24dp;
					expenseColor = context.getResources().getColor(R.color.exp_card3);
					break;
				case ExpenseCategory.PAID_SALARIES:
					expenseTypeText = "Paid Salaries";
					expenseTypeImageSrc = R.drawable.ic_account_balance_wallet_black_24dp;
					expenseColor = context.getResources().getColor(R.color.exp_card4);
					break;
				case ExpenseCategory.FUEL:
					expenseTypeText = "Fuel";
					expenseTypeImageSrc = R.drawable.ic_gaspump;
					expenseColor = context.getResources().getColor(R.color.exp_card5);
					break;
				case ExpenseCategory.OTHER:
				default:
					expenseTypeText = "OTHER";
					expenseTypeImageSrc = R.drawable.ic_view_list_black_24dp;
					expenseColor = context.getResources().getColor(R.color.exp_card6);
					break;

			}
			expenseTypeTextView.setText(expenseTypeText);
			expenseImage.setImageResource(expenseTypeImageSrc);
			expenseImage.setColorFilter(expenseColor);
		}

		private void setDate(Date date) {
			CharSequence month = dateUtils.getFormattedDate(date, "MMM. yyyy");
			CharSequence exactDate = dateUtils.getFormattedDate(date, "dd");
			CharSequence day = dateUtils.getFormattedDate(date, "EEE");
			monthTextView.setText(month);
			onlyDateTextView.setText(exactDate);
			dayTextView.setText(day);
			//    yearTextView.setText(year);
		}


	}


}
