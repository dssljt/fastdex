package com.example.colze.examsdk.core;

import com.example.colze.examsdk.ExamSDK;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tong on 15/11/18.
 * 题目信息
 */
public class Subject implements Serializable {
    /**
     * 宿主文件相对路径
     */
    private String location;

    /**
     * 在父节点的id
     */
    private String id;

    /**
     * 问题描述
     */
    private String sub;

    /**
     * 选择题的选项
     */
    private List<String> options = new ArrayList<String>();

    /**
     * 正确答案
     */
    private String answer;

    /**
     * 题目类型
     */
    private SubjecType type;

    /**
     * 词汇
     */
    private String word;

    /**
     * 解析
     */
    private String content;

    /**
     * 技巧
     */
    private String skill;

    /**
     * 详解
     */
    private String point;

    public Subject(String location, String subjectId, SubjecType subjecType, JSONObject subjectObj) {
        this.location = location;
        this.id = subjectId;
        this.type = subjecType;

        JSONObject bodyObj = subjectObj.optJSONObject("Body");
        JSONObject keyObj = subjectObj.optJSONObject("Key");

        this.sub = bodyObj.optString("Sub");
        for (int i = 0;i < SubjecType.getMaxOptions();i++) {
            String value = bodyObj.optString("An" + (i + 1));
            options.add(value != null ? value : "");
        }

        this.answer = keyObj.optString("Ans");
        this.word = keyObj.optString("Word");
        this.content = keyObj.optString("Content");
        this.skill = keyObj.optString("Skill");
        this.point = keyObj.optString("Point");

        ExamSDK.d("Subject init location: " + location + " id: " + id + " obj: " + subjectObj);

//        "Q1": {
//            "Body": {
//                "Sub": "",
//                        "An1": "with",
//                        "An2": "in",
//                        "An3": "at",
//                        "An4": "for"
//            },
//            "Key": {
//                "Ans": "D",
//                        "Word": "memorynn【注释】nnn.记忆(力)；回忆；纪念nn【词形变化】nn复数:memoriesnnb) 形容词:memorial nnc) 名词:memorial nnd) 动词: memorizenn【词组】nnin memory of (纪念)；from memory (凭记忆)；have a bad/good memory (记性不好/好)；in living memory (在人们的记忆里)；lose one’s memory (失去记忆)；jog sb’s memory (唤起某人的记忆)",
//                        "Content": "【原文】A good memory is a great help (for) learning a language.nn【翻译】好的记忆力对学习语言有很大的帮助。nn【解析】with意为“和……一起，带有……”；in 表地点时，意为“在……里面”；表时间时，意为“一段时间或与年、月、季节时间连用”；at表地点时，“在……”；表时间时，指“在时间上的某一时刻”；for 意为“对，适合于”。故选D。",
//                        "Skill": "动词在搭配关系上与名词、介词、副词的用法紧密相关。解决这类题目要求考生多读、多记，对所学词语或固定搭配牢固掌握，并能灵活运用。",
//                        "Point": "本题考查介词的用法.nnfor 的习惯用法包括:nn① 表示“当作、作为”。nn例:I like some bread and milk for breakfast. （我喜欢把面包和牛奶作为早餐。）nn② 表示理由或原因,意为“因为、由于”。nn例:Thank you for helping me with my English. （谢谢你帮我学习英语。）nn③ 表示动作的对象或接受者,意为“给……”、“对…… (而言)”。\nn例:Let me pick it up for you. （让我为你捡起来。）nn④ 表示时间、距离,意为“计、达”，for+时间段\n例:I usually do the running for an hour in the morning. （我早晨通常跑步一小时。）nn⑤ 表示去向、目的,意为“向、往、取、买”等。nn例:Let’s go for a walk. （我们出去散步吧。）nn⑥ 用于一些固定搭配中。\n例1:Who are you waiting for? （你在等谁?）\n例2:For example, Mr. Green is a kind teacher. （比如,格林先生是一位心地善良的老师。）",
//                        "S1": "1",
//                        "S2": "0",
//                        "S3": "0",
//                        "S4": "0",
//                        "S5": "0",
//                        "Frequency": ""
//            }
//        }
    }


    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getSub() {
        return sub;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public SubjecType getType() {
        return type;
    }

    public String getWord() {
        return word;
    }

    public String getContent() {
        return content;
    }

    public String getSkill() {
        return skill;
    }

    public String getPoint() {
        return point;
    }
}
