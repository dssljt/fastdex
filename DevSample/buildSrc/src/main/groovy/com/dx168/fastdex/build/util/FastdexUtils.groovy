package com.dx168.fastdex.build.util

import org.gradle.api.Project

/**
 * Created by tong on 17/3/14.
 */
public class FastdexUtils {
    public static final File getBuildDir(Project project) {
        File file = new File(project.getBuildDir(),Constant.FASTDEX_BUILD_DIR);
        return file;
    }

    public static final File getBuildDir(Project project,String variantName) {
        File file = new File(getBuildDir(project),variantName);
        return file;
    }

    public static final File getDexCacheDir(Project project,String variantName) {
        File file = new File(getBuildDir(project,variantName),Constant.FASTDEX_DEX_CACHE_DIR);
        return file;
    }

    public static boolean hasValidCache(Project project,String variantName) {
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

    public static boolean cleanCache(Project project,String variantName) {
        return FileUtils.deleteDir(getBuildDir(project,variantName))
    }

    public static boolean cleanAllCache(Project project,String variantName) {
        return FileUtils.deleteDir(getBuildDir(project))
    }

    public static File getCachedResourceMappingFile(Project project,String variantName) {
        File resourceMappingFile = new File(getBuildDir(project,variantName),Constant.R_TXT)
        return resourceMappingFile
    }

    public static File getCachedDependListFile(Project project,String variantName) {
        File cachedDependListFile = new File(getBuildDir(project,variantName),Constant.DEPENDENCIES_MAPPING_FILENAME)
        return cachedDependListFile
    }

    public static File getInjectedJarFile(Project project,String variantName) {
        File injectedJarFile = new File(getBuildDir(project,variantName),Constant.INJECTED_JAR_FILENAME)
        return injectedJarFile
    }

    public static File getCustomJavacTaskOutputFile(Project project,String variantName) {
        File injectedJarFile = new File(getBuildDir(project,variantName),Constant.CUSTOM_JAVAC_JAR_FILENAME)
        return injectedJarFile
    }

    public static Set<String> scanChangedClasses(Project project,String variantName,String manifestPath) {
        String[] srcDirs = project.android.sourceSets.main.java.srcDirs
        File snapshootDir = new File(getBuildDir(project,variantName),Constant.FASTDEX_SNAPSHOOT_DIR)
        Set<String> changedJavaClassNames = new HashSet<>()
        for (String srcDir : srcDirs) {
            File newDir = new File(srcDir)
            File oldDir = new File(snapshootDir,srcDir)

            Set<JavaDirDiff.DiffInfo> set = JavaDirDiff.diff(newDir,oldDir,true,project.logger)

            for (JavaDirDiff.DiffInfo diff : set) {
                String str = diff.relativePath
                str = str.substring(0,str.length() - ".java".length())
                str = "${str}.class"
                changedJavaClassNames.add(str)
            }
        }
        changedJavaClassNames.add(GradleUtils.getBuildConfigClassName(manifestPath))
        return changedJavaClassNames
    }
}
