package com.example.colze.bean;

import java.io.Serializable;

public class LoginParam implements Serializable {

	public String userName;
	public String passwd;
	public int sex;
	public String facePic;
	public String school;
	public String grade = "1";
	public String nickname;
	public String trueName;

	@Override
	public String toString() {
		return "LoginParam [userName=" + userName + ", passwd=" + passwd
				+ ", sex=" + sex + ", facePic=" + facePic + ", school="
				+ school + ", grade=" + grade + ", nickname=" + nickname + "]";
	}

	public LoginParam() {
	}
}
