package com.example.colze.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.colze.R;
import com.example.colze.R;

public class ProgressDialog extends Dialog {
	private Context mContext;
	private TextView mTextView_Title;
	private TextView mTextView_Progress;
	public ProgressDialog(Context context) {
		super(context, R.style.dialog);
		this.mContext = context;
		getWindow().setWindowAnimations(R.style.PopupAnimation);
		setContentView(R.layout.dialog_progress);
		initView();
	}
	private void initView()
	{
		mTextView_Title = (TextView) findViewById(R.id.title);
		mTextView_Progress = (TextView) findViewById(R.id.progress);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (screenHeigh * 0.5);
		p.width = (int) (screenWidth / 2.4);
		dialogWindow.setAttributes(p);
	}
	
	public  void setTitle(String title)
	{
		mTextView_Title.setText(title);
	}
	
	public  void setContext(String progress)
	{
		mTextView_Progress.setText(progress);
	}
}
