package com.example.colze.examsdk.core;


import com.example.colze.examsdk.ExamProperties;
import com.example.colze.utils.Logger;

import java.io.File;

/**
 * Created by tong on 15/11/18.
 * 可配置的路径策略
 */
public class ConfigurablePathStrategy implements PathStrategy {
    private ExamProperties properties;
    private File rootPath;
    private String subjectRelative;

    public ConfigurablePathStrategy(ExamProperties properties) {
        this.properties = properties;

        loadConfig();
    }

    private void loadConfig() {
        String path = (String)properties.get(ExamProperties.KEY_ROOT_PATH);
        Logger.d("<<< Root path: " + path);
        if (path == null) {
            throw new IllegalArgumentException("你配置rootPath这个选项了吗");
        }
        rootPath = new File(path);
        if (!rootPath.exists() || !rootPath.isDirectory()) {
            throw new IllegalStateException("你配置了一个无效的资源文件路径! " + rootPath.getAbsolutePath());
        }

        String sRelative = (String)properties.get(ExamProperties.KEY_SUBJECT_RELATIVE);
        if (sRelative == null) {
            sRelative = "subject/${lessonNo}";
        }
        if (!sRelative.contains("${lessonNo}")) {
            throw new IllegalArgumentException("单题的相对路径配置需要包含${lessonNo}!");
        }
        this.subjectRelative = sRelative;
        Logger.d("<<< subjectRelative: " + subjectRelative);
    }

    public ExamProperties getProperties() {
        return properties;
    }

    @Override
    public File getRootPath() {
        return rootPath;
    }

    @Override
    public String getSubjectRelativePath(String lessonNo) {
        return this.subjectRelative.replace("${lessonNo}",lessonNo);
    }
}
