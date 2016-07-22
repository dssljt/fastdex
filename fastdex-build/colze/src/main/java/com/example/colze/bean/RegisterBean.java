package com.example.colze.bean;

import com.example.colze.utils.ToolUtils;

import java.io.Serializable;

public class RegisterBean implements Serializable {

	public int status;
	public String msg;
	public String data;
	
	public String getUseId(){
		String useId = "";
		if(!ToolUtils.isNullOrEmpter(data)){
			useId = data.split("_")[0];
		}
		return useId;
	}
	
	public String getOutToken(){
		String outToken = "";
		if(!ToolUtils.isNullOrEmpter(data)){
			if(data.split("_").length > 1){
				outToken = data.split("_")[1];
			}
		}
		return outToken;
	}
	
}
