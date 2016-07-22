package com.example.colze.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.colze.R;
import com.example.colze.utils.pay.MyPayUtil;
import com.example.colze.utils.pay.PayListener;
import com.example.colze.utils.zip.ZipUtil;
import com.example.colze.utils.zip.ZipUtil.ZipListener;

public class TestActivity extends Activity {
	private Button mButtonZip;
	private Button mButtonPay;
	private TextView mTextView;
	private ZipUtil zu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		mButtonZip = (Button) findViewById(R.id.button_zip);
		mButtonPay = (Button) findViewById(R.id.button_pay);
		mTextView = (TextView) findViewById(R.id.textview);
		mButtonPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MyPayUtil pay = new MyPayUtil(TestActivity.this,
						new PayListener() {

							@Override
							public void onPaying() {

							}

							@Override
							public void onPaySuccess() {

							}

							@Override
							public void onPayErr() {

							}
						}, "购买题目", "25元购买题目", "0.01");
				pay.start();
			}
		});
		mButtonZip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				zu = new ZipUtil(
						TestActivity.this,
						new ZipListener() {
							@Override
							public void onZipStartDownLoad() {
								mTextView.setText("开始下载");
								Log.e("0000", "开始下载");
							}

							@Override
							public void onZipDownLoading(int position) {
								mTextView.setText("进度：" + position);
								Log.e("0000", "进度：" + position);
							}

							@Override
							public void onZipEndDownLoad() {
								mTextView.setText("结束下载");
								Log.e("0000", "结束下载");
							}

							@Override
							public void onZipDownLoadErr() {
								mTextView.setText("下载错误");
								Log.e("0000", "下载错误");
							}

							@Override
							public void onZipStartDecompression() {
								mTextView.setText("开始解压");
								Log.e("0000", "开始解压");
							}

							@Override
							public void onZipDecompressioning(int position) {
								mTextView.setText("进度：" + position);
								Log.e("0000", "进度：" + position);
							}

							@Override
							public void onZipEndDecompression() {
								mTextView.setText("结束解压");
								Log.e("0000", "结束解压");
							}

							@Override
							public void onZipDecompressionErr() {
								mTextView.setText("解压错误");
								Log.e("0000", "解压错误");
							}

							@Override
							public void noUpdate() {
								// TODO Auto-generated method stub
								
							}
						},
						"http://app.gaofy.com/upload/subject/androidPriReading/ExamResource.zip",
						true);
				zu.start();
			}
		});
	}
}
