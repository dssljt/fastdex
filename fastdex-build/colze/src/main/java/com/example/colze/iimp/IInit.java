package com.example.colze.iimp;

import android.view.View;

public interface IInit {
	/**
	 * 初始化基本数据
	 */
	public void initData();

	/**
	 * 初始化界面
	 */
	public void initWidget();

	/**
	 * 初始化第三方SDK
	 */
	public void initSDK();

	/**
	 * 启动网络请求
	 */
	public void doHttp();

	/**
	 * 时间监听器
	 */
	public void onViewClick(View v);
}
