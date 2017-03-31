package com.dx168.fastdex.build.snapshoot

import com.dx168.fastdex.build.snapshoot.api.Snapshoot
import com.dx168.fastdex.build.snapshoot.sourceset.SourceSetResultSet
import com.dx168.fastdex.build.snapshoot.sourceset.SourceSetSnapshoot
import com.dx168.fastdex.build.util.FastdexUtils
import com.dx168.fastdex.build.variant.FastdexVariant

/**
 * Created by tong on 17/3/31.
 */
public class ProjectSnapshoot {
    FastdexVariant fastdexVariant
    SourceSetSnapshoot nowSourceSetSnapshoot
    SourceSetSnapshoot oldSourceSetSnapshoot
    SourceSetResultSet sourceSetResultSet

    ProjectSnapshoot(FastdexVariant fastdexVariant) {
        this.fastdexVariant = fastdexVariant
    }

    def prepareEnv(boolean hasValidCache) {
        if (nowSourceSetSnapshoot != null) {
            return
        }

        def project = fastdexVariant.project
        def srcDirs = project.android.sourceSets.main.java.srcDirs
        nowSourceSetSnapshoot = new SourceSetSnapshoot(project.projectDir,srcDirs)

        if (hasValidCache) {
            //load old cache
            oldSourceSetSnapshoot = Snapshoot.load(new FileInputStream(FastdexUtils.getSourceSetFile(project,fastdexVariant.variantName)))
            boolean isSourceSetChanged = oldSourceSetSnapshoot.ensumeProjectDir(project.projectDir)
            if (isSourceSetChanged) {
                project.logger.error("==fastdex sourceChanged \n old: ${oldSourceSetSnapshoot.nodes} \n now: ${nowSourceSetSnapshoot}")
                //save
                saveSourceSetSnapshoot(oldSourceSetSnapshoot)
            }

            sourceSetResultSet = nowSourceSetSnapshoot.diff(oldSourceSetSnapshoot)
        }
        else {
            //save
            saveSourceSetSnapshoot(nowSourceSetSnapshoot)
        }
    }

    def saveSourceSetSnapshoot(SourceSetSnapshoot snapshoot) {
        snapshoot.serializeTo(new FileOutputStream(FastdexUtils.getSourceSetFile(project,fastdexVariant.variantName)))
    }
}
