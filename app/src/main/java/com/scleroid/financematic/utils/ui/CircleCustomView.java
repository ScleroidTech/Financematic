package com.scleroid.financematic.utils.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Copyright (C) 2018
 *
 * @author Ganesh Kaple
 * @since 3/22/18
 */

public class CircleCustomView extends View {

	private Paint mCircleYellow;
	private Paint mCircleGray;

	private float mRadius;
	@NonNull
	private RectF mArcBounds = new RectF();
	//a big mistake, capital F was used, making it an object
	private float drawUpto = 0;

	public CircleCustomView(Context context) {
		super(context);

		// create the Paint and set its color

	}

	public CircleCustomView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		initPaints();
	}

	private void initPaints() {
		mCircleYellow = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCircleYellow.setStyle(Paint.Style.FILL);
		// mCircleYellow.setColor(Color.GREEN);
		mCircleYellow.setStyle(Paint.Style.STROKE);
		mCircleYellow.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
		mCircleYellow.setStrokeCap(Paint.Cap.SQUARE);
		// mEyeAndMouthPaint.setColor(getResources().getColor(R.color.colorAccent));
		mCircleYellow.setColor(Color.parseColor("#4693c5"));
		/*	mCircleYellow.setColor(Color.parseColor("#fbab00"));4693c5 yellow*/
		mCircleGray = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCircleGray.setStyle(Paint.Style.FILL);
		mCircleGray.setColor(Color.GRAY);
		mCircleGray.setStyle(Paint.Style.STROKE);
		mCircleGray.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
		mCircleGray.setStrokeCap(Paint.Cap.SQUARE);
		// mEyeAndMouthPaint.setColor(getResources().getColor(R.color.colorAccent));
		mCircleGray.setColor(Color.parseColor("#d8d8d8"));
		/*mCircleGray.setColor(Color.parseColor("#76787a"));*/

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mRadius = Math.min(w, h) / 2f;

	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		super.onDraw(canvas);
		if (drawUpto == 0) {
			drawUpto = 50f;
		}
		//drawUpto = 46f;


		float mouthInset = mRadius / 3f;
		mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset);
		canvas.drawArc(mArcBounds, 0f, 360f, false, mCircleGray);

		canvas.drawArc(mArcBounds, 270f, drawUpto, false, mCircleYellow);


	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);

		int size = Math.min(w, h);
		setMeasuredDimension(size, size);
	}

	public void setAngle(float angle) {
		drawUpto = angle;

	}
}
