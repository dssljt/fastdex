package com.example.colze;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.colze.fragment.AlreadyBuyFragment;
import com.example.colze.fragment.CorrectionFragment;
import com.example.colze.fragment.ReadFragment;
import com.example.colze.fragment.SettingFragment;
import com.example.colze.fragment.UnBuyFragment;
import com.example.colze.utils.ObjectCacheToFile;

public class HomeActivity extends FragmentActivity {
	public Double[] localArr = { 0.0000, 0.0000, 0.0000, 0.0000, 0.0000 };
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				mAlreadyBuyFragment.updateView((Double[])msg.obj,msg.what);
				break;
			case 1:
				mAlreadyBuyFragment.updateView(localArr,msg.what);
				break;
			default:
				break;
			}
		}
	};

	private static final String HASBUY = "hasBuy";

	private int hasBuy = 0;// 1代表已购买，进入购买版本后的主页；0代表未购买

	private AlreadyBuyFragment mAlreadyBuyFragment = new AlreadyBuyFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setFragemnt(new ReadFragment());
		hasBuy = getIntent().getIntExtra(HASBUY, 0);
		if (hasBuy == 1) {
			setFragemnt(mAlreadyBuyFragment);
		} else {
			setFragemnt(new UnBuyFragment());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != ObjectCacheToFile.getCache("localArr", String.class, this))
		{
			String arrStr = ObjectCacheToFile.getCache("localArr",
					String.class, this).toString();
			String[] strArr = arrStr.split(",");
			if (strArr.length == 5) {
				for (int i = 0; i < strArr.length; i++) {
					localArr[i] = Double.parseDouble(strArr[i]);
				}
			}
		}
	}

	public void onViewClick(View v) {
//		switch (v.getId()) {
//		case R.id.button_reading:
//			if (hasBuy == 1) {
//				setFragemnt(mAlreadyBuyFragment);
//				// Intent intent = new Intent(this, BuyActivity.class);
//				// intent.putExtra(BuyActivity.Point_Key, hasBuy);
//				// startActivity(intent);
//				doExam();
//			} else {
//				setFragemnt(new UnBuyFragment());
//			}
//			break;
//		case R.id.button_error:
//			setFragemnt(new CorrectionFragment());
//			break;
//		case R.id.button_chat:
//
//			break;
//		case R.id.button_setting:
//			setFragemnt(new SettingFragment());
//			break;
//		default:
//			break;
//		}
	}

	private int position = 0;

	private void doExam() {
		if (BaseApplication.allTaoBeanPath != null
				&& BaseApplication.allTaoBeanPath.size() > position) {
			String name = BaseApplication.allTaoBeanPath.get(0);
			BaseApplication.examName = name;
			name += ".json";
			if (!name.contains(BaseApplication.examAllPath)) {
				position++;
				doExam();
			}
			BaseApplication.examData = new HashMap<String, Object>();
			BaseApplication.judgeDifficulty(name);
			BaseApplication.initLesson();
			BaseApplication.taoBeans = BaseApplication.allTaoBeans.get(name);
			startActivity(ReadyActivity.createIntent(HomeActivity.this,
					BaseApplication.taoBeans
							.get(BaseApplication.currentExamLesson), name));
		}
	}

	public void setFragemnt(Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		int backStackCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		if (backStackCount > 0) {
			getSupportFragmentManager().popBackStack();
		}
		fragmentTransaction.replace(R.id.home_layout, fragment);
		fragmentTransaction.commit();
	}

	public static Intent createIntent(Context context, int point1) {
		Intent intent = new Intent(context, HomeActivity.class);
		intent.putExtra(HASBUY, point1);
		return intent;
	}
}
