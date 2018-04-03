package com.scleroid.financematic.utils;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 4/3/18
 */

import android.content.Context;
import android.util.AttributeSet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by john on 7/7/16.
 */
public class RupeeTextView extends android.support.v7.widget.AppCompatTextView {

    Context context;

    public RupeeTextView(Context context) {
        super(context);
        this.context = context;
    }

    public RupeeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public RupeeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        String formattedString = null;
        try {
            // The comma in the format specifier does the trick
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,##,##,##,###");
            formattedString = "₹" + formatter.format(Double.parseDouble(text.toString()));  // adds rupee symbol and thousand seperater
        } catch (NumberFormatException e) {
            Timber.e("Rupee TextView NumberFormatException");
            e.printStackTrace();
        }
        super.setText(formattedString, type);
    }
}