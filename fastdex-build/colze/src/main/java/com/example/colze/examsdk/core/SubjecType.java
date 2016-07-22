package com.example.colze.examsdk.core;

/**
 * Created by tong on 15/11/19.
 * 题目类型
 */
public enum SubjecType {
    colze(1,"填空"),one_of_three(3,"三选一"),one_of_four(4,"四选一");

    private int code;
    private String desc;

    SubjecType(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public static SubjecType valueOf(int code) {
        for (SubjecType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取选项最多的题目的选项值
     * @return
     */
    public static int getMaxOptions() {
        return 4;
    }
}
