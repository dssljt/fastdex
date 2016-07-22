package com.example.colze.examsdk.core;

import com.example.colze.examsdk.ExamSDK;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;

/**
 * Created by tong on 15/11/18.
 * 课程信息
 */
public class Lesson implements Serializable {
    public static final String ID_PERFIX = "Lesson";

    /**
     * 宿主文件相对路径
     */
    private String location;

    /**
     * 在父节点的id
     */
    private String id;

    /**
     * 在父节点的索引
     */
    private final int position;

    /**
     * 课程名字
     */
    private String name;

    /**
     * 测试时间，单位秒
     */
    private int testTime;

    /**
     * 及格分数
     */
    private int passScore;

    /**
     * 题目总数量
     */
    private int numQ;

    /**
     * 子题目
     */
    private SoftReference<SubjectGroup> subjectGroupSoftReference;

    private Difficulty difficulty;

//    /**
//     * 懒加载单题时用到
//     */
//    private String[] subjectUrls;

    /**
     * 题组id
     */
    private String subjectGroupId;

    public Lesson(String location, String lessonId, JSONObject lessonObj) {
//        "Lesson1": {
//            "Head": {
//                "Name": "language",
//                        "About": "",
//                        "NumQ": "10",
//                        "TestTime": "900",
//                        "PassScore": "60",
//                        "Difficulty": "3"
//            },
//            "Body": {
//                "Q1": "Q13_1_2_000001_Q1",
//                        "Q2": "Q13_1_2_000001_Q2",
//                        "Q3": "Q13_1_2_000001_Q3",
//                        "Q4": "Q13_1_2_000001_Q4",
//                        "Q5": "Q13_1_2_000001_Q5",
//                        "Q6": "Q13_1_2_000001_Q6",
//                        "Q7": "Q13_1_2_000001_Q7",
//                        "Q8": "Q13_1_2_000001_Q8",
//                        "Q9": "Q13_1_2_000001_Q9",
//                        "Q10": "Q13_1_2_000001_Q10"
//            }
//        }

        ExamSDK.d("Lesson init location: " + location + " id: " + lessonId + " obj: " + lessonObj);

        /*  examInOne/S13_1_0001.json  */
        this.location = location;
        /*  Lession1 */
        this.id = lessonId;
        this.position = Integer.valueOf(lessonId.replace(ID_PERFIX,""));

        JSONObject headObj = lessonObj.optJSONObject("Head");
        JSONObject bodyObj = lessonObj.optJSONObject("Body");

        this.name = headObj.optString("Name");
        //this.about = headObj.optString("About");
        this.testTime = headObj.optInt("TestTime");
        this.passScore = headObj.optInt("PassScore");
        this.difficulty = Difficulty.valueOf(headObj.optInt("Difficulty"));

        this.numQ = headObj.optInt("NumQ");

        String q1 = bodyObj.optString("Q1");
        subjectGroupId = q1.substring(0,q1.lastIndexOf("_"));
    }

    public SubjectGroup getSubjectGroup() {
//        if (subjectGroupSoftReference == null
//                || subjectGroupSoftReference.get() == null) {
//            String subjectPath = ExamSDK.getPathStrategy().getSubjectRelativePath(String.valueOf(position));
//
//            File file = new File(ExamSDK.getPathStrategy().getRootPath(), subjectPath + "/" + subjectGroupId + ".json");
//            JSONObject subjectGroupObj = ExamUtil.getJSONObject(file);
//
//            SubjectGroup subjectGroup = new SubjectGroup(subjectPath,subjectGroupId,subjectGroupObj);
//            subjectGroupSoftReference = new SoftReference<SubjectGroup>(subjectGroup);
//        }
//        return subjectGroupSoftReference.get();

        String subjectPath = ExamSDK.getPathStrategy().getSubjectRelativePath(String.valueOf(position));

        File file = new File(ExamSDK.getPathStrategy().getRootPath(), subjectPath + "/" + subjectGroupId + ".json");
        JSONObject subjectGroupObj = ExamUtil.getJSONObject(file);

        SubjectGroup subjectGroup = new SubjectGroup(subjectPath,subjectGroupId,subjectGroupObj);

        return subjectGroup;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getTestTime() {
        return testTime;
    }

    public int getPassScore() {
        return passScore;
    }

    public int getNumQ() {
        return numQ;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getSubjectGroupId() {
        return subjectGroupId;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "location='" + location + '\'' +
                ", id='" + id + '\'' +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", testTime=" + testTime +
                ", passScore=" + passScore +
                ", numQ=" + numQ +
                ", subjectGroupSoftReference=" + subjectGroupSoftReference +
                ", difficulty=" + difficulty +
                ", subjectGroupId='" + subjectGroupId + '\'' +
                '}';
    }
}
