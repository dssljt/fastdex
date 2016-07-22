package com.example.colze.bean;

import java.io.Serializable;

public class UploadBean implements Serializable {

	public int status;
	public String msg;
	public Data data;

	public class Data {

	}

	@Override
	public String toString() {
		return "UploadBean [status=" + status + ", msg=" + msg + ", data="
				+ data + "]";
	}

}
