package com.example.colze.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Window;
import android.widget.TextView;

import com.example.colze.BaseApplication;
import com.example.colze.bean.DanTiBean.Body;
import com.example.colze.bean.DanTiBean.Key;
import com.google.gson.Gson;

public class ToolUtils {
	/**
	 * dp转换成px
	 * 
	 * @param dpValue
	 * @return
	 */
	public static int dp2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换成dp
	 * 
	 * @param dpValue
	 * @return
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 设备唯一标示
	 * @return
	 */
	public static String getUUID(){
		final TelephonyManager tm = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String uniqueId = deviceUuid.toString();
	    return uniqueId;
	}
	
	/**
	 * 获取版本名
	 * @return
	 */
	public static String getAppInfo() {
 		try {
 			String pkName = BaseApplication.getContext().getPackageName();
 			String versionName = BaseApplication.getContext().getPackageManager().getPackageInfo(
 					pkName, 0).versionName;
 			int versionCode = BaseApplication.getContext().getPackageManager()
 					.getPackageInfo(pkName, 0).versionCode;
// 			return pkName + "   " + versionName + "  " + versionCode;
 			return versionName;
 		} catch (Exception e) {
 		}
 		return null;
 	}

	/**
	 * 全屏状态栏留着
	 */
	public static void fullScreenLearnNotice(Context context) {
		((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		String content = "";
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				content += tempString + "\n";
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return content;
	}

	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 */
	public static byte[] readFileByBytes(String fileName) {
		byte[] allByte = null;
		File file = new File(fileName);
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try {
			// 一次读多个字节
			int byteread = 0;
			in = new FileInputStream(fileName);
			baos = new ByteArrayOutputStream();
			// showAvailableBytes(in);
			allByte = new byte[in.available()];
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(allByte)) != -1) {
				baos.write(allByte, 0, byteread);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					if (baos != null) {
						baos.close();
					}
				} catch (IOException e1) {
				}
			}
		}
		return allByte;
	}

	/**
	 * 显示输入流中还剩的字节数
	 */
	public static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 套题需要转map
	 * 
	 * @param jsStr
	 */
	public static HashMap<String, Object> jsStrToMap(String jsStr, Class clazz) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		JSONObject jsonObject;
		String keys = "";
		try {
			jsonObject = new JSONObject(jsStr);
			Iterator it = jsonObject.keys();
			Gson gson = new Gson();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				Object value = (Object) jsonObject.get(key);
				data.put(key, gson.fromJson(value.toString(), clazz));
			}
		} catch (JSONException e) {
		}
		return data;
	}

	public static boolean isFileExists(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 单体需要转json
	 * 
	 * @param jsStr
	 */
	public static Object jsStrToJson(String jsStr, Class clazz) {
		Gson gson = new Gson();
		return gson.fromJson(jsStr, clazz);
	}

	/**
	 * 保留2位小数
	 */
	public static String limitDecimal(double value) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(value);
	}

	/**
	 * 将图片加入TextView
	 * 
	 * @param view
	 * @param bitmap
	 * @param content
	 */
	public static void setImageToTextView(Context context, TextView view,
			Bitmap bitmap, String content) {
		ImageSpan imgSpan = new ImageSpan(context, bitmap);
		SpannableString spanString = new SpannableString("icon");
		spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(spanString);
		view.append("");
	}

	/**
	 * 字节转图片
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 判断字串是否为null或者为""
	 * 
	 * @param text
	 * @return
	 */
	@SuppressLint("NewApi")
	public static boolean isNullOrEmpter(String text) {
		if (text == null || text.isEmpty() || "null".equals(text)) {
			return true;
		}
		return false;
	}

	/**
	 * 组合问题和选项
	 */
	public static String getQusContent(Body body) {
		return body.Sub + "\nA. " + body.An1 + "\nB. " + body.An2 + "\nC. "
				+ body.An3 + "\nD. " + body.An4;
	}

	/**
	 * 題目解析
	 */
	public static String getQusAns(Key key) {
		String ans = "#正确答案#\n " + key.Ans + "\n\n";
		ans += "#解析#\n " + key.Content + "\n";
		ans += "#技巧#\n" + key.Skill;
		ans += "#考点#\n" + key.Point;
		ans = ans.replaceAll("nn", "\n");
		return ans;
	}

	/**
	 * 获取译文
	 * 
	 * @param name
	 */
	public static String getTranslate(String name) {
		return "";
	}
	/**
	 * 随机生成4位数
	 */
	public static String getCode(){
		return Integer.valueOf((int) (Math.random()*9000+1000))+"";
	}
	
	public static String getStringByFloat(float score){
		score *=100;
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(score);
	}
}
