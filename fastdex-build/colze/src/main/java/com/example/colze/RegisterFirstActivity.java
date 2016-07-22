package com.example.colze;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.colze.bean.LoginParam;
import com.example.colze.utils.ToolUtils;

public class RegisterFirstActivity extends BaseActivity {
	private EditText mEditText_RealName;
	private EditText mEditText_NickName;
	public static RegisterFirstActivity instance;

	/**
	 * 登陆参数
	 */
	private LoginParam loginParam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_first);
		instance = this;
		init();
	}

	@Override
	public void initData() {
		super.initData();
		loginParam = (LoginParam) getIntent()
				.getSerializableExtra(RegisterActivity.LOGINPARAM);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mEditText_RealName = (EditText) findViewById(R.id.edit_realname);
		mEditText_NickName = (EditText) findViewById(R.id.edit_nickname);
	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.button_next:
//			String useName = mEditText_RealName.getText().toString().trim();
//			if (ToolUtils.isNullOrEmpter(useName)) {
//				showToast("你还没有填写真实姓名");
//				return;
//			}
//			String nickName = mEditText_NickName.getText().toString().trim();
//			if (ToolUtils.isNullOrEmpter(nickName)) {
//				showToast("你还没有填写昵称");
//				return;
//			}
//			loginParam.trueName = useName;
//			loginParam.nickname = nickName;
//			startActivity(RegisterSecondActivity.createIntent(
//					RegisterFirstActivity.this, loginParam));
//			break;
//		default:
//			break;
//		}
	}

	public static Intent createIntent(Context context, LoginParam loginParam) {
		Intent intent = new Intent(context, RegisterFirstActivity.class);
		intent.putExtra(RegisterActivity.LOGINPARAM, loginParam);
		return intent;
	}
}
