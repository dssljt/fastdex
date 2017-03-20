package com.dx168.fastdex.build.util

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project

import java.security.MessageDigest

/**
 * Created by tong on 17/3/14.
 */
public class FastdexUtils {
    /**
     * 获取fastdex的build目录
     * @param project
     * @return
     */
    public static final File getBuildDir(Project project) {
        File file = new File(project.getBuildDir(),Constant.FASTDEX_BUILD_DIR);
        return file;
    }

    /**
     * 获取fastdex指定variantName的build目录
     * @param project
     * @return
     */
    public static final File getBuildDir(Project project,String variantName) {
        File file = new File(getBuildDir(project),variantName);
        return file;
    }

    /**
     * 获取fastdex指定variantName的dex缓存目录
     * @param project
     * @return
     */
    public static final File getDexCacheDir(Project project,String variantName) {
        File file = new File(getBuildDir(project,variantName),Constant.FASTDEX_DEX_CACHE_DIR);
        return file;
    }

    /**
     * 把第一个字母变成小写
     * @param str
     * @return
     */
    public static final String firstCharToLowerCase(String str) {
        if (str == null || str.length() == 0) {
            return ""
        }
        return str.substring(0,1).toLowerCase() + str.substring(1)
    }

    /**
     * 是否存在dex缓存
     * @param project
     * @param variantName
     * @return
     */
    public static boolean hasDexCache(Project project, String variantName) {
        File cacheDexDir = getDexCacheDir(project,variantName)
        if (!FileUtils.dirExists(cacheDexDir.getAbsolutePath())) {
            return false;
        }

        //check dex
        boolean result = false
        for (File file : cacheDexDir.listFiles()) {
            if (file.getName().endsWith(Constant.DEX_SUFFIX)) {
                result = true
                break
            }
        }
        //check R.txt
        return result
    }

    /**
     * 清空所有缓存
     * @param project
     * @param variantName
     * @return
     */
    public static boolean cleanCache(Project project,String variantName) {
        File dir = getBuildDir(project,variantName)
        project.logger.error("==fastdex clean dir: ${dir}")
        return FileUtils.deleteDir(dir)
    }

    /**
     * 清空指定variantName缓存
     * @param project
     * @param variantName
     * @return
     */
    public static boolean cleanAllCache(Project project) {
        File dir = getBuildDir(project)
        project.logger.error("==fastdex clean dir: ${dir}")
        return FileUtils.deleteDir(dir)
    }

    /**
     * 获取资源映射文件
     * @param project
     * @param variantName
     * @return
     */
    public static File getCachedResourceMappingFile(Project project,String variantName) {
        File resourceMappingFile = new File(getBuildDir(project,variantName),Constant.R_TXT)
        return resourceMappingFile
    }

    /**
     * 获取全量打包时的依赖列表
     * @param project
     * @param variantName
     * @return
     */
    public static File getCachedDependListFile(Project project,String variantName) {
        File cachedDependListFile = new File(getBuildDir(project,variantName),Constant.DEPENDENCIES_MAPPING_FILENAME)
        return cachedDependListFile
    }

    /**
     * 获取全量打包时的包括所有代码的jar包
     * @param project
     * @param variantName
     * @return
     */
    public static File getInjectedJarFile(Project project,String variantName) {
        File injectedJarFile = new File(getBuildDir(project,variantName),Constant.INJECTED_JAR_FILENAME)
        return injectedJarFile
    }

    /**
     * 获取自定义的java compile任务输出的jar文件
     * @param project
     * @param variantName
     * @return
     */
    public static File getCustomJavacTaskOutputFile(Project project,String variantName) {
        File injectedJarFile = new File(getBuildDir(project,variantName),Constant.CUSTOM_JAVAC_JAR_FILENAME)
        return injectedJarFile
    }

    /**
     * 补丁打包时扫描那些java文件发生了变化
     * @param project
     * @param variantName
     * @param manifestPath
     * @return
     */
    public static Set<String> scanChangedClasses(Project project,String variantName,String manifestPath) {
        String[] srcDirs = project.android.sourceSets.main.java.srcDirs
        File snapshootDir = new File(getBuildDir(project,variantName),Constant.FASTDEX_SNAPSHOOT_DIR)
        Set<String> changedJavaClassNames = new HashSet<>()
        for (String srcDir : srcDirs) {
            File newDir = new File(srcDir)
            File oldDir = new File(snapshootDir,fixSourceSetDir(srcDir))

            Set<JavaDirDiff.DiffInfo> set = JavaDirDiff.diff(newDir,oldDir,true,project.logger)

            for (JavaDirDiff.DiffInfo diff : set) {
                String str = diff.relativePath
                str = str.substring(0,str.length() - ".java".length())
                str = "${str}.class"
                changedJavaClassNames.add(str)
            }
        }
        changedJavaClassNames.add(GradleUtils.getBuildConfigRelativePath(manifestPath))
        return changedJavaClassNames
    }

    public static String fixSourceSetDir(String srcDir) {
        if (srcDir == null || srcDir.length() == 0) {
            return srcDir
        }
//        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
//            return MessageDigest.getInstance("MD5").digest(srcDir.bytes).encodeHex().toString()
//        }
//        return srcDir
        return MessageDigest.getInstance("MD5").digest(srcDir.bytes).encodeHex().toString()
    }
}
