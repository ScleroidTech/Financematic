package com.scleroid.financematic.utils.ui;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.widget.TextView;

import javax.inject.Inject;

import timber.log.Timber;

public class TextViewUtils {
	@Inject
	public TextViewUtils() {
	}

	public void textViewExperiments(@NonNull TextView textView) {
		Shader textShaderOther = new LinearGradient(0, 0, 300, textView.getLineHeight(),
				new int[]{0xFF852D91, 0xFF6253E1, 0xFF00E0E4},
				null, Shader.TileMode.CLAMP);
		Timber.i(
				"textview Values" + textView.getTextScaleX() + " " + textView.getTextSize() + " "
						+ textView
						.getMeasuredWidth() + " " + textView.getLineHeight());
		Shader textShader = new LinearGradient(0, 0, 150, textView.getLineHeight(),
				new int[]{Color.GRAY, Color.BLUE},
				null, Shader.TileMode.CLAMP);
		textView.getPaint().setShader(textShaderOther);
	}

	public void textViewExperimentsNext(@NonNull TextView textView) {
		Shader textShaderOther = new LinearGradient(0, 0, 300, textView.getLineHeight(),
				new int[]{0xFF852D91, 0xFF6253E1, 0xFF00E0E4},
				null, Shader.TileMode.CLAMP);
		Timber.i(
				"textview Values" + textView.getTextScaleX() + " " + textView.getTextSize() + " "
						+ textView
						.getMeasuredWidth() + " " + textView.getLineHeight());
		Shader textShader = new LinearGradient(0, 0, 150, textView.getLineHeight(),
				new int[]{Color.GRAY, Color.BLUE},
				null, Shader.TileMode.CLAMP);
		textView.getPaint().setShader(textShader);
	}

	public void textViewExperimentsRed(@NonNull TextView textView) {
		Shader textShaderOther = new LinearGradient(0, 0, 300, textView.getLineHeight(),
				new int[]{0xFFFF7513, 0xFFDB4437, 0xFFB72B21},

				null, Shader.TileMode.CLAMP);
		Timber.i(
				"textview Values" + textView.getTextScaleX() + " " + textView.getTextSize() + " "
						+ textView
						.getMeasuredWidth() + " " + textView.getLineHeight());
		Shader textShader = new LinearGradient(0, 0, 150, textView.getLineHeight(),
				new int[]{Color.GRAY, Color.BLUE},
				null, Shader.TileMode.CLAMP);
		textView.getPaint().setShader(textShader);
	}
}