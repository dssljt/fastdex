package com.dx168.fastdex.build.lib.snapshoot.sourceset;

import com.dx168.fastdex.build.lib.snapshoot.api.DiffResultSet;
import com.dx168.fastdex.build.lib.snapshoot.api.Status;
import com.dx168.fastdex.build.lib.snapshoot.file.FileNode;
import com.dx168.fastdex.build.lib.Constant

/**
 * Created by tong on 17/3/29.
 */
public class JavaDirectoryDiffResultSet extends DiffResultSet<JavaFileDiffInfo> {
    private final Set<String> addOrModifiedClassPatterns = new HashSet<>();

    public JavaDirectoryDiffResultSet() {
    }

    public JavaDirectoryDiffResultSet(JavaDirectoryDiffResultSet resultSet) {
        super(resultSet);
    }

    @Override
    public boolean add(JavaFileDiffInfo diffInfo) {
        //TODO
        if (diffInfo.status == Status.ADDED
                || diffInfo.status == Status.MODIFIED) {
            FileNode fileNode = (FileNode) diffInfo.now;
            String classRelativePath = fileNode.relativePath.substring(0, fileNode.relativePath.length() - ".java".length());

            addOrModifiedClassPatterns.add(classRelativePath + ".class");
            addOrModifiedClassPatterns.add(classRelativePath + "\\\$\\S{0,}${Constant.CLASS_SUFFIX}");
        }
        return super.add(diffInfo);
    }

    @Override
    public void merge(DiffResultSet<JavaFileDiffInfo> resultSet) {
        addOrModifiedClassPatterns.addAll(((JavaDirectoryDiffResultSet)resultSet).addOrModifiedClassPatterns);
        super.merge(resultSet);
    }

    public Set<String> getAddOrModifiedClassPatterns() {
        return addOrModifiedClassPatterns;
    }
}
