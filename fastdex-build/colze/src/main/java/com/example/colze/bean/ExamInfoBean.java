package com.example.colze.bean;

import java.io.Serializable;

public class ExamInfoBean implements Serializable {

	public int status;
	public String msg;
	public Data data;
	
	public class Data implements Serializable{
		public String id;
		public String personNum;
		public float ratio1;
		public float ratio2;
		public float ratio3;
		public float ratio4;
		public String scoreNum;
		public String set_id;
	}
}
