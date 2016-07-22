package com.example.colze.bean;

import java.io.Serializable;

public class LoginBean implements Serializable {

	public int status;
	public String msg;
	public Data data;

	public class Data implements Serializable {
		public String appName;
		public String birth;
		public String city;
		public String email;
		public String engName;
		public String facePic;
		public String grade;
		public String hardware1;
		public String hardware2;
		public String headPic;
		public String id;
		public String myWord;
		public String nickname;
		public String outToken;
		public String passwd;
		public String phoneNum;
		public String province;
		public String qq;
		public String regHardware;
		public String regTime;
		public String school;
		public int sex;
		public String training;
		public String trueName;
		public String userName;
	}
}
