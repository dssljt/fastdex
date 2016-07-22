package com.example.colze.examsdk.core;

import com.example.colze.examsdk.ExamSDK;

import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tong on 15/11/18.
 * 题目组
 */
public class SubjectGroup implements Serializable {
    public static final int RESOURCE_TYPE_TXT = 1;

    /**
     * 宿主文件相对路径
     */
    private String location;

    private String id;

    private String type1;
    private int type2;

    /**
     * 题目数量
     */
    private int ansQues;

    /**
     * 文本文件名
     */
    private String txt;

    /**
     * 图片文件名
     */
    private String pic;

    /**
     * 题目类型
     */
    private SubjecType subjecType;

    private List<Subject> subjects = new ArrayList<Subject>();


    public String getLocation() {
        return location;
    }

    public SubjectGroup(String subjectPath, String subjectGroupId, JSONObject subjectGroupObj) {
        //        "T": {
//            "Head": {
//                "Part": "完型填空",
//                        "Type1": "TXT",
//                        "Type2": "4",
//                        "Rem": "REM",
//                        "AnsQues": "10",
//                        "Txt": "Q13_1_2_000001.txt",
//                        "Pic": "",
//                        "Sound": ""
//            },
//            "Q1": {
//                "Body": {
//                    "Sub": "",
//                            "An1": "with",
//                            "An2": "in",
//                            "An3": "at",
//                            "An4": "for"
//                },
//                "Key": {
//                    "Ans": "D",
//                            "Word": "memorynn【注释】nnn.记忆(力)；回忆；纪念nn【词形变化】nn复数:memoriesnnb) 形容词:memorial nnc) 名词:memorial nnd) 动词: memorizenn【词组】nnin memory of (纪念)；from memory (凭记忆)；have a bad/good memory (记性不好/好)；in living memory (在人们的记忆里)；lose one’s memory (失去记忆)；jog sb’s memory (唤起某人的记忆)",
//                            "Content": "【原文】A good memory is a great help (for) learning a language.nn【翻译】好的记忆力对学习语言有很大的帮助。nn【解析】with意为“和……一起，带有……”；in 表地点时，意为“在……里面”；表时间时，意为“一段时间或与年、月、季节时间连用”；at表地点时，“在……”；表时间时，指“在时间上的某一时刻”；for 意为“对，适合于”。故选D。",
//                            "Skill": "动词在搭配关系上与名词、介词、副词的用法紧密相关。解决这类题目要求考生多读、多记，对所学词语或固定搭配牢固掌握，并能灵活运用。",
//                            "Point": "本题考查介词的用法.nnfor 的习惯用法包括:nn① 表示“当作、作为”。nn例:I like some bread and milk for breakfast. （我喜欢把面包和牛奶作为早餐。）nn② 表示理由或原因,意为“因为、由于”。nn例:Thank you for helping me with my English. （谢谢你帮我学习英语。）nn③ 表示动作的对象或接受者,意为“给……”、“对…… (而言)”。\nn例:Let me pick it up for you. （让我为你捡起来。）nn④ 表示时间、距离,意为“计、达”，for+时间段\n例:I usually do the running for an hour in the morning. （我早晨通常跑步一小时。）nn⑤ 表示去向、目的,意为“向、往、取、买”等。nn例:Let’s go for a walk. （我们出去散步吧。）nn⑥ 用于一些固定搭配中。\n例1:Who are you waiting for? （你在等谁?）\n例2:For example, Mr. Green is a kind teacher. （比如,格林先生是一位心地善良的老师。）",
//                            "S1": "1",
//                            "S2": "0",
//                            "S3": "0",
//                            "S4": "0",
//                            "S5": "0",
//                            "Frequency": ""
//                }
        //subjectUrls   =  [Q13_1_2_000001_Q1,Q13_1_2_000001_Q2, .......]

        if (!subjectPath.endsWith("/")) {
            subjectPath = subjectPath + "/";
        }
        subjectGroupObj = subjectGroupObj.optJSONObject("T");
        this.location = subjectPath;
        this.id = subjectGroupId;

        JSONObject headObj = subjectGroupObj.optJSONObject("Head");
        this.type1 = headObj.optString("Type1");
        this.type2 = headObj.optInt("Type2");
        this.txt = headObj.optString("Txt");
        this.pic = headObj.optString("Pic");
        this.ansQues = headObj.optInt("AnsQues");

        ExamSDK.d("SubjectGroup init location: " + location + " id: " + id + " size: " + ansQues);

        this.subjecType = SubjecType.valueOf(this.type2);
        for (int i = 0,size = subjectGroupObj.length() - 1;i < size - 1;i ++) {
            String subjectId = "Q" + (i + 1);
            subjects.add(new Subject(location,subjectId,this.subjecType,subjectGroupObj.optJSONObject(subjectId)));
        }
    }

    public String getType1() {
        return type1;
    }

    public int getType2() {
        return type2;
    }

    public int getAnsQues() {
        return ansQues;
    }

    public int getSize() {
        return ansQues;
    }

    public String getTxt() {
        return txt;
    }

    public String getPic() {
        return pic;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public SubjecType getSubjecType() {
        return subjecType;
    }
}
