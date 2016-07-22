package com.example.colze.utils;

import com.example.colze.Config;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	/**
	 * 无参数传递的
	 * 
	 * @param url
	 * @return
	 */
	public synchronized static String getPostResult(String url) {
		HttpGet post = new HttpGet(url);
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 3000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(response.getEntity());
				return URLDecoder.decode(content, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"code\":405,\"msg\":\"网络超时！\"}";
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return "{\"code\":405,\"msg\":\"网络超时！\"}";
	}

	/**
	 * 有参数传递的
	 * 
	 * @param url
	 * @param paramList
	 * @return
	 */
	public synchronized static String getPostResult(String url,
			List<NameValuePair> paramList) {
		UrlEncodedFormEntity entity = null;
		paramList.add(new BasicNameValuePair("hardWare", ToolUtils.getUUID()));
		paramList
				.add(new BasicNameValuePair("appName", Config.appName + ""));
		try {
			entity = new UrlEncodedFormEntity(paramList, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpPost post = new HttpPost(url);
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
		post.setEntity(entity);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		try {
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				return URLDecoder.decode(content, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"code\":405,\"msg\":\"网络超时！\"}";
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return "{\"code\":405,\"msg\":\"网络超时！\"}";
	}

}
