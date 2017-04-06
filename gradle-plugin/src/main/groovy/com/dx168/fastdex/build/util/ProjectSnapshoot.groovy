package com.dx168.fastdex.build.util

import com.dx168.fastdex.build.snapshoot.sourceset.SourceSetDiffResultSet
import com.dx168.fastdex.build.snapshoot.sourceset.SourceSetSnapshoot
import com.dx168.fastdex.build.variant.FastdexVariant

/**
 * Created by tong on 17/3/31.
 */
public class ProjectSnapshoot {
    FastdexVariant fastdexVariant
    SourceSetSnapshoot sourceSetSnapshoot
    SourceSetDiffResultSet diffResultSet
    SourceSetDiffResultSet oldDiffResultSet

    ProjectSnapshoot(FastdexVariant fastdexVariant) {
        this.fastdexVariant = fastdexVariant
    }

    def prepareEnv() {
        if (sourceSetSnapshoot != null) {
            return
        }

        def project = fastdexVariant.project
        def srcDirs = project.android.sourceSets.main.java.srcDirs
        sourceSetSnapshoot = new SourceSetSnapshoot(project.projectDir,srcDirs)

        if (fastdexVariant.hasDexCache) {
            //load old sourceSet
            File sourceSetSnapshootFile = FastdexUtils.getSourceSetSnapshootFile(project,fastdexVariant.variantName)
            SourceSetSnapshoot oldSourceSetSnapshoot = SourceSetSnapshoot.load(sourceSetSnapshootFile,SourceSetSnapshoot.class)
            boolean isProjectDirChanged = oldSourceSetSnapshoot.ensumeProjectDir(project.projectDir)
            if (isProjectDirChanged) {
                project.logger.error("==fastdex sourceChanged \n old: ${oldSourceSetSnapshoot.nodes} \n now: ${sourceSetSnapshoot}")
                //save
                saveSourceSetSnapshoot(oldSourceSetSnapshoot)
            }

            diffResultSet = sourceSetSnapshoot.diff(oldSourceSetSnapshoot)
//            if (fastdexVariant.configuration.debug) {
//                project.logger.error("==fastdex diffResultSet: ${diffResultSet}")
//            }

            File diffResultSetFile = FastdexUtils.getDiffResultSetFile(project,fastdexVariant.variantName)
            if (fastdexVariant.firstPatchBuild) {
                if (!diffResultSet.changedJavaFileDiffInfos.empty) {
                    //全量打包后首次java文件发生变化
                    diffResultSet.serializeTo(new FileOutputStream(diffResultSetFile))
                }
            }
            else {
                oldDiffResultSet = SourceSetDiffResultSet.load(diffResultSetFile,SourceSetDiffResultSet.class)
            }
        }
        else {
            //save
            saveSourceSetSnapshoot(sourceSetSnapshoot)
        }
    }

    def saveSourceSetSnapshoot(SourceSetSnapshoot snapshoot) {
        snapshoot.serializeTo(new FileOutputStream(FastdexUtils.getSourceSetSnapshootFile(fastdexVariant.project,fastdexVariant.variantName)))
    }
}
