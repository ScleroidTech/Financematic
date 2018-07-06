package com.scleroid.financematic.registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.scleroid.financematic.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotActivity extends BaseActivity {
	@BindView(R.id.email_edit_text)
	EditText mEmailField;
	@BindView(R.id.forgot_button)
	Button forgotButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * @return layout resource id
	 */
	@Override
	public int getLayoutId() {
		return R.layout.activity_forgotpassword;
	}

	@OnClick(R.id.forgot_button)
	public void onViewClicked() {
		String email = mEmailField.getText().toString();
		if (!validateForm(email)) return;
		FirebaseAuth auth = FirebaseAuth.getInstance();

		auth.sendPasswordResetEmail(email)
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						Toast.makeText(ForgotActivity.this, "Check email to reset your password!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ForgotActivity.this, "Fail to send reset password email!",
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	private boolean validateForm(final String email) {
		boolean valid = true;


		if (TextUtils.isEmpty(email)) {
			mEmailField.setError("Required.");
			valid = false;
		} else {
			mEmailField.setError(null);
		}

		return valid;
	}

}
