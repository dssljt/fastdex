package com.example.colze.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SchoolBean implements Serializable{

	public String province;
	public List<City> cities;
	
	public static class City{
		public String city;
		public List<String> area;

		@Override
		public String toString() {
			return "City [city=" + city + ", area=" + area + "]";
		}
		
	}

	@Override
	public String toString() {
		return "SchoolBean [province=" + province + ", cities=" + cities + "]";
	}
	
}
