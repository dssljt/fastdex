package com.dx168.fastdex.build.util;

import java.io.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.gradle.api.Project

/**
 * Created by tong on 17/3/23.
 */
public class JarOperation {
    /**
     * 转换全量打包的jar包，往所有项目代码里注入解决pre-verify问题的code
     * @param inputJar                  输入jar
     * @param outputJar                 补丁jar输出位置
     * @param needInjectClassPatterns   所有项目代码的class路径正则列表
     *
     * 例如:
     * com/dx168/fastdex/sample/MainActivity.class
     * com/dx168/fastdex/sample/MainActivity\\$\S{0,}.class
     * @throws IOException
     */
    public static void transformNormalJar(Project project,File inputJar, File outputJar, Set<String> needInjectClassPatterns) throws IOException {
        long start = System.currentTimeMillis();
        transformJar(inputJar,outputJar,new NormalProcessor(project,needInjectClassPatterns));
        long end = System.currentTimeMillis();
        project.logger.error("==fastdex inject complete: ${outputJar} use: ${end - start}ms")
    }

    /**
     * 转换补丁jar包，从jar中移除没有变化的class
     * @param inputJar              输入jar
     * @param outputJar             补丁jar输出位置
     * @param changedClassPatterns  所有变化的class路径正则列表
     *
     * 例如:
     * com/dx168/fastdex/sample/MainActivity.class
     * com/dx168/fastdex/sample/MainActivity\\$\S{0,}.class
     * @throws IOException
     */
    public static void transformPatchJar(Project project,File inputJar, File outputJar, Set<String> changedClassPatterns) throws IOException {
        long start = System.currentTimeMillis();
        transformJar(inputJar,outputJar,new PatchProcessor(project,changedClassPatterns));
        long end = System.currentTimeMillis();
        project.logger.error("==fastdex generate patch jar complete: ${outputJar} use: ${end - start}ms")
    }

    /**
     * 转换jar包
     * @param inputJar      输入jar
     * @param outputJar     输出jar的路径
     * @param processor     处理器
     * @throws IOException
     */
    private static void transformJar(File inputJar, File outputJar, Processor processor) throws IOException {
        if (outputJar.exists()) {
            outputJar.delete();
        }

        ZipOutputStream outputJarStream = new ZipOutputStream(new FileOutputStream(outputJar));
        ZipFile zipFile = new ZipFile(inputJar);
        Enumeration enumeration = zipFile.entries();
        try {
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                processor.process(zipFile,entry,outputJarStream);
            }
        } finally {
            if (zipFile != null) {
                zipFile.close();
            }
            if (outputJarStream != null) {
                outputJarStream.close();
            }
        }
    }

    private static class PatchProcessor extends NormalProcessor {
        public PatchProcessor(Project project,Set<String> changedClassPatterns) {
            super(project,changedClassPatterns);
        }

        @Override
        public void process(ZipFile zipFile, ZipEntry entry, ZipOutputStream outputJarStream) throws IOException {
            for (Pattern pattern : patterns) {
                if (pattern.matcher(entry.getName()).matches()) {
                    project.logger.error("==fastdex patch class: " + entry.getName());
                    outputJarStream.putNextEntry(new ZipEntry(entry.getName()));
                    byte[] classBytes = readStream(zipFile.getInputStream(entry));
                    outputJarStream.write(classBytes);
                    outputJarStream.closeEntry();
                    break;
                }
            }
        }
    }

    private static class NormalProcessor extends Processor {
        protected final Project project
        protected final Set<Pattern> patterns;

        public NormalProcessor(Project project,Set<String> needInjectClassPatterns) {
            this.project = project;

            patterns = new HashSet<Pattern>();
            if (needInjectClassPatterns != null && !needInjectClassPatterns.isEmpty()) {
                for (String patternStr : needInjectClassPatterns) {
                    patterns.add(Pattern.compile(patternStr));
                }
            }
        }

        @Override
        public void process(ZipFile zipFile,ZipEntry entry, ZipOutputStream outputJarStream) throws IOException {
            outputJarStream.putNextEntry(new ZipEntry(entry.getName()));
            byte[] classBytes = readStream(zipFile.getInputStream(entry));
            if (patterns != null) {
                for (Pattern pattern : patterns) {
                    if (pattern.matcher(entry.getName()).matches()) {
                        project.logger.error("==fastdex inject: " + entry.getName())
                        classBytes = ClassInject.inject(classBytes);
                        break;
                    }
                }
            }
            outputJarStream.write(classBytes);
            outputJarStream.closeEntry();
        }
    }

    private static abstract class Processor {
        public abstract void process(ZipFile zipFile,ZipEntry entry,ZipOutputStream outputJarStream) throws IOException;

        protected byte[] readStream(InputStream is) throws IOException {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final int bufferSize = 1024;
            try {
                final BufferedInputStream bIn = new BufferedInputStream(is);
                int length;
                byte[] buffer = new byte[bufferSize];
                byte[] bufferCopy;
                while ((length = bIn.read(buffer, 0, bufferSize)) != -1) {
                    bufferCopy = new byte[length];
                    System.arraycopy(buffer, 0, bufferCopy, 0, length);
                    output.write(bufferCopy);
                }
                bIn.close();
            } finally {
                output.close();
            }
            return output.toByteArray();
        }
    }
}
