package com.example.colze.bean;

import java.io.Serializable;

public class AnalysisBean implements Serializable {

	public int status;
	public String msg;
	public Data data;

	public class Data implements Serializable {
		public int id;
		public int qu_allCount;
		public int qu_rightCount;
		public int qu_A_count;
		public int qu_B_count;
		public int qu_C_count;
		public int qu_D_count;
		public String reserved;
		public String qu_id;
	}

}
