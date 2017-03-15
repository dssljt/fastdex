package com.dx168.fastdex.build.task

import com.dx168.fastdex.build.util.Constant
import com.dx168.fastdex.build.util.FastdexUtils
import com.dx168.fastdex.build.util.FileUtils
import com.dx168.fastdex.build.util.GradleUtils
import com.dx168.fastdex.build.util.JavaDirDiff
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

/**
 * Created by tong on 17/3/12.
 */
public class FastdexCustomJavacTask extends DefaultTask {
    def applicationVariant
    String variantName
    String manifestPath
    Task compileTask

    FastdexCustomJavacTask() {
        group = 'fastdex'
    }

    @TaskAction
    void compile() {
        // project.tasks.create("fastdexPrepareTaskFor${variantName}", FastdexPrepareTask)
        prepareEnv()

        boolean hasValidCache = FastdexUtils.hasValidCache(project,variantName)
        if (!hasValidCache) {
            compileTask.enabled = true
            return
        }

        File patchJavaFileDir = new File(FastdexUtils.getBuildDir(project,variantName),"custom-combind")
        File patchClassesFileDir = new File(FastdexUtils.getBuildDir(project,variantName),"custom-combind-classes")
        FileUtils.deleteDir(patchJavaFileDir)
        FileUtils.ensumeDir(patchClassesFileDir)

        //compare changed class
        String[] srcDirs = project.android.sourceSets.main.java.srcDirs
        File snapshootDir = new File(FastdexUtils.getBuildDir(project,variantName),Constant.FASTDEX_SNAPSHOOT_DIR)
        Set<String> changedJavaClassNames = new HashSet<>()
        for (String srcDir : srcDirs) {
            File newDir = new File(srcDir)
            File oldDir = new File(snapshootDir,srcDir)

            Set<JavaDirDiff.DiffInfo> set = JavaDirDiff.diff(newDir,oldDir,false,project.logger)

            for (JavaDirDiff.DiffInfo diff : set) {
                FileUtils.copyFileUsingStream(new File(diff.absolutePath),new File(patchJavaFileDir,diff.relativePath))
                changedJavaClassNames.add(diff.relativePath)
            }
        }

        if (changedJavaClassNames.isEmpty()) {
            compileTask.enabled = true
            return
        }

        //compile java
        File androidJar = new File("${project.android.getSdkDirectory()}/platforms/${project.android.getCompileSdkVersion()}/android.jar")
        File classpathJar = FastdexUtils.getInjectedJarFile(project,variantName)
        project.logger.error("==fastdex androidJar: ${androidJar}")
        project.logger.error("==fastdex classpath: ${classpathJar}")
        project.ant.javac(
                srcdir: patchJavaFileDir,
                source: '1.7',
                target: '1.7',
                destdir: patchClassesFileDir,
                bootclasspath: androidJar,
                classpath: classpathJar
        )

        project.logger.error("==fastdex compile success: ${patchClassesFileDir}")
        compileTask.enabled = false

        File classesDir = applicationVariant.getVariantData().getScope().getJavaOutputDir()
        Files.walkFileTree(patchClassesFileDir.toPath(),new SimpleFileVisitor<Path>(){
            @Override
            FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relativePath = patchClassesFileDir.toPath().relativize(file)
                File destFile = new File(classesDir,relativePath.toString())
                FileUtils.copyFileUsingStream(file.toFile(),destFile)

                project.logger.error("==fastdex apply class to ${destFile}")
                return FileVisitResult.CONTINUE
            }
        })

        //FileUtils.deleteDir(patchClassesFileDir)

//        File customJavacTaskOutputFile = FastdexUtils.getCustomJavacTaskOutputFile(project,variantName)
//        project.ant.zip(baseDir: patchClassesFileDir, destFile: customJavacTaskOutputFile)

//        if (FileUtils.isLegalFile(customJavacTaskOutputFile)) {
//            project.logger.error("==fastdex compile success: ${customJavacTaskOutputFile}")
//            compileTask.enabled = false
//            //jarMergingTask.enabled = false
//        }
//        else {
//            project.logger.error("==fastdex compile fail!")
//            compileTask.enabled = true
//            //jarMergingTask.enabled = true
//        }
    }

    void prepareEnv() {
        //delete expired cache
        boolean hasValidCache = FastdexUtils.hasValidCache(project,variantName)
        if (hasValidCache) {
            try {
                File cachedDependListFile = FastdexUtils.getCachedDependListFile(project,variantName)
                if (!FileUtils.isLegalFile(cachedDependListFile)) {
                    throw new CheckException("miss depend list file: ${cachedDependListFile}")
                }
                //old
                Set<String> cachedDependencies = getCachedDependList()
                //current
                Set<String> currentDependencies = GradleUtils.getCurrentDependList(project,variantName)
                currentDependencies.removeAll(cachedDependencies)

                //check dependencies
                //remove
                //old    current
                //1.aar  1.aar
                //2.aar

                //add
                //old    current
                //1.aar  1.aar
                //       2.aar

                //change
                //old    current
                //1.aar  1.aar
                //2.aar  xx.aar

                //handler add and change
                if (!currentDependencies.isEmpty()) {
                    throw new CheckException("${variantName.toLowerCase()} dependencies changed")
                }

                File cachedResourceMappingFile = FastdexUtils.getCachedResourceMappingFile(project,variantName)
                if (!FileUtils.isLegalFile(cachedResourceMappingFile)) {
                    throw new CheckException("miss resource mapping file: ${cachedResourceMappingFile}")
                }

                File injectedJarFile = FastdexUtils.getInjectedJarFile(project,variantName)
                if (!FileUtils.isLegalFile(injectedJarFile)) {
                    throw new CheckException("miss injected jar file: ${injectedJarFile}")
                }
            } catch (CheckException e) {
                hasValidCache = false
                project.logger.error("==fastdex ${e.getMessage()}")
                project.logger.error("==fastdex we will remove ${variantName.toLowerCase()} cache")
            }
        }

        FileUtils.deleteFile(FastdexUtils.getCustomJavacTaskOutputFile(project,variantName))
        if (hasValidCache) {
            project.logger.error("==fastdex discover cached for ${variantName.toLowerCase()}")
        }
        else {
            FastdexUtils.cleanCache(project,variantName)
            FileUtils.ensumeDir(FastdexUtils.getBuildDir(project,variantName))
        }
    }

    /**
     * 获取缓存的依赖列表
     * @return
     * @throws FileNotFoundException
     */
    Set<String> getCachedDependList() {
        Set<String> result = new HashSet<>()
        File cachedDependListFile = FastdexUtils.getCachedDependListFile(project,variantName)
        if (FileUtils.isLegalFile(cachedDependListFile.getAbsolutePath())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cachedDependListFile)))
            String line = null
            while ((line = reader.readLine()) != null) {
                result.add(line)
            }
            reader.close()
        }
        return result
    }

    private class CheckException extends Exception {
        CheckException(String var1) {
            super(var1)
        }
    }
}
