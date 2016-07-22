package com.example.colze;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.colze.iimp.IInit;
import com.example.colze.utils.HttpUtils;
import com.example.colze.utils.ToolUtils;
import com.example.colze.utils.UrlUtils;
import com.google.gson.Gson;

public class BaseActivity extends Activity implements IInit, Handler.Callback {

	private static final String TAG = BaseActivity.class.getSimpleName();
	protected LayoutInflater layoutInflater;
	protected Handler handler = new Handler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ToolUtils.fullScreenLearnNotice(this);
	}

	protected void init() {
		initWidget();
		initData();
		doHttp();
	}

	@Override
	public void initData() {
		layoutInflater = LayoutInflater.from(this);
	}

	@Override
	public void initWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initSDK() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doHttp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void getDataByGet(final String url, final int type,
			final Handler handler) {
		if (isConnect()) {
			new Thread(new Runnable() {

				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					Log.d(TAG,"<<<< url: " + url);
					String content = HttpUtils.getPostResult(url);
					Log.d(TAG,"<<<< content: " + content);
					Gson gson = new Gson();
					if (!ToolUtils.isNullOrEmpter(content)) {
						handler.sendMessage(handler.obtainMessage(
								type,
								gson.fromJson(content,
										UrlUtils.getClassType(type))));
					} else {
						BaseActivity.this.baseHandler
								.sendMessage(BaseActivity.this.baseHandler
										.obtainMessage(1001));
					}
				}
			}).start();
		}
	}

	public void getDataByPost(final String url, final int type,
			final Handler handler, final List<NameValuePair> paramList) {
		if (isConnect()) {
			new Thread(new Runnable() {

				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					String content = HttpUtils.getPostResult(url, paramList);
					System.out.println(content);
					Gson gson = new Gson();
					if (!ToolUtils.isNullOrEmpter(content)) {
						handler.sendMessage(handler.obtainMessage(
								type,
								gson.fromJson(content,
										UrlUtils.getClassType(type))));
					} else {
						BaseActivity.this.baseHandler
								.sendMessage(BaseActivity.this.baseHandler
										.obtainMessage(1001));
					}
				}
			}).start();
		}
	}

	protected Handler baseHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 1001:
				showToast("数据异常");
				break;

			default:
				break;
			}
			return false;
		}
	});

	public boolean isConnect() {
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			showToast("无互联网连接");
			return false;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
}
