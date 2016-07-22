package com.example.colze.bean;

import java.io.Serializable;

public class DanTiBean implements Serializable {

	public DanTi T;
	
	public class DanTi implements Serializable{
		
		public Head Head;
		public Item Q1;
		public Item Q2;
		public Item Q3;
		public Item Q4;
		public Item Q5;
		public Item Q6;
		public Item Q7;
		public Item Q8;
		public Item Q9;
		public Item Q10;
		@Override
		public String toString() {
			return "DanTi [Head=" + Head + ", Q1=" + Q1 + ", Q2=" + Q2
					+ ", Q3=" + Q3 + ", Q4=" + Q4 + ", Q5=" + Q5 + "]";
		}
		
		
		
	}
	
	public class Item implements Serializable{
		public Body Body;
		public Key Key;
		@Override
		public String toString() {
			return "Item [Body=" + Body + ", Key=" + Key + "]";
		}
		
		
	}
	
	public class Key implements Serializable{
		public String Ans;
		public String Word;
		public String Content;
		public String Skill;
		public String Point;
		public int S1;
		public int S2;
		public int S3;
		public int S4;
		public int S5;
		public String Frequency;
		@Override
		public String toString() {
			return "Key [Ans=" + Ans + ", Word=" + Word + ", Content="
					+ Content + ", Skill=" + Skill + ", Point=" + Point
					+ ", S1=" + S1 + ", S2=" + S2 + ", S3=" + S3 + ", S4=" + S4
					+ ", S5=" + S5 + ", Frequency=" + Frequency + "]";
		}
		
		
	}
	
	public class Body implements Serializable{
		public String Sub;
		public String An1;
		public String An2;
		public String An3;
		public String An4;
		@Override
		public String toString() {
			return "Body [Sub=" + Sub + ", An1=" + An1 + ", An2=" + An2
					+ ", An3=" + An3 + ", An4=" + An4 + "]";
		}
		
		
	}
	
	public class Head implements Serializable{
		public String Part;
		public String Type1;
		public String Type2;
		public String Rem;
		public String AnsQues;
		public String Txt;
		public String Pic;
		public String Sound;
		@Override
		public String toString() {
			return "Head [Part=" + Part + ", Type1=" + Type1 + ", Type2="
					+ Type2 + ", Rem=" + Rem + ", AnsQues=" + AnsQues
					+ ", Txt=" + Txt + ", Pic=" + Pic + ", Sound=" + Sound
					+ "]";
		}
		
		
	}

	@Override
	public String toString() {
		return "DanTiBean [T=" + T + "]";
	}
	
	
}
