package com.example.colze.examsdk.core;



import com.example.colze.examsdk.ExamSDK;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tong on 15/11/19.
 */
public class ExamUtil {
    public static JSONObject getJSONObject(File file) {
        try {
            return (JSONObject)new JSONTokener(getContent(file)).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getContent(File file) {
        try {
            ExamSDK.d("load file: " + file.getAbsolutePath());
            return new String(readStream(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer,0,len);
        }
        return bos.toByteArray();
    }
}
