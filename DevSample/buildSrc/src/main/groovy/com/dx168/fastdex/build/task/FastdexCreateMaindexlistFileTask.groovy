package com.dx168.fastdex.build.task

import com.dx168.fastdex.build.util.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by tong on 17/3/12.
 */
public class FastdexCreateMaindexlistFileTask extends DefaultTask {
    String variantName

    FastdexCreateMaindexlistFileTask() {
        group = 'fastdex'
    }

    @TaskAction
    void createFile() {
        if (variantName != null) {
            File maindexlistFile = new File(project.getBuildDir(),"/intermediates/multi-dex/${variantName.toLowerCase()}/maindexlist.txt")
            File parentFile = maindexlistFile.getParentFile()
            FileUtils.ensumeDir(parentFile)

            if (!FileUtils.isLegalFile(maindexlistFile.getAbsolutePath())) {
                maindexlistFile.createNewFile()
            }
        }
    }
}
