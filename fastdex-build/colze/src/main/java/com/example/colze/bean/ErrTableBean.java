package com.example.colze.bean;

import com.example.colze.bean.DanTiBean.Head;
import com.example.colze.bean.DanTiBean.Item;
import com.example.colze.bean.TaoTiBean.Body;

import java.io.Serializable;

public class ErrTableBean implements Serializable {
   public String id;
   public int difficulty;
   public String source;
   public String testCenter;
   public String time;
   
   /**
    * 当前错误题目索引
    */
   public int currentIndex;
   /**
    * 当前错误题在试卷中的索引
    */
   public int qusPosition;
   /**
    * 所在试卷路劲
    */
   public String examPath;
   /**
    * 试卷Lesson
    */
   public String examLesson;
   public Body body;// 试卷整体
   public Item item;// 题目详解
   public Head head;// 题目内容
}
