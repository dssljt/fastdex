package com.example.colze.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles", "CommitPrefEdits" })
public class ObjectCacheToFile {

	private static final String EXT_NAME = ".object";
	private static final String CACHE_DIR = "obj_cache";

	/**
	 * 
	 * @param key
	 * @param result
	 * @param context
	 */
	public static void doCache(String key, Object result, Context context) {
//			//TODO 老代码  内存溢出
//			Logger.d("<< doCache to sp key: " + key);
//			SharedPreferences.Editor ed = getSP(context).edit();
//			Gson gs = new Gson();
//			ed.putString(key, gs.toJson(result));
//			ed.commit();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getCacheFileByKey(context,key)));
			oos.writeObject(result);
			Logger.d("<< doCache with object stream key: " + key);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private static SharedPreferences getSP(Context context) {
		return context.getSharedPreferences("all_cache",
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
	}

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
		try {
			Object obj = null;
			File cacheFile = getCacheFileByKey(context,key);
			if (cacheFile.exists()) {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(getCacheFileByKey(context,key)));
				obj =  is.readObject();
			}
			Logger.d("<< getCache with object stream key: " + key);
			return obj;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private static File getCacheFileByKey(Context context,String key) {
		try {
			return new File(getCacheDir(context), URLEncoder.encode(key,"UTF-8") + EXT_NAME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static File getCacheDir(Context context) {
		File cacheDir = new File(context.getCacheDir(),CACHE_DIR);
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		return cacheDir;
	}
}
