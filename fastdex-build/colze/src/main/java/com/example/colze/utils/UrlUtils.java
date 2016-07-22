package com.example.colze.utils;

import com.example.colze.Config;
import com.example.colze.bean.AnalysisBean;
import com.example.colze.bean.BaseBean;
import com.example.colze.bean.CheckLogin;
import com.example.colze.bean.ExamInfoBean;
import com.example.colze.bean.HasBuyBean;
import com.example.colze.bean.LoginBean;
import com.example.colze.bean.RegisterBean;
import com.example.colze.bean.School;
import com.example.colze.bean.UploadBean;

public class UrlUtils {

	public static final String BASEURL = "http://app2.gaofy.com:8080/";

	/**
	 * 获取URL
	 */
	public static String getUrl(String methodName, String... name) {
		String url = BASEURL + methodName;
		for (String str : name) {
			url += "/" + str;
		}
		return url;
	}

	public class MethodName {
		public static final String CHECKREG = "gaofy/checkreg";
		public static final String REG = "gaofy/reg";
		public static final String LOGIN = "gaofy/login";
		public static final String SEARCHSCHOOL = "gaofy/" + Config.interfaceUrl;
		public static final String GETSETINFO = "gaofy/getSetInfo";
		public static final String GETANALYSIS = "gaofy/getAnalysis";
		public static final String HASBUY = "gaofy/getMoney";
		public static final String UPLOADRESULT = "gaofy/uploadResult.do";
	}

	public class MethodType {
		public static final int TYPE_CHECKREG = 9000;
		public static final int TYPE_REG = 9001;
		public static final int TYPE_LOGIN = 9002;
		public static final int TYPE_SEARCHSCHOOL = 9003;
		public static final int TYPE_GETSETINFO = 9004;
		public static final int TYPE_GETANALYSIS = 9005;
		public static final int TYPE_HASBUY = 9006;
		public static final int TYPE_UPLOADRESULT = 9007;
	}

	@SuppressWarnings("rawtypes")
	public static Class getClassType(int type) {
		switch (type) {
		case MethodType.TYPE_CHECKREG:
			return CheckLogin.class;
		case MethodType.TYPE_REG:
			return RegisterBean.class;
		case MethodType.TYPE_LOGIN:
			return LoginBean.class;
		case MethodType.TYPE_SEARCHSCHOOL:
			return School.class;
		case MethodType.TYPE_GETSETINFO:
			return ExamInfoBean.class;
		case MethodType.TYPE_GETANALYSIS:
			return AnalysisBean.class;
		case MethodType.TYPE_HASBUY:
			return HasBuyBean.class;
		case MethodType.TYPE_UPLOADRESULT:
			return UploadBean.class;

		default:
			break;
		}
		return BaseBean.class;
	}

}
