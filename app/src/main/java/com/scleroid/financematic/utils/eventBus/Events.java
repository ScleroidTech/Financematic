package com.scleroid.financematic.utils.eventBus;

import android.os.Bundle;

/**
 * The class used to provide events that need to be handled by
 *
 * @author Ganesh
 * @see org.greenrobot.eventbus.EventBus The inner classes are static & needs it's methods to be
 * implemented with subscribe annotations
 * @since 22-11-2017
 */

public class Events {


	// Event used to send message from activity to fragment.
	public static class openCustomerFragment {
		private int customerId;

		public openCustomerFragment(final int customerId) {this.customerId = customerId;}

		public int getCustomerId() {
			return customerId;
		}
	}

	// Event used to send message from activity to fragment.
	public static class openReceiveMoneyFragment {
		private int accountNo, installmentId;

		public openReceiveMoneyFragment(final int accountNo, final int installmentId) {
			this.accountNo = accountNo;
			this.installmentId = installmentId;
		}

		public int getAccountNo() {

			return accountNo;
		}

		public int getInstallmentId() {
			return installmentId;
		}
	}


	public static class openLoanDetailsFragment {
		private int accountNo;

		public openLoanDetailsFragment(final int accountNo) {
			this.accountNo = accountNo;
		}

		public int getAccountNo() {
			return accountNo;
		}
	}

	public static class openDelayFragment {
		private int installmentId;
		private int loanAccountNo;

		public openDelayFragment(final int customerId, final int loanAccountNo) {
			this.installmentId = customerId;
			this.loanAccountNo = loanAccountNo;
		}

		public int getInstallmentId() {
			return installmentId;
		}

		public int getLoanAccountNo() {
			return loanAccountNo;
		}
	}
	/**
	 *
	 */
	public static class ActivityActivityMessage {
		private String message;

		public ActivityActivityMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	// Event used to send message from activity to activity.
	public static class AddressMessage {
		private Bundle message;

		public AddressMessage(Bundle bundle) {
			this.message = bundle;
		}

		public Bundle getMessage() {
			return message;
		}
	}

	// Event used to send message from activity to activity.
	public static class DateMessage {
		private Bundle message;

		public DateMessage(Bundle bundle) {
			this.message = bundle;
		}

		public Bundle getMessage() {
			return message;
		}
	}


	// Event used to place call using MainActivity
	public static class placeCall {
		private String message;

		public placeCall(final String phone) {
			this.message = phone;
		}


		public String getNumber() {
			return message;
		}
	}

	// Event used to show toast
	public static class showToast {
		private String message;
		private String type;

		public showToast(final String phone, String type) {
			this.message = phone;
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public String getMessage() {
			return message;
		}

	}

}
