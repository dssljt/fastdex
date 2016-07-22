package com.example.colze.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.colze.bean.SchoolBean;
import com.example.colze.bean.SchoolBean.City;

public class SchoolJsonUtils {

	public static ArrayList<SchoolBean> parson(Context context) {
		ArrayList<SchoolBean> beans = new ArrayList<SchoolBean>();
		InputStream is = null;
		String content = "";
		try {
			AssetManager am = context.getResources().getAssets();
			is = am.open("citySchool.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			content = new String(buffer, "utf-8");
		} catch (Exception e) {

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (!ToolUtils.isNullOrEmpter(content)) {
			try {
				JSONObject json = new JSONObject(content);
				JSONArray prov = json.getJSONArray("prov");
				/**
				 * 获取省份
				 */
				for (int i = 0; i < prov.length(); i++) {
					JSONObject obj = prov.getJSONObject(i);
					Iterator it = obj.keys();
					SchoolBean bean = new SchoolBean();
					while (it.hasNext()) {
						String str = (String) it.next();
						bean.province = str;
						JSONArray citiesJA = obj.getJSONArray(str);
						ArrayList<City> cities = new ArrayList<City>();
						for (int j = 0; j < citiesJA.length(); j++) {
							JSONObject ctObj = citiesJA.getJSONObject(j);
							Iterator ctIt = ctObj.keys();
							City city = new City();
							while (ctIt.hasNext()) {
								String cityName = (String) ctIt.next();
								city.city = cityName;
								JSONArray areaJA = ctObj.getJSONArray(cityName);
								ArrayList<String> areas = new ArrayList<String>();
								for (int k = 0; k < areaJA.length(); k++) {
									areas.add(areaJA.getJSONObject(k)
											.getString("district"));
								}
								city.area = areas;
							}
							cities.add(city);
						}
						bean.cities = cities;
					}
					beans.add(bean);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return beans;
	}

}
