package com.example.colze.view;

import com.example.colze.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class MySeekBar extends SeekBar {
	Context mContext = null;

	public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public MySeekBar(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void setThumb(Drawable thumb) {

		super.setThumb(thumb);
	}

	public void setThumb(int number) {
		setThumb(addNumber(number));
		setProgress(number);
		if(number < 3){
			setProgress(3);
		}
	}

	@SuppressWarnings("deprecation")
	public Drawable addNumber(int number) {
		return new BitmapDrawable(drawableToBitamp(number));
	}

	private Bitmap drawableToBitamp(int number) {
		LayoutInflater inflator = LayoutInflater.from(mContext);
		View viewHelp = inflator.inflate(R.layout.bg_thumb, null);
		TextView tv_num = (TextView) viewHelp.findViewById(R.id.tv_num);
		tv_num.setText(number + "");
		return convertViewToBitmap(viewHelp);
	}

	public static Bitmap convertViewToBitmap(View view) {
		if (view == null) {
			return null;
		}
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// return super.onTouchEvent(event);
		return false;
	}
}
