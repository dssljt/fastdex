package com.dx168.fastdex.build.util

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.security.MessageDigest
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import com.dx168.fastdex.build.lib.Constant

/**
 * Created by tong on 17/3/14.
 */
public class FastdexUtils {
    public static final String getSdkDirectory(Project project) {
        String sdkDirectory = project.android.getSdkDirectory()
        if (sdkDirectory.contains("\\")) {
            sdkDirectory = sdkDirectory.replace("\\", "/");
        }
        return sdkDirectory
    }
    public static final String getDxCmdPath(Project project) {
        File dx = new File(FastdexUtils.getSdkDirectory(project),"build-tools${File.separator}${project.android.getBuildToolsVersion()}${File.separator}dx")
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            return "${dx.absolutePath}.bat"
        }
        return dx.getAbsolutePath()
    }

    /**
     * 获取fastdex的build目录
     * @param project
     * @return
     */
    public static final File getBuildDir(Project project) {
        File file = new File(project.getBuildDir(),Constant.BUILD_DIR);
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
        File file = new File(getBuildDir(project,variantName),Constant.DEX_CACHE_DIR);
        return file;
    }

    /**
     * 获取fastdex指定variantName的dex缓存目录
     * @param project
     * @return
     */
    public static final File getSourceSetFile(Project project,String variantName) {
        File file = new File(getBuildDir(project,variantName),Constant.SOURCESET_SNAPSHOOT_FILENAME);
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

    public static File getDiffResultSetFile(Project project,String variantName) {
        File diffResultFile = new File(getBuildDir(project,variantName),Constant.LAST_DIFF_RESULT_SET_FILENAME)
        return diffResultFile
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
     * 补丁打包时扫描那些java文件发生了变化
     * @param project
     * @param variantName
     * @param manifestPath
     * @return
     */
    public static Set<String> getChangedClassPatterns(Project project,String variantName,String manifestPath) {
        String[] srcDirs = project.android.sourceSets.main.java.srcDirs
        File snapshootDir = new File(getBuildDir(project,variantName),Constant.SNAPSHOOT_DIR)
        Set<String> changedJavaClassNames = new HashSet<>()
        for (String srcDir : srcDirs) {
            File newDir = new File(srcDir)
            File oldDir = new File(snapshootDir,fixSourceSetDir(srcDir))

            Set<JavaDirDiff.DiffInfo> set = JavaDirDiff.diff(newDir,oldDir,true,project.logger)

            for (JavaDirDiff.DiffInfo diff : set) {
                //假如MainActivity发生变化，生成的class
                //包括MainActivity.class  MainActivity$1.class MainActivity$2.class ...
                //如果依赖的有butterknife,还会动态生成MainActivity$$ViewBinder.class，所以尽量别使用这玩意，打包会很慢的

                String className = diff.relativePath
                //className = com/dx168/fastdex/sample/MainActivity.java || com\\dx168\\fastdex\\sample\\MainActivity.java
                //防止windows路径出问题
                if (className.contains("\\")) {
                    className = className.replace("\\", "/");
                }

                className = className.substring(0,className.length() - Constant.JAVA_SUFFIX.length())
                changedJavaClassNames.add("${className}${Constant.CLASS_SUFFIX}")
                changedJavaClassNames.add("${className}\\\$\\S{0,}${Constant.CLASS_SUFFIX}")}
        }
        changedJavaClassNames.add(GradleUtils.getBuildConfigRelativePath(manifestPath))
        return changedJavaClassNames
    }

    /**
     * 获取所有编译的class存放目录
     * @param invocation
     * @return
     */
    public static Set<File> getDirectoryInputFiles(TransformInvocation invocation) {
        Set<File> dirClasspaths = new HashSet<>();
        for (TransformInput input : invocation.getInputs()) {
            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs()
            if (directoryInputs != null) {
                for (DirectoryInput directoryInput : directoryInputs) {
                    dirClasspaths.add(directoryInput.getFile())
                }
            }
        }

        return dirClasspaths
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

    /**
     * 生成补丁jar,仅把变化部分参与jar的生成
     * @param project
     * @param directoryInputFiles
     * @param outputJar
     * @param changedClassPatterns
     * @throws IOException
     */
    public static void generatePatchJar(Project project,Set<File> directoryInputFiles, File patchJar, Set<String> changedClassPatterns) throws IOException {
        project.logger.error("==fastdex generate patch jar start")
        if (project.fastdex.debug) {
            project.logger.error("==fastdex debug changedClassPatterns: ${changedClassPatterns}")
        }

        long start = System.currentTimeMillis()
        if (directoryInputFiles == null || directoryInputFiles.isEmpty()) {
            throw new IllegalArgumentException("DirClasspaths can not be null!!")
        }
        if (changedClassPatterns == null || changedClassPatterns.isEmpty()) {
            throw new IllegalArgumentException("DirClasspaths can not be null!!")
        }

        FileUtils.deleteFile(patchJar)

        Set<Pattern> patterns = new HashSet<>()
        if (changedClassPatterns != null && !changedClassPatterns.isEmpty()) {
            for (String patternStr : changedClassPatterns) {
                patterns.add(Pattern.compile(patternStr))
            }
        }

        ZipOutputStream outputJarStream = new ZipOutputStream(new FileOutputStream(patchJar));
        try {
            for (File classpathFile : directoryInputFiles) {
                Path classpath = classpathFile.toPath()
                Files.walkFileTree(classpath,new SimpleFileVisitor<Path>(){
                    @Override
                    FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!file.toFile().getName().endsWith(Constant.CLASS_SUFFIX)) {
                            return FileVisitResult.CONTINUE;
                        }
                        Path relativePath = classpath.relativize(file)
                        String className = relativePath.toString()
                        //防止windows路径出问题
                        if (className.contains("\\")) {
                            className = className.replace("\\", "/");
                        }
                        for (Pattern pattern : patterns) {
                            if (pattern.matcher(className).matches()) {
                                ZipEntry e = new ZipEntry(className)
                                outputJarStream.putNextEntry(e)

                                if (project.fastdex.debug) {
                                    project.logger.error("==fastdex add entry: ${e}")
                                }
                                byte[] bytes = FileUtils.readContents(file.toFile())
                                outputJarStream.write(bytes,0,bytes.length)
                                outputJarStream.closeEntry()
                                break;
                            }
                        }
                        return FileVisitResult.CONTINUE
                    }
                })
            }

        } finally {
            if (outputJarStream != null) {
                outputJarStream.close();
            }
        }

        if (!FileUtils.isLegalFile(patchJar)) {
            throw new GradleException("==fastdex generate patch jar fail: ${patchJar}")
        }
        long end = System.currentTimeMillis();
        project.logger.error("==fastdex generate patch jar complete: ${patchJar} use: ${end - start}ms")
    }
}
