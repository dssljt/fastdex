package com.example.colze;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.example.colze.bean.LoginBean;
import com.example.colze.bean.SchoolBean;
import com.example.colze.bean.TaoTiBean;
import com.example.colze.examsdk.ExamSDK;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.LoggerHashMap;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.SchoolJsonUtils;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

	public static int currentDifficulty = 0;// 难度
	public static String currentExam = "";// 当前阅读路劲
	public static HashMap<String, TaoTiBean> taoBeans;// 当前阅读的试卷
	public static HashMap<String, Object> examData;// 已解析的数据
	public static String currentExamLesson = "Lesson1";
	public static ArrayList<SchoolBean> beans;
	public static ArrayList<String> allTaoBeanPath = new ArrayList<String>();

	public static LoginBean loginBean;

	public static String examName;

	public static String examAllPath = "/freeExam";
	/**
	 * 所有试题
	 */
	public static HashMap<String, HashMap<String, TaoTiBean>> allTaoBeans = new LoggerHashMap<String, HashMap<String, TaoTiBean>>("allTaoBeans");

	private static Context mContext;

	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		examData = new LoggerHashMap<String, Object>("examData");
		// initTaoBeansByPath(AllContacts.testPath);
		// initAllTaoExams();
		readSchoolBean();

		CrashReport.initCrashReport(this,"900012595",false);
	}

	public static void readSchoolBean() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				beans = SchoolJsonUtils.parson(mContext);
			}
		}).start();
	}

	public static void initAllTaoExams() {
		new Thread(   new Runnable() {

			@Override
			public void run() {
				String examsPath = AllContacts.sdcard + examAllPath;
				File file = new File(examsPath);
				if (file.exists()) {
					File[] files = file.listFiles();
					for (File item : files) {
						if (!item.getName().startsWith("._")) {
							allTaoBeanPath.add(item.getAbsolutePath().replaceAll(
									".json", ""));
							initTaoBeansByPath(item.getAbsolutePath());
						}
					}
				}

				beans = SchoolJsonUtils.parson(mContext);
			}
		}).start();
	}

	/**
	 * 
	 * @param path
	 *            根据路劲读取套题内容
	 */
	@SuppressWarnings("unchecked")
	public static void initTaoBeansByPath(String path) {
		taoBeans = new LoggerHashMap<String, TaoTiBean>("taoBeans");
		taoBeans.putAll((HashMap<String, TaoTiBean>) TaoTiBean.allTaoTi(path));

		initBeanIsRead();
		allTaoBeans.put(path, taoBeans);
	}

	private static void initBeanIsRead() {
		Set<String> set = taoBeans.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String name = it.next();
			TaoTiBean item = (TaoTiBean) ObjectCacheToFile.getCache(
					AllContacts.testPath + name, TaoTiBean.class, mContext);
			if (item != null) {
				taoBeans.put(name, item);
			}
		}
	}

	public static void initLesson() {
		Object obj = ObjectCacheToFile.getCache("currentDifficulty_Lesson_"
				+ currentDifficulty, String.class, mContext);
		if (obj != null) {
			currentExamLesson = (String) obj;
			int i = Integer.valueOf(currentExamLesson.replaceAll("Lesson", ""));
			int lessonNo = i + 1;
			int maxLesson = ExamSDK.getDefaultLessonGroup().getSize();
			if (lessonNo > maxLesson) {
				lessonNo = maxLesson;
			}
			currentExamLesson = "Lesson" + lessonNo;
		} else {
			currentExamLesson = "Lesson1";
		}
	}

	public static void judgeDifficulty(String path) {
		//path = "/storage/sdcard0/YueDuLiJie/ExamResource/examInOne/S14_1_0001.json";
		String[] strs = path.split("_");
		BaseApplication.currentExam = path;
		BaseApplication.currentDifficulty = Integer.valueOf(strs[1]);
		Object obj = ObjectCacheToFile.getCache("currentDifficulty_Lesson_"
				+ currentDifficulty, String.class, mContext);
		if (obj == null) {
			currentExamLesson = "Lesson1";
		}
	}

	/**
	 * 下一试卷
	 */
	public static void nextExam() {
		// int i = Integer.valueOf(currentExamLesson.replaceAll("Lesson", ""));
		// currentExamLesson = "Lesson" + (i + 1);
		// if (taoBeans.get(currentExamLesson) == null) {
		// currentExamLesson = "Lesson" + i;
		// }
		/**
		 * 存储当前做到了那一套题
		 */
		ObjectCacheToFile.doCache("currentDifficulty_Lesson_"
				+ currentDifficulty, currentExamLesson, mContext);
	}

	/**
	 * 获取当前开始的试卷
	 */
	public static TaoTiBean getExamTaoTi() {
		return taoBeans.get(currentExamLesson);
	}

}
