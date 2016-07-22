package com.example.colze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.colze.bean.CheckLogin;
import com.example.colze.bean.TaoTiBean;
import com.example.colze.utils.UrlUtils;

@SuppressWarnings("unchecked")
public class MainActivity extends BaseActivity {

	private TextView tv_content;
//userName=高铁了, passwd=123456, sex=1, facePic=1, school=天津市河西区新平山小学, grade=1, nickname=一天就过去, hardWare=00000000-5f3d-ff90-1725-2d0e62cce401, appName=1.0
	private String userName = "afef", passwd = "123456", sex = "1",facePic = "1", school="皇家中学", grade = "7",nickName="一天就过去";
	private HashMap<String, TaoTiBean> taoBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case UrlUtils.MethodType.TYPE_CHECKREG:
				if (msg.obj != null) {
					CheckLogin response = (CheckLogin) msg.obj;
					if (response.status == 1) {
						/**
						 * 注册
						 */
						reg(userName, passwd, sex, facePic, school, grade, nickName);

					} else {
						showToast(response.msg + "");
					}
				}
				break;
			case UrlUtils.MethodType.TYPE_REG:
				break;

			default:
				break;
			}
			return false;
		}

	});

	@Override
	public void doHttp() {
		super.doHttp();
		checkReg();
	}

	/**
	 * 检查是否可注册
	 */
	private void checkReg() {
		getDataByGet(UrlUtils.getUrl(UrlUtils.MethodName.CHECKREG,
				new String[] { userName }), UrlUtils.MethodType.TYPE_CHECKREG,
				handler);
	}

	/**
	 * 注册
	 */
	private void reg(String userName, String passwd,
			String sex, String facePic, String school, String grade,
			String nickName) {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		paramList.add(new BasicNameValuePair("userName", userName));
		paramList.add(new BasicNameValuePair("passwd", passwd));
		paramList.add(new BasicNameValuePair("sex", sex));
		paramList.add(new BasicNameValuePair("facePic", facePic));
		paramList.add(new BasicNameValuePair("school", school));
		paramList.add(new BasicNameValuePair("grade", grade));
		paramList.add(new BasicNameValuePair("nickname", nickName));
		getDataByPost(UrlUtils.getUrl(UrlUtils.MethodName.REG),
				UrlUtils.MethodType.TYPE_REG, handler, paramList);
	}

	@Override
	public void initData() {
		super.initData();
		taoBeans = BaseApplication.taoBeans;
	}

	@Override
	public void initWidget() {
		super.initWidget();
		tv_content = (TextView) findViewById(R.id.tv_content);
	}

	public void onViewClick(View view) {
		super.onViewClick(view);
//		switch (view.getId()) {
//		case R.id.btn_ready:
//			// startActivity(new Intent(this, ReadyActivity.class));
////			if (taoBeans != null && taoBeans.size() > 0) {
////				startActivity(ReadyActivity.createIntent(this,
////						taoBeans.get(BaseApplication.currentExamLesson), ));
////			}
//			break;
//		case R.id.btn_topic:
//			startActivity(new Intent(this, TopicActivity.class));
//			break;
//		case R.id.btn_result:
//			startActivity(new Intent(this, ResultActivity.class));
//			break;
//
//		case R.id.btn_login:
//			startActivity(new Intent(this, LoginActivity.class));
//			break;
//		case R.id.btn_register:
//			startActivity(new Intent(this, RegisterActivity.class));
//			break;
//		case R.id.btn_touxiang:
//			startActivity(new Intent(this, HomeActivity.class));
//			break;
//		default:
//			break;
//		}
	}
}
