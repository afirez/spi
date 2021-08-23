package com.knight.transform

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.knight.transform.Utils.PrintAllTaskUtil
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import transform.KnightTransform

abstract class KnightTaskPlugin<E : BaseExtension, C : BaseContext<*>> : Plugin<Project> {

    protected lateinit var context: C
    protected lateinit var extension: E

    protected lateinit var project: Project
    protected lateinit var android: TestedExtension


    abstract fun getContext(project: Project, extension: E, android: TestedExtension): C
    abstract fun createExtensions(): E
    lateinit var transform: KnightTransform

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(AppPlugin::class.java) && !project.plugins.hasPlugin(LibraryPlugin::class.java)) {
            throw  GradleException("'com.android.application' or 'com.android.library' plugin required!")
        }

        val aClass = if (project.plugins.hasPlugin(AppPlugin::class.java))
            AppExtension::class.java
        else LibraryExtension::class.java
        this.project = project
        android = project.extensions.getByType(aClass)
        extension = createExtensions()
        context = getContext(project, extension, android)
        project.afterEvaluate {
            if (android is AppExtension) {
                (android as AppExtension).applicationVariants.all {
                    createTask(it, context)
                }
            } else if (android is LibraryExtension) {
                (android as LibraryExtension).libraryVariants.all {
                    createTask(it, context)
                }
            }
        }
    }

    abstract fun createTask(variant: BaseVariant, context: C)
}