package com.example.colze.examsdk;

import android.content.Context;
import android.util.Log;

import com.example.colze.Config;
import com.example.colze.examsdk.core.ConfigurablePathStrategy;
import com.example.colze.examsdk.core.ExamUtil;
import com.example.colze.examsdk.core.Lesson;
import com.example.colze.examsdk.core.LessonGroup;
import com.example.colze.examsdk.core.PathStrategy;
import com.example.colze.examsdk.core.SubjectGroup;
import com.example.colze.utils.AllContacts;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tong on 15/11/18.
 */
public class ExamSDK {
    private static final String TAG = ExamSDK.class.getSimpleName();
    public static boolean debug = true;

    private static PathStrategy pathStrategy;
    private static final Map<Object,SoftReference> cacheMap = new ConcurrentHashMap<Object,SoftReference>();
    private static ExamProperties properties;

    /**
     * 初始化
     * @param context
     */
    public static synchronized void init(Context context) {
        ExamProperties defaultProperties = new ExamProperties();
        defaultProperties.put(ExamProperties.KEY_ROOT_PATH, AllContacts.sdcard);

        defaultProperties.put(ExamProperties.KEY_SUBJECT_RELATIVE,"subject/${lessonNo}");

        defaultProperties.put(ExamProperties.KEY_DEFAULT_LESSION,"examInOne/" + Config.initJson + ".json");

        ExamSDK.properties = defaultProperties;

        d(properties);
        pathStrategy = new ConfigurablePathStrategy(properties);
    }

    /**
     * 获取指定位置的套题
     * @param location
     * @return
     */
    public static LessonGroup getLessionGroup(String location) {
        d("getLessionGroup: " + location);
//        SoftReference reference = cacheMap.get(location);
//        LessonGroup lessonGroup = null;
//        if (reference == null || reference.get() == null) {
//            File file = new File(pathStrategy.getRootPath(), location);
//            JSONObject obj = ExamUtil.getJSONObject(file);
//            lessonGroup = new LessonGroup(location, obj);
//            cacheMap.put(location, new SoftReference(obj));
//        } else {
//            lessonGroup = (LessonGroup) reference.get();
//        }
//        return lessonGroup;

        File file = new File(pathStrategy.getRootPath(), location);
        JSONObject obj = ExamUtil.getJSONObject(file);
        return  new LessonGroup(location, obj);
    }

    /**
     * 获取默认课程组信息
     * @return
     */
    public static LessonGroup getDefaultLessonGroup() {
        return getLessionGroup(properties.getProperty(ExamProperties.KEY_DEFAULT_LESSION));
    }

    /**
     * 获取课程
     * @param lessonId
     * @return
     */
    public static Lesson getLession(String lessonId) {
        d("getLession: " + lessonId);
        LessonGroup lessonGroup = getDefaultLessonGroup();
        for (Lesson lesson : lessonGroup.getLessons()) {
            if (lesson.getId().equals(lessonId)) {
                return lesson;
            }
        }
        return null;
    }

    /**
     * 获取单题
     * @return
     */
    public static SubjectGroup getSubjectGroupByLessonId(String lessonId) {
        d("getSubjectGroupByLessonId: " + lessonId);
        Lesson lesson = getLession(lessonId);
        if (lesson != null) {
            lesson.getSubjectGroup();
        }
        return null;
    }

    /**
     * 获取单题
     * @return
     */
    public static SubjectGroup getSubjectGroup(String subjectGroupId) {
        d("getSubjectGroup: " + subjectGroupId);

        LessonGroup lessonGroup = getDefaultLessonGroup();
        for (Lesson lesson : lessonGroup.getLessons()) {
            if (lesson.getSubjectGroupId().equals(subjectGroupId)) {
                return lesson.getSubjectGroup();
            }
        }
        return null;
    }

    public static PathStrategy getPathStrategy() {
        return pathStrategy;
    }

    private static void put2cache(String key,Object obj) {

    }

    private static Object getfromcache(String key) {
        return null;
    }

    public static void d(String tag,Object obj) {
        if (debug) {
            String content = "";
            if (obj instanceof String) {
                content = (String)obj;
            } else if (obj != null) {
                content = obj.toString();
            }
            Log.d(tag,content);
        }
    }

    public static void d(Object obj) {
        ExamSDK.d(">>" + TAG + "<<",obj);
    }
}
