package com.example.colze.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles", "CommitPrefEdits" })
public class ObjectCacheToFile2 {

	/**
	 *
	 * @param key
	 * @param result
	 * @param context
	 */
	public static void doCache(String key, Object result, Context context) {
		Editor ed = getSP(context).edit();
		Gson gs = new Gson();
		ed.putString(key, gs.toJson(result));
		ed.commit();
	}

	@SuppressWarnings("deprecation")
	private static SharedPreferences getSP(Context context) {
		return context.getSharedPreferences("all_cache",
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
	}

//	/**
//	 * 鏍规嵁key鑾峰彇result绫诲瀷瀵硅薄
//	 *
//	 * @param key
//	 * @param result
//	 * @param context
//	 * @return
//	 */
//	public static Object getCache(String key, TypeToken<?> result,
//			Context context) {
//		String ret = getSP(context).getString(key, null);
//		Object obj = null;
//		if ("".equals(ret) || ret == null) {
//			return obj;
//		}
//		Gson gs = new Gson();
//		obj = gs.fromJson(ret, result.getType());
//		return obj;
//	}

	/**
	 * 鏍规嵁key鑾峰彇result绫诲瀷瀵硅薄鏇村叿绫诲瀷
	 *
	 * @param key
	 * @param result
	 * @param context
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getCache(String key, Class cls, Context context) {
		String ret = getSP(context).getString(key, null);
		Object obj = null;
		if ("".equals(ret) || ret == null) {
			return obj;
		}
		Gson gs = new Gson();
		obj = gs.fromJson(ret, cls);
		return obj;
	}

	public static void clearObjectByKey(Context context, String key) {
		Editor ed = getSP(context).edit();
		ed.putString(key, null);
		ed.commit();
	}

	/**
	 * 娓呯悊鏁版嵁
	 *
	 * @param context
	 */
	public static void clearAllCache(Context context) {
		Editor edit = getSP(context).edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * 瀹夎鍚庣殑鍩烘湰鏁版嵁涓嶆竻妤�
	 */
	@SuppressWarnings("deprecation")
	public static void doBaseCache(String key, Object result, Context context) {
		Editor preferences = context.getSharedPreferences(
				"base_cache",
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE)
				.edit();
		Gson gs = new Gson();
		preferences.putString(key, gs.toJson(result));
		preferences.commit();
	}

	/**
	 * 鑾峰彇瀹夎鍩烘湰鏁版嵁
	 */
	@SuppressWarnings("deprecation")
	public static Object getBaseCache(String key, TypeToken<?> result,
			Context context) {
		SharedPreferences sp = context.getSharedPreferences("base_cache",
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
		String ret = sp.getString(key, null);
		Object obj = null;
		if ("".equals(ret) || ret == null) {
			return obj;
		}
		Gson gs = new Gson();
		obj = gs.fromJson(ret, result.getType());
		return obj;
	}
}
