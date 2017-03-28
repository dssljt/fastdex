package com.dx168.fastdex.build.util

import com.dx168.fastdex.build.extension.FastdexExtension
import org.gradle.api.Project

/**
 * Created by tong on 17/3/10.
 */
public class FastdexVariantData {
    Project project
    FastdexExtension configuration
    def applicationVariant
    String variantName
    String manifestPath
}
