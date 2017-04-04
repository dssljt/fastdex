package com.dx168.fastdex.build.util

import org.gradle.api.logging.Logger
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

/**
 * 目录对比，file.length或者file.lastModified不一样时判定文件发生变化
 *
 * Created by tong on 17/3/10.
 */
public class JavaDirDiff {
    public static Set<DiffInfo> diff(File newDir, File oldDir, boolean useRelativePath, Logger logger) throws IOException {
        if (newDir == null || oldDir == null) {
            throw new RuntimeException("newDir == null || oldDir == null");
        }
        if (!newDir.isDirectory()) {
            throw new RuntimeException(newDir + " is not dir");
        }
//        if (!oldDir.isDirectory()) {
//            throw new RuntimeException(oldDir + " is not dir");
//        }

        logger.error("==fastdex diff dir: ${newDir} ${oldDir}")
        Set<DiffInfo> result = new HashSet<>();
        Files.walkFileTree(newDir.toPath(),new CompareFileVisitor(newDir.toPath(),oldDir.toPath(),result,useRelativePath,logger));
        return result;
    }

    public static final class DiffInfo {
        String relativePath
        String absolutePath

        DiffInfo(String relativePath, String absolutePath) {
            this.relativePath = relativePath
            this.absolutePath = absolutePath
        }
    }

    private static final class CompareFileVisitor extends SimpleFileVisitor<Path> {
        private final Path newDir;
        private final Path oldDir;
        private final Set<DiffInfo> result;
        private final boolean useRelativePath;
        private final Logger logger;

        public CompareFileVisitor(Path newDir, Path oldDir, Set<DiffInfo> result,boolean useRelativePath,Logger logger) {
            this.newDir = newDir;
            this.oldDir = oldDir;
            this.result = result;
            this.useRelativePath = useRelativePath;
            this.logger = logger;
        }

        @Override
        public FileVisitResult visitFile(Path newPath, BasicFileAttributes attrs) throws IOException {
            if (!newPath.toFile().getName().endsWith(".java")) {
                return FileVisitResult.CONTINUE;
            }
            Path relativePath = newDir.relativize(newPath);
            Path oldPath = oldDir.resolve(relativePath);

            File newFile = newPath.toFile();
            File oldFile = oldPath.toFile();

            if (!oldFile.exists()) {
                //String item = useRelativePath ? relativePath.toString() : newFile.getAbsolutePath()
                logger.error("==fastdex found change: " + relativePath)
                //result.add(item);
                result.add(new DiffInfo(relativePath.toString(),newFile.getAbsolutePath()))
            }
            else if ((newFile.lastModified() != oldFile.lastModified())
                    || (newFile.length() != oldFile.length())) {
                //String item = useRelativePath ? relativePath.toString() : newFile.getAbsolutePath()
                logger.error("==fastdex found change: " + relativePath)
                //result.add(item);
                result.add(new DiffInfo(relativePath.toString(),newFile.getAbsolutePath()))
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
