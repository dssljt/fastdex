package com.example.colze.examsdk.core;

import java.io.File;

/**
 * Created by tong on 15/11/18.
 * 资源路径相关
 */
public interface PathStrategy {
    /**
     * 获取资源文件夹路径
     * @return
     */
    File getRootPath();

    /**
     * 获取题目相关文件的相对路径
     * @param lessonNo
     * @return
     */
    String getSubjectRelativePath(String lessonNo);
}
