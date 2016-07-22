package com.example.colze.bean;

import java.io.Serializable;
import java.util.List;

public class School implements Serializable {

	public int status;
	public String msg;
	public List<Data> data;

	public class Data implements Serializable {
		public int id;
		public String sheng;
		public String shi;
		public String xian;
		public String xiao;
	}

}
