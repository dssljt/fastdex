package com.example.colze.examsdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tong on 15/11/18.
 */
public class ExamProperties extends Properties {
    public static final String KEY_ROOT_PATH = "rootPath";
    public static final String KEY_SUBJECT_RELATIVE = "subjectRelative";
    public static final String KEY_DEFAULT_LESSION = "defaultLession";

    public ExamProperties() {
    }

    public ExamProperties(InputStream is) {
        try {
            load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
