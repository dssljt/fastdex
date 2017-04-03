package com.dx168.fastdex.build.util

import com.dx168.fastdex.build.lib.snapshoot.api.DiffResultSet
import com.dx168.fastdex.build.lib.snapshoot.api.Snapshoot
import com.dx168.fastdex.build.lib.snapshoot.sourceset.SourceSetDiffResultSet
import com.dx168.fastdex.build.lib.snapshoot.sourceset.SourceSetSnapshoot
import com.dx168.fastdex.build.util.FastdexUtils
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

    def prepareEnv(boolean hasValidCache) {
        if (sourceSetSnapshoot != null) {
            return
        }

        def project = fastdexVariant.project
        def srcDirs = project.android.sourceSets.main.java.srcDirs
        sourceSetSnapshoot = new SourceSetSnapshoot(project.projectDir,srcDirs)

        if (hasValidCache) {
            //load old cache
            File sourceSetFile = FastdexUtils.getSourceSetFile(project,fastdexVariant.variantName)

            SourceSetSnapshoot oldSourceSetSnapshoot = Snapshoot.load(sourceSetFile,SourceSetSnapshoot.class)
            boolean isSourceSetChanged = oldSourceSetSnapshoot.ensumeProjectDir(project.projectDir)
            if (isSourceSetChanged) {
                project.logger.error("==fastdex sourceChanged \n old: ${oldSourceSetSnapshoot.nodes} \n now: ${sourceSetSnapshoot}")
                //save
                saveSourceSetSnapshoot(oldSourceSetSnapshoot)
            }

            diffResultSet = sourceSetSnapshoot.diff(oldSourceSetSnapshoot)
            if (fastdexVariant.configuration.debug) {
                project.logger.error("==fastdex diffResultSet: ${diffResultSet}")
            }

            File diffResultSetFile = FastdexUtils.getDiffResultSetFile(project,fastdexVariant.variantName)
            if (diffResultSetFile.exists()) {
                oldDiffResultSet = DiffResultSet.load(diffResultSetFile,SourceSetDiffResultSet.class)
            }
        }
        else {
            //save
            saveSourceSetSnapshoot(sourceSetSnapshoot)
        }
    }

    def saveSourceSetSnapshoot(SourceSetSnapshoot snapshoot) {
        snapshoot.serializeTo(new FileOutputStream(FastdexUtils.getSourceSetFile(fastdexVariant.project,fastdexVariant.variantName)))
    }

    def isSourceSetChanged() {
        diffResultSet.equals(oldDiffResultSet)
    }

    def getChangedClassPatterns() {
        diffResultSet.getAddOrModifiedClassPatterns()
    }
}
