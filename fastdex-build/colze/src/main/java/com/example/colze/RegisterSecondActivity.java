package com.example.colze;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.colze.bean.LoginBean;
import com.example.colze.bean.LoginParam;
import com.example.colze.bean.RegisterBean;
import com.example.colze.dialog.SelectSchoolDialog;
import com.example.colze.dialog.SelectSchoolDialog.SchoolSelectListener;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;

public class RegisterSecondActivity extends BaseActivity {
	private Button mButton_Icon;
	private Button mButton_School;
	private Button mButton_Go;
	private SelectSchoolDialog diglog = null;
	private LoginParam loginParam;

	public static RegisterSecondActivity instance;

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case UrlUtils.MethodType.TYPE_REG:
			if (msg.obj != null) {
				RegisterBean response = (RegisterBean) msg.obj;
				if (response.status == 1) {
					showToast("注册成功");
//					login(loginParam.userName, loginParam.passwd);
					Intent intent = new Intent(RegisterSecondActivity.this,LoginActivity.class);
					intent.putExtra(LoginActivity.KEY, 1);
					intent.putExtra(LoginActivity.NAME_KEY, loginParam.userName);
					intent.putExtra(LoginActivity.PASSWORD_KEY, loginParam.passwd);
					startActivity(intent);
					finish();
					if (RegisterActivity.instance != null) {
						RegisterActivity.instance.finish();
					}
					if (RegisterFirstActivity.instance != null) {
						RegisterFirstActivity.instance.finish();
					}
					if (RegisterSecondActivity.instance != null) {
						RegisterSecondActivity.instance.finish();
					}
					if (LoginActivity.instance != null) {
						LoginActivity.instance.finish();
					}
				} else {
					showToast(response.msg + "");
				}
			}
			break;
		case UrlUtils.MethodType.TYPE_LOGIN:
			if (msg.obj != null) {
				LoginBean response = (LoginBean) msg.obj;
				if (response.status == 1) {
					BaseApplication.loginBean = response;
					ObjectCacheToFile.doCache("LoginBean", response,
							RegisterSecondActivity.this);
					if (RegisterActivity.instance != null) {
						RegisterActivity.instance.finish();
					}
					if (RegisterFirstActivity.instance != null) {
						RegisterFirstActivity.instance.finish();
					}
					if (RegisterSecondActivity.instance != null) {
						RegisterSecondActivity.instance.finish();
					}
					if (LoginActivity.instance != null) {
						LoginActivity.instance.finish();
					}
					startActivity(HomeActivity.createIntent(
							RegisterSecondActivity.this, 0));
					finish();
				} else {
					showToast(response.msg + "");
				}
			}
			break;
		case -1:
			mButton_School.setText(loginParam.school + "");
			break;

		default:
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_second);
		instance = this;
		init();
	}

	/**
	 * 登录
	 */
	private void login(String userName, String passwd) {
		getDataByGet(
				UrlUtils.getUrl(UrlUtils.MethodName.LOGIN,
						new String[] { userName, passwd,
								ToolUtils.getAppInfo(), ToolUtils.getUUID() }),
				UrlUtils.MethodType.TYPE_LOGIN, handler);
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
		mButton_Icon = (Button) findViewById(R.id.button_selectIcon);
		mButton_School = (Button) findViewById(R.id.button_selectSchool);
		mButton_Go = (Button) findViewById(R.id.button_go);
	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.button_selectIcon: {
//			startActivityForResult(
//					ChoiceIconActivity.createIntent(this, loginParam), 1001);
//		}
//			break;
//		case R.id.button_selectSchool: {
//			diglog = new SelectSchoolDialog(this, BaseApplication.beans,
//					new SchoolSelectListener() {
//						@Override
//						public void onSchoolSelect(String province,
//								String city, String area, String school) {
//							showToast(province + "," + city + "," + area + ","
//									+ school);
//							handler.sendEmptyMessage(-1);
//							loginParam.school = school;
//							if (null != diglog) {
//								diglog.dismiss();
//							}
//						}
//					});
//			diglog.show();
//		}
//			break;
//		case R.id.button_go: {
//			reg();
//		}
//			break;
//		default:
//			break;
//		}
	}

	/**
	 * 注册
	 */
	private void reg() {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		paramList.add(new BasicNameValuePair("userName", loginParam.userName));
		paramList.add(new BasicNameValuePair("passwd", loginParam.passwd));
		paramList.add(new BasicNameValuePair("sex", loginParam.sex + ""));
		paramList
				.add(new BasicNameValuePair("facePic", loginParam.facePic + ""));
		paramList.add(new BasicNameValuePair("school", loginParam.school));
		paramList.add(new BasicNameValuePair("grade", loginParam.grade));
		paramList.add(new BasicNameValuePair("nickname", loginParam.nickname));
		getDataByPost(UrlUtils.getUrl(UrlUtils.MethodName.REG),
				UrlUtils.MethodType.TYPE_REG, handler, paramList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1001 && resultCode == 2002) {
			LoginParam lp = (LoginParam) data
					.getSerializableExtra(RegisterActivity.LOGINPARAM);
			loginParam.facePic = lp.facePic;
			loginParam.sex = lp.sex;
		}
	}

	public static Intent createIntent(Context context, LoginParam loginParam) {
		Intent intent = new Intent(context, RegisterSecondActivity.class);
		intent.putExtra(RegisterActivity.LOGINPARAM, loginParam);
		return intent;
	}
}
