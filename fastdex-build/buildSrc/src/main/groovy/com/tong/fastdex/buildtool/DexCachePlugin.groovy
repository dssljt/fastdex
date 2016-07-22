package com.tong.fastdex.buildtool

import com.tong.fastdex.buildtool.utils.FastDexUtils
import com.tong.fastdex.buildtool.utils.FolderComparator
import com.tong.fastdex.buildtool.utils.FolderComparatorFactory;
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState
import org.gradle.api.tasks.Delete

/**
 * 缓存dex,hook android打包流程并应用已缓存的dex
 * Created by tong on 16/7/13.
 */
class DexCachePlugin implements Plugin<Project>,TaskExecutionListener  {
    public static final String PROJECT_NAME = "fastdex"
    public static final String CACHE_DIR_NAME = 'build-' + PROJECT_NAME
    public static final boolean DEBUG = false;

    private File cacheDir;
    private Project appProject;

    File getCacheDir() {
        return cacheDir
    }

    void dlog(String msg) {
        if (DEBUG) {
            println('==='+ PROJECT_NAME.toUpperCase() + ': ' + msg)
        }
    }

    void log(String msg) {
        println('==='+ PROJECT_NAME.toUpperCase() + ': ' + msg)
    }

    void esureCacheDir() {
        dlog('esureCacheDir')
        File cacheDir = getCacheDir();

        if (!cacheDir.exists()) {
            dlog('create dir: ' + cacheDir.getAbsolutePath())
            cacheDir.mkdir()
        }
    }

