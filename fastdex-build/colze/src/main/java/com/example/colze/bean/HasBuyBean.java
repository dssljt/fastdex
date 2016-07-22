package com.example.colze.bean;

import java.io.Serializable;

public class HasBuyBean implements Serializable {

	public int status;
	public String msg;

	public Data data;

	public class Data implements Serializable {
		public String appName;
		public String coin;
		public String diamond;
		public String id;
		public String item1;
		public String item2;
		public String item3;
		public String item4;
		public String item5;
		public String item6;
		public String item7;
		public String item8;
		public String item9;
		public String item10;
		public String lastLogin;
		public int point1;
		public String point2;
		public String point3;
		public String point4;
		public String userName;

		@Override
		public String toString() {
			return "Data [appName=" + appName + ", coin=" + coin + ", diamond="
					+ diamond + ", id=" + id + ", item1=" + item1 + ", item2="
					+ item2 + ", item3=" + item3 + ", item4=" + item4
					+ ", item5=" + item5 + ", item6=" + item6 + ", item7="
					+ item7 + ", item8=" + item8 + ", item9=" + item9
					+ ", item10=" + item10 + ", lastLogin=" + lastLogin
					+ ", point1=" + point1 + ", point2=" + point2 + ", point3="
					+ point3 + ", point4=" + point4 + ", userName=" + userName
					+ "]";
		}
	}

	@Override
	public String toString() {
		return "HasBuyBean [status=" + status + ", msg=" + msg + ", data="
				+ data + "]";
	}

}
