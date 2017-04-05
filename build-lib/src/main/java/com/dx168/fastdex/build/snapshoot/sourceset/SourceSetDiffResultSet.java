package com.dx168.fastdex.build.snapshoot.sourceset;

import com.dx168.fastdex.build.snapshoot.api.DiffResultSet;
import com.dx168.fastdex.build.snapshoot.api.Status;
import com.dx168.fastdex.build.snapshoot.string.StringDiffInfo;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tong on 17/3/31.
 */
public class SourceSetDiffResultSet extends DiffResultSet<StringDiffInfo> {
    public Set<JavaFileDiffInfo> changedJavaFileDiffInfos = new HashSet<JavaFileDiffInfo>();
    public String currentPath;
    public Set<String> addOrModifiedClassPatterns = new HashSet<>();
    public Set<File> addOrModifiedFiles = new HashSet<>();
    public Set<File> addOrModifiedRelativePaths = new HashSet<>();

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

                    break;
            }
        }
        this.changedJavaFileDiffInfos.addAll(changedDiffInfos);
    }

//    public Set<JavaFileDiffInfo> getJavaFileDiffInfos(Status ...statuses) {
//        Set<JavaFileDiffInfo> result = new HashSet<JavaFileDiffInfo>();
//        for (JavaFileDiffInfo diffInfo : changedJavaFileDiffInfos) {
//            bb : for (Status status : statuses) {
//                if (diffInfo.status == status) {
//                    result.add(diffInfo);
//                    break bb;
//                }
//            }
//        }
//        return result;
//    }

    public Set<String> getAddOrModifiedClassPatterns() {
        //return addOrModifiedClassPatterns;

        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SourceSetDiffResultSet that = (SourceSetDiffResultSet) o;

        return changedJavaFileDiffInfos != null ? changedJavaFileDiffInfos.equals(that.changedJavaFileDiffInfos) : that.changedJavaFileDiffInfos == null;

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
                '}';
    }
}
