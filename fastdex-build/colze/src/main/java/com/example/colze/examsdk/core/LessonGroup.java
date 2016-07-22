package com.example.colze.examsdk.core;



import com.example.colze.examsdk.ExamSDK;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tong on 15/11/18.
 * 课程组
 */
public class LessonGroup implements Serializable {
    /**
     * 宿主文件相对路径
     */
    private String location;
    /**
     * 课程组id
     */
    private String id;

    private int size;

    private List<Lesson> lessons;

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public LessonGroup(String location, JSONObject obj) {
        this.size = obj.length();
        ExamSDK.d("LessonGroup init location: " + location + " size:" + size);

        /*  examInOne/S13_1_0001.json  */
        this.location = location;
        /*  S13_1_0001.json */
        id = location.substring(location.lastIndexOf("/") + 1);

        List<Lesson> lessonList = new ArrayList<Lesson>();
        for (int i = 1;i <= this.size;i++) {
            String lessionId = "Lesson" + i;
            JSONObject lessonObj = obj.optJSONObject(lessionId);
            if (lessonObj != null) {
                lessonList.add(new Lesson(location,lessionId,lessonObj));
            }
        }
        this.lessons = lessonList;
    }
}
