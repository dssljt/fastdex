package com.dx168.fastdex.build.snapshoot.sourceset;

import com.dx168.fastdex.build.snapshoot.api.DiffResultSet;
import com.dx168.fastdex.build.snapshoot.api.Status;
import com.dx168.fastdex.build.snapshoot.string.StringDiffInfo;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tong on 17/3/31.
 */
public class SourceSetDiffResultSet extends DiffResultSet<StringDiffInfo> {
    public Set<JavaFileDiffInfo> changedJavaFileDiffInfos = new HashSet<JavaFileDiffInfo>();

    @Expose
    public String currentPath;
    @Expose
    public Set<String> addOrModifiedClassPatterns = new HashSet<>();
    @Expose
    public Set<File> addOrModifiedFiles = new HashSet<>();
    @Expose
    public Set<String> addOrModifiedRelativePaths = new HashSet<>();

    public SourceSetDiffResultSet() {

    }

    public boolean isSourceSetChanged() {
        return !getDiffInfos(Status.ADDED,Status.DELETEED).isEmpty();
    }

    public void addJavaFileDiffInfo(JavaFileDiffInfo diffInfo) {
        if (diffInfo.status != Status.NOCHANGED) {
            this.changedJavaFileDiffInfos.add(diffInfo);
        }
    }

    public void mergeJavaDirectoryResultSet(JavaDirectoryDiffResultSet javaDirectoryResultSet) {
        Set<JavaFileDiffInfo> changedDiffInfos = javaDirectoryResultSet.changedDiffInfos;
        for (JavaFileDiffInfo javaFileDiffInfo : changedDiffInfos) {
            switch (javaFileDiffInfo.status) {
                case ADDED:
                case MODIFIED:
                    addOrModifiedRelativePaths.add(javaFileDiffInfo.uniqueKey);
                    addOrModifiedFiles.add(new File(currentPath,javaFileDiffInfo.uniqueKey));

                    String classRelativePath = javaFileDiffInfo.uniqueKey.substring(0, javaFileDiffInfo.uniqueKey.length() - ".java".length());
                    addOrModifiedClassPatterns.add(classRelativePath + ".class");
                    addOrModifiedClassPatterns.add(classRelativePath + "\\$\\S{0,}$.class");
                    break;
            }
        }
        this.changedJavaFileDiffInfos.addAll(changedDiffInfos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SourceSetDiffResultSet resultSet = (SourceSetDiffResultSet) o;

        return changedJavaFileDiffInfos != null ? changedJavaFileDiffInfos.equals(resultSet.changedJavaFileDiffInfos) : resultSet.changedJavaFileDiffInfos == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (changedJavaFileDiffInfos != null ? changedJavaFileDiffInfos.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SourceSetDiffResultSet{" +
                "addOrModifiedClassPatterns=" + addOrModifiedClassPatterns +
                ", addOrModifiedFiles=" + addOrModifiedFiles +
                ", addOrModifiedRelativePaths=" + addOrModifiedRelativePaths +
                ", currentPath='" + currentPath + '\'' +
                '}';
    }
}
