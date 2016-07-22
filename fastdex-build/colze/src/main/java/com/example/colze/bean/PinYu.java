package com.example.colze.bean;

import java.io.Serializable;

public class PinYu implements Serializable {
    public String cihui;
    public String  lijie;
    public String  juxing;
    public String shitai;
    public String  cizu;
    
    public String getString()
    {
    	return "词汇:"+cihui+"\n词组:"+cizu+"\n时态:"+shitai+"\n句型:"+juxing+"\n理解:"+lijie;
    }
}