    Set<String> scanKeepMainDexList(Project project) {
        Set<String> keepMainDexList = new HashSet<>()
        String applicationClassName = FastDexUtils.getApplicationClassName(FastDexUtils.getManifestFile(project))
        String packageName = FastDexUtils.getManifestPackageName(FastDexUtils.getManifestFile(project))
        keepMainDexList.add(applicationClassName.replaceAll("\\.","/") + ".class")
        keepMainDexList.add(packageName.replaceAll("\\.","/")  + "/R.class")
        keepMainDexList.add(packageName.replaceAll("\\.","/")  + "/R\$**.class")
       // keepMainDexList.add("**/BuildConfig.class")

        FolderComparator folderComparator = null
        dlog('scanKeepMainDexList incremental: ' + project.fastdex.incremental)
        if (project.fastdex.incremental
                && (folderComparator = FolderComparatorFactory.createFolderComparator(project)) != null) {
            //增量更新
            Set<String> diffFileSet =
                    folderComparator.compare(project,getCacheDir(),new File(project.getProjectDir(),"src/main/java"),new File(getCacheDir(),"java"))
            if (diffFileSet != null) {
                for (String path : diffFileSet) {
                    keepMainDexList.add(path.replaceAll(".java",".class"))
                }
            }
        }
        else {
            dlog('hookJar ext: ' + project.fastdex.rootPackage)
            if (project.fastdex.rootPackage == null || "".equals(project.fastdex.rootPackage.trim())) {
                dlog('scanKeepMainDexList: use applicationId: ' + project.android.defaultConfig.applicationId)
                keepMainDexList.add(project.android.defaultConfig.applicationId.replaceAll("\\.","/") + "/**")
            }
            else {
                dlog('scanKeepMainDexList: use rootPackage: ' + project.fastdex.rootPackage)
                keepMainDexList.add(project.fastdex.rootPackage.replaceAll("\\.","/") + "/**")
            }
        }

        //是否存在${projectdir}/keep_main_dex_list.txt
        File rulesFile = new File(project.getProjectDir(),PROJECT_NAME + "_keep_main_dex.txt");
        if (rulesFile.exists() && rulesFile.isFile()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rulesFile)));
            String line = null;
            while((line = br.readLine()) != null){
                if (line != null && !line.startsWith("#")) {
                    keepMainDexList.add(line.replaceAll("\\.","/"))
                }
            }
            br.close();
        }
        return keepMainDexList;
    }

    boolean hasCachedDex() {
        File dexFile = null;
        for (int point = 2;(dexFile = new File(getCacheDir(),"classes" + point + ".dex")).exists();point++) {
            dlog("has cached dex")
            return true
        }
        return false
    }

    void hookJar(Project project) {
        dlog('hookJar transformClassesWithJarMergingForDebug')

        File zipFile = new File(project.getBuildDir(),"/intermediates/transforms/jarMerging/debug/jars/1/1f/combined.jar")
        if (!zipFile.exists()) {
            dlog('hookJar file not found: ' + zipFile.getAbsolutePath())
            return
        }

        File unzipDir = new File(getCacheDir(),"combined")
        FastDexUtils.deleteFile(unzipDir)
        if (unzipDir.exists()) {
            throw new IllegalStateException("del combined dir fail: " + unzipDir.getAbsolutePath())
        }
        unzipDir.mkdirs()

        Set<String> mainDexList = scanKeepMainDexList(project)

        project.copy {
            from project.zipTree(zipFile)

            for (String pattern : mainDexList) {
                log('hookJar main_dex_pattern: ' + pattern)
                include pattern
            }
            into unzipDir
        }


        zipFile.delete()
        project.ant.zip(baseDir: unzipDir, destFile: zipFile)
    }

    void hookDex(Project project) {
        dlog('hookDex transformClassesWithDexForDebug')

        project.copy {
            from(getCacheDir())
            into('build/intermediates/transforms/dex/debug/folders/1000/1f/main/')

            File dexFile = null;
            for (int point = 2;(dexFile = new File(getCacheDir(),"classes" + point + ".dex")).exists();point++) {
                log("use cache dex: " + dexFile)
                include(dexFile.getName())
            }
        }
    }

    @Override
    void beforeExecute(Task task) {

    }

    void removeFinalModifier(Project project,File rootFile) {
        if (rootFile.isFile()) {
            String content = FastDexUtils.readUTF8Content(rootFile)
            content = content.replaceAll("final","")
            BufferedOutputStream bos = null
            try {
                bos = rootFile.newOutputStream()
                bos.write(content.getBytes("UTF-8"))
                bos.flush()
            } finally {
              if (bos != null) {
                  try {
                      bos.close()
                  } catch (Exception e){

                  }
              }
            }
        } else {
            File[] files = rootFile.listFiles()
            for (File file : files) {
                removeFinalModifier(project,file)
            }
        }
    }

    void copyDex(Project project) {
        dlog('copyDex')
        //缓存dex
        project.copy {
            from(new File(project.getBuildDir(), "/intermediates/transforms/dex/debug/folders/1000/1f/main/"))
            into(getCacheDir())

            include('classes3.dex')
            rename('classes3.dex', 'classes4.dex')

            include('classes2.dex')
            rename('classes2.dex', 'classes3.dex')

            include('classes.dex')
            rename('classes.dex', 'classes2.dex')
        }
    }

    void copyJavaSource(Project project) {
        dlog('copyJavaSource')
        //对source目录做快照
        FastDexUtils.deleteFile(new File(getCacheDir(), "/java"))
        if (project.fastdex.incremental) {
            //如果开启增量更新，对sourceSet对快照
            project.copy {
                from(new File(project.getProjectDir(), 'src/main/java'))
                into(new File(getCacheDir(), "/java"))
            }
        }
    }

    @Override
    void afterExecute(Task task, TaskState state) {
        if ('assembleDebug'.equals(task.name)) {
            Properties properties = new Properties()
            properties.load(appProject.rootProject.file('local.properties').newDataInputStream())
            def enable = properties.getProperty('fastdex.enable',"true")

            if ("false".equals(enable)) {
                copyJavaSource(appProject)
            }
        }
        if ('processDebugResources'.equals(task.name)) {
//            def bootTaskName = appProject.gradle.startParameter.taskNames[0]
//            if ('cacheDex'.equals(bootTaskName)) {
//                removeFinalModifier(appProject,new File(appProject.getBuildDir(),"/generated/source/r/debug"))
//            }
        }
        else if ('transformClassesWithJarMergingForDebug'.equals(task.name)) {
            Properties properties = new Properties()
            properties.load(appProject.rootProject.file('local.properties').newDataInputStream())
            def enable = properties.getProperty('fastdex.enable',"true")

            if (hasCachedDex() && !"false".equals(enable)) {
                hookJar(appProject)
            }
        }
        else if ('transformClassesWithDexForDebug'.equals(task.name)) {
            Properties properties = new Properties()
            properties.load(appProject.rootProject.file('local.properties').newDataInputStream())
            def enable = properties.getProperty('fastdex.enable',"true")

            if (hasCachedDex()) {
                if ("false".equals(enable)) {
                    copyDex(appProject)
                }
                else {
                    hookDex(appProject)
                }
            }
        }
        else if ('assembleDebug'.equals(task.name)) {
            Properties properties = new Properties()
            properties.load(appProject.rootProject.file('local.properties').newDataInputStream())
            def enable = properties.getProperty('fastdex.enable',"true")

            if (hasCachedDex()) {
                log("***请关闭Instant Run,使用5.0以上的设备调试***")
            }
            else {
                if (!"false".equals(enable)) {
                    log("***执行cacheDex预先缓存dex,能加速assembleDebug任务apk的生成***")
                }
            }
        }
    }

    void apply(Project project) {
        if (project.getProjectDir().equals(project.getRootDir())) {
            return
        }
        project.extensions.create(PROJECT_NAME, AppExtension.class, project)

        project.gradle.addListener(new TimeListener())
        appProject = project;
        project.gradle.addListener(this)
        cacheDir = new File(project.getProjectDir(),'/' + CACHE_DIR_NAME);

        def bootTaskName = project.gradle.startParameter.taskNames[0]
        dlog('bootTaskName: ' + bootTaskName + ' projectDir: ' + project.getProjectDir())

        project.task('cleanDex', group: PROJECT_NAME, description: 'Clean all dex',type: Delete,dependsOn: 'clean') << {
            dlog('cleanDex')
            delete getCacheDir()
        }

        project.task('cacheDex', group: PROJECT_NAME, description: 'Build all dex',dependsOn: ['cleanDex','transformClassesWithDexForDebug']) {
            doLast {
                dlog('cacheDex doLast')
                esureCacheDir()

                copyDex(project)
                copyJavaSource(project)
            }
        }
    }
}