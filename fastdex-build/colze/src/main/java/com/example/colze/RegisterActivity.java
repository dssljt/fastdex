package com.example.colze;

import java.util.Timer;
import java.util.TimerTask;

import com.example.colze.bean.CheckLogin;
import com.example.colze.bean.LoginParam;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class RegisterActivity extends BaseActivity {

	public static final String LOGINPARAM = "loginParam";
	private String userName = "huangdong", passwd = "123456", code = "";
	private TextView mTextView_Time;
	private EditText edit_phonenum, edit_password, edit_codenum;
	private Button mButton_Code;
	public static RegisterActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		instance = this;
		init();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case UrlUtils.MethodType.TYPE_CHECKREG:
			if (msg.obj != null) {
				CheckLogin response = (CheckLogin) msg.obj;
				if (response.status == 1) {
					/**
					 * 获取验证码
					 */
					sendCode(userName);
				} else {
					showToast(response.msg + "");
				}
			}
			break;
		case UrlUtils.MethodType.TYPE_REG:
			break;
		case 10000:
			break;

		default:
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mTextView_Time = (TextView) findViewById(R.id.text_waitTime);
		mButton_Code = (Button) findViewById(R.id.codeButton);
		edit_phonenum = (EditText) findViewById(R.id.edit_phonenum);
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_codenum = (EditText) findViewById(R.id.edit_codenum);
	}

	/**
	 * 检查是否可注册
	 */
	private void checkReg() {
		getDataByGet(UrlUtils.getUrl(UrlUtils.MethodName.CHECKREG,
				new String[] { userName }), UrlUtils.MethodType.TYPE_CHECKREG,
				super.handler);
	}

	@Override
	public void onViewClick(View v) {
//		switch (v.getId()) {
//		case R.id.codeButton:
//			/**
//			 * 获取验证码
//			 */
//			userName = edit_phonenum.getText().toString();
//			if (ToolUtils.isNullOrEmpter(userName)) {
//				showToast("请输入手机号码");
//				return;
//			}
//			checkReg();
//			break;
//		case R.id.button_sure:
//			userName = edit_phonenum.getText().toString();
//			String et_code = edit_codenum.getText().toString();
//			passwd = edit_password.getText().toString();
//			if (ToolUtils.isNullOrEmpter(userName)) {
//				showToast("请输入手机号码");
//				return;
//			}
//			if (ToolUtils.isNullOrEmpter(passwd)) {
//				showToast("密码不能为空");
//				return;
//			}
//			if (ToolUtils.isNullOrEmpter(et_code)) {
//				showToast("验证码不能为空");
//				return;
//			}
//			if (et_code.equals(code)) {
//				/**
//				 * 第一步注册成功
//				 */
//				LoginParam loginParam = new LoginParam();
//				loginParam.userName = userName;
//				loginParam.passwd = passwd;
//				startActivity(RegisterFirstActivity.createIntent(this,
//						loginParam));
//			} else {
//				showToast("验证码输入不正确");
//			}
//			break;
//		case R.id.button_cancel:
//			finish();
//			break;
//		default:
//			break;
//		}
//		super.onViewClick(v);
	}

	private void sendCode(String userName) {
		code = ToolUtils.getCode();
		showToast(code);
		getDataByGet("http://app2.gaofy.com:8080/gaofy/sms/" + userName + "/"
				+ code, 10000, handler);
		startCountdown();
	}

	private void startCountdown() {
		mButton_Code.setVisibility(View.GONE);
		mTextView_Time.setVisibility(View.VISIBLE);
		mTextView_Time.setText(nowTime + "s");
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (task == null) {
			task = new TimerTask() {
				@Override
				public void run() {
					handler.sendMessage(new Message());
				}
			};
		}
		mTimer.schedule(task, 1000, 1000);
	}

	private void endCountdown() {
		mTimer.cancel();
		mTimer = null;
		task.cancel();
		task = null;
		nowTime = 60;
		mButton_Code.setVisibility(View.VISIBLE);
		mTextView_Time.setVisibility(View.GONE);
	}

	public static Intent createIntent(Context context) {
		Intent intent = new Intent(context, RegisterActivity.class);
		return intent;
	}

	private int nowTime = 60;
	private Timer mTimer = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			handler.sendMessage(new Message());
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			nowTime--;
			if (nowTime < 0) {
				endCountdown();
			} else {
				mTextView_Time.setText(nowTime + "s");
			}
		}
	};
}
