package com.example.colze;

import java.io.File;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.colze.bean.HasBuyBean;
import com.example.colze.bean.LoginBean;
import com.example.colze.dialog.ProgressDialog;
import com.example.colze.examsdk.ExamSDK;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.SchoolJsonUtils;
import com.example.colze.utils.StringUtil;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;
import com.example.colze.utils.zip.ZipUtil;
import com.example.colze.utils.zip.ZipUtil.ZipListener;

public class LoginActivity extends BaseActivity {

	private EditText edit_loginname, edit_password;

	public static LoginActivity instance;

	private ProgressDialog dialog;
	
	public static final String KEY = "key";
	public static final String NAME_KEY = "name";
	public static final String PASSWORD_KEY = "password";
	
	private int key = 0;
	private String name = "";
	private String password = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance = this;
		init();
		edit_loginname.setText("chen8899");
		edit_password.setText("chen8899");
	}

	@Override
	public void initData() {
		super.initData();
		if (null != ObjectCacheToFile.getCache("LoginBean", LoginBean.class,
				this)) {
			LoginBean olderLogin = (LoginBean) ObjectCacheToFile.getCache(
					"LoginBean", LoginBean.class, this);
			if ((null != olderLogin) && (null != olderLogin.data)) {
				if (!StringUtil.isEmpty(olderLogin.data.userName))
					edit_loginname.setText(olderLogin.data.userName);
				if (!StringUtil.isEmpty(olderLogin.data.passwd))
					edit_password.setText(olderLogin.data.passwd);
			}
		}
		
		key = getIntent().getIntExtra(KEY, 0);
		if(key==1)
		{
			name = getIntent().getStringExtra(NAME_KEY);
			password = getIntent().getStringExtra(PASSWORD_KEY);
			edit_loginname.setText(name);
			edit_password.setText(password);
			login(name, password);
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		edit_password = (EditText) findViewById(R.id.edit_password);
		edit_loginname = (EditText) findViewById(R.id.edit_loginname);
		dialog = new ProgressDialog(this);
	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.button_login:
//			String useName = edit_loginname.getText().toString();
//			String usePwd = edit_password.getText().toString();
//			if (ToolUtils.isNullOrEmpter(usePwd)) {
//				showToast("密码不能为空");
//				return;
//			}
//			if (ToolUtils.isNullOrEmpter(useName)) {
//				showToast("用户名不能为空");
//				return;
//			}
//			login(useName, usePwd);
//			break;
//		case R.id.button_register:
//			startActivity(RegisterActivity.createIntent(this));
//			break;
//
//		default:
//			break;
//		}
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
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case UrlUtils.MethodType.TYPE_LOGIN:
			if (msg.obj != null) {
				LoginBean response = (LoginBean) msg.obj;
				if (response.status == 1) {
					BaseApplication.loginBean = response;
					ObjectCacheToFile.doCache("LoginBean", response,
							LoginActivity.this);
					showToast("登录成功");
					//buy();
					download();
				} else {
					showToast(response.msg + "");
				}
			}
			break;
		case UrlUtils.MethodType.TYPE_HASBUY:
			if (msg.obj != null) {
				HasBuyBean response = (HasBuyBean) msg.obj;
				if (response != null && response.data != null) {
					startActivity(HomeActivity.createIntent(LoginActivity.this,
							response.data.point1));
					if (response.data.point1 == 1) {
						BaseApplication.examAllPath = "/examInOne";
					} else {
						BaseApplication.examAllPath = "/freeExam";
					}
				} else {
					BaseApplication.examAllPath = "/freeExam";
					startActivity(HomeActivity.createIntent(LoginActivity.this,
							1));
				}
				// BaseApplication.initAllTaoExams();
				// BaseApplication.readSchoolBean();
				new Thread(mRunnable).start();
			}
			break;
		case 10000:
			finish();
			break;

		default:
			break;
		}
		return super.handleMessage(msg);
	}

	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			String examsPath = AllContacts.sdcard + BaseApplication.examAllPath;
			File file = new File(examsPath);
			if (file.exists()) {
				File[] files = file.listFiles();
				for (File item : files) {
					if (!item.getName().startsWith("._")) {
						BaseApplication.allTaoBeanPath.add(item.getAbsolutePath()
								.replaceAll(".json", ""));
						BaseApplication.initTaoBeansByPath(item.getAbsolutePath());
					}
				}
			} else {
				System.out.println("000000");
			}
			BaseApplication.beans = SchoolJsonUtils.parson(LoginActivity.this);

			try {
				ExamSDK.init(LoginActivity.this);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			handler.sendEmptyMessage(10000);
		}
	};

	/**
	 * 是否购买
	 */
	private void buy() {
		Log.e("0000", UrlUtils.getUrl(UrlUtils.MethodName.HASBUY, new String[] {
						BaseApplication.loginBean.data.userName, "14" }));
		getDataByGet(
				UrlUtils.getUrl(UrlUtils.MethodName.HASBUY, new String[] {
						BaseApplication.loginBean.data.userName, "14" }),
				UrlUtils.MethodType.TYPE_HASBUY, handler);
	}

	/**
	 * 是否下载
	 */
	private void download() {
		ZipUtil zu = new ZipUtil(
				this,
				new ZipListener() {

					@Override
					public void onZipStartDownLoad() {
						try {
							dialog.setTitle("下载中");
							dialog.show();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onZipStartDecompression() {
						try {
							dialog.setTitle("解压中");
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onZipEndDownLoad() {
						try {
							dialog.setTitle("下载完成");
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onZipEndDecompression() {
						try {
							dialog.setTitle("解压完成");
							dialog.dismiss();
							buy();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onZipDownLoading(int position) {
						try {
							dialog.setContext(position + "%");
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onZipDownLoadErr() {
						try {
							dialog.setTitle("下载出错");
							dialog.dismiss();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onZipDecompressioning(int position) {
						try {
							dialog.setContext(position + "%");
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onZipDecompressionErr() {
						try {
							dialog.setTitle("解压出错");
							dialog.dismiss();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void noUpdate() {
						buy();
					}
				},
				"http://app.gaofy.com/upload/subject/"+ Config.downloadZip +"/ExamResource.zip",
				true);
		zu.start();
	}
}
