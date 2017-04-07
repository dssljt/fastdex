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
    public Set<String> addOrModifiedClassPatterns = new HashSet<>();

    @Expose
    public Set<PathInfo> addOrModifiedPathInfos = new HashSet<>();

    public SourceSetDiffResultSet() {

    }

    public SourceSetDiffResultSet(SourceSetDiffResultSet resultSet) {
        super(resultSet);
        //from gson
        this.changedJavaFileDiffInfos.addAll(resultSet.changedJavaFileDiffInfos);
        this.addOrModifiedClassPatterns.addAll(resultSet.addOrModifiedClassPatterns);
        this.addOrModifiedPathInfos.addAll(resultSet.addOrModifiedPathInfos);
    }

    public boolean isJavaFileChanged() {
        return !addOrModifiedClassPatterns.isEmpty();
    }

    public void addJavaFileDiffInfo(JavaFileDiffInfo diffInfo) {
        if (diffInfo.status != Status.NOCHANGED) {
            this.changedJavaFileDiffInfos.add(diffInfo);
        }
    }

    public void mergeJavaDirectoryResultSet(String path,JavaDirectoryDiffResultSet javaDirectoryResultSet) {
        Set<JavaFileDiffInfo> changedDiffInfos = javaDirectoryResultSet.changedDiffInfos;
        for (JavaFileDiffInfo javaFileDiffInfo : changedDiffInfos) {
            switch (javaFileDiffInfo.status) {
                case ADDED:
                case MODIFIED:
                    addOrModifiedPathInfos.add(new PathInfo(new File(path,javaFileDiffInfo.uniqueKey),javaFileDiffInfo.uniqueKey));

                    String classRelativePath = javaFileDiffInfo.uniqueKey.substring(0, javaFileDiffInfo.uniqueKey.length() - ".java".length());
                    addOrModifiedClassPatterns.add(classRelativePath + ".class");
                    addOrModifiedClassPatterns.add(classRelativePath + "\\$\\S{0,}.class");
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
                "changedJavaFileDiffInfos=" + changedJavaFileDiffInfos +
                ", addOrModifiedClassPatterns=" + addOrModifiedClassPatterns +
                ", addOrModifiedPathInfos=" + addOrModifiedPathInfos +
                '}';
    }
}
