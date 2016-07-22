package com.example.colze.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.example.colze.utils.ToolUtils;

/**
 * 
 * @author GodXJ 套题
 */
public class TaoTiBean implements Serializable {

	public Head Head;
	public Body Body;
	public boolean hasRead = false;

	public static class Head implements Serializable {
		public String Name;
		public String About;
		public String NumQ;
		public float TestTime;
		public float PassScore;
		public String Difficulty;

		@Override
		public String toString() {
			return "Head [Name=" + Name + ", About=" + About + ", NumQ=" + NumQ
					+ ", TestTime=" + TestTime + ", PassScore=" + PassScore
					+ ", Difficulty=" + Difficulty + "]";
		}
	}

	public static class Body implements Serializable {
		public String Q1;
		public String Q2;
		public String Q3;
		public String Q4;
		public String Q5;
		public String Q6;
		public String Q7;
		public String Q8;
		public String Q9;
		public String Q10;
		public String Q11;
		public String Q12;
		public String Q13;
		public String Q14;
		public String Q15;
		public String Q16;
		public String Q17;
		public String Q18;
		public String Q19;
		public String Q20;

		private String lessonNo;

		public String getLessonNoNumber() {
			String ret = "";
			try {
				ret = lessonNo.replace("Lesson","");
			} catch (Throwable e) {

			}
			return ret;
		}

		@Override
		public String toString() {
			return "Body{" +
					"Q1='" + Q1 + '\'' +
					", Q2='" + Q2 + '\'' +
					", Q3='" + Q3 + '\'' +
					", Q4='" + Q4 + '\'' +
					", Q5='" + Q5 + '\'' +
					", Q6='" + Q6 + '\'' +
					", Q7='" + Q7 + '\'' +
					", Q8='" + Q8 + '\'' +
					", Q9='" + Q9 + '\'' +
					", Q10='" + Q10 + '\'' +
					", Q11='" + Q11 + '\'' +
					", Q12='" + Q12 + '\'' +
					", Q13='" + Q13 + '\'' +
					", Q14='" + Q14 + '\'' +
					", Q15='" + Q15 + '\'' +
					", Q16='" + Q16 + '\'' +
					", Q17='" + Q17 + '\'' +
					", Q18='" + Q18 + '\'' +
					", Q19='" + Q19 + '\'' +
					", Q20='" + Q20 + '\'' +
					", lessonNo='" + lessonNo +
					'}';
		}

	}

	@Override
	public String toString() {
		return "TaoTiBean [Head=" + Head + ", Body=" + Body + "]";
	}

	public static HashMap<String, ?> allTaoTi(String filePath) {
		String content = ToolUtils.readFileByLines(filePath);
		HashMap<String, ?> objs = ToolUtils
				.jsStrToMap(content, TaoTiBean.class);

		for (Map.Entry<String,TaoTiBean> entry : ((HashMap<String,TaoTiBean>)(objs)).entrySet()) {
			TaoTiBean taoTiBean = entry.getValue();
			if (taoTiBean.Body != null) {
				taoTiBean.Body.lessonNo = entry.getKey();
				//taoTiBean.Body.head = taoTiBean.Head;
			}
		}
		return objs;
	}
}
