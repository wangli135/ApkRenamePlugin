package com.xingfeng.plugin

import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.platform.base.Variant

/**
 * Created by xingfeng
 * on 2018/9/6
 */
class ApkRenamePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        ApkRenamePluginExtension apkRenameExtensions = project.extensions.create("apkrename", ApkRenamePluginExtension)

        configureApkRename(project, project.android.applicationVariants, apkRenameExtensions)


    }

    private void configureApkRename(Project project, DomainObjectCollection<Variant> variants, ApkRenamePluginExtension apkRenamePluginExtension) {

        def projectName = project.name

        project.afterEvaluate {

            variants.all { variant ->

                def androidExtensions = project.extensions.findByName("android")

                def versionName = androidExtensions.defaultConfig.versionName
                def versionCode = androidExtensions.defaultConfig.versionCode

                variant.outputs.each { output ->
                    if (output != null && output.outputFile.name.endsWith(".apk")) {

                        def originApkName = output.outputFile.name
                        def apkNameSuffix = originApkName.substring(projectName.length())

                        def apkName = apkRenamePluginExtension.apkName == "" ? projectName : apkRenamePluginExtension.apkName
                        def apkFile = new File(output.outputFile.parent, "${apkName}-${versionName}-${versionCode}${apkNameSuffix}")
                        output.outputFile = apkFile

                    }

                }

            }

        }

    }


}
