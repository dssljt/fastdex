package com.dx168.fastdex.build;

/**
 * Created by tong on 17/3/14.
 */
public interface Constant {
    /**
     * 最低支持的android gradle build版本
     */
    public static final String MIN_SUPPORT_ANDROID_GRADLE_VERSION = "2.0.0"
    public static final String FASTDEX_BUILD_DIR = "fastdex"
    public static final String FASTDEX_DEX_CACHE_DIR = "dex_cache"
    public static final String FASTDEX_SNAPSHOOT_DIR = "snapshoot"
    public static final String INJECTED_JAR_FILENAME = "injected-combined.jar"
    public static final String JAVA_SUFFIX = ".java"
    public static final String CLASS_SUFFIX = ".class"
    public static final String DEX_SUFFIX = ".dex"
    public static final String R_TXT = "R.txt"
    public static final String RUNTIME_DEX_FILENAME = "fastdex-runtime.dex"
    public static final String DEPENDENCIES_MAPPING_FILENAME = "dependencies-mapping.txt"
}
