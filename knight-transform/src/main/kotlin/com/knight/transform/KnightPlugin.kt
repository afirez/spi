package com.knight.transform

import com.android.build.api.transform.Transform
import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.AndroidProject
import com.knight.transform.Interceptor.IClassVisitorInterceptor
import com.knight.transform.Utils.Timer
import org.gradle.api.*
import transform.KnightTransform
import transform.task.OutPutMappingTask

abstract class KnightPlugin<E : BaseExtension, C : BaseContext<*>> : Plugin<Project>, IPlugin {

    protected lateinit var context: C
    protected lateinit var extension: E

    protected lateinit var project: Project
    protected lateinit var android: TestedExtension
    protected abstract val isNeedPrintMapAndTaskCostTime: Boolean


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
        if (android is AppExtension) {
            (android as AppExtension).applicationVariants.all {
                createTask(it, context)
            }
        } else if (android is LibraryExtension) {
            (android as LibraryExtension).libraryVariants.all {
                createTask(it, context)
            }
        }
        transform = KnightTransform(context, this, ::getTransformName)
        android.registerTransform(transform)
    }

    abstract fun getTransformName(): String


    override fun getScanClassVisitorInterceptor(): List<IClassVisitorInterceptor>? {
        return null
    }

    override fun getWeaveClassVisitorInterceptor(): List<IClassVisitorInterceptor>? {
        return null
    }


    open fun createTask(variant: BaseVariant, context: BaseContext<*>) {
        createWriteMappingTask(variant, context)
    }

    private fun createWriteMappingTask(variant: BaseVariant, context: BaseContext<*>) {
        if (!isNeedPrintMapAndTaskCostTime) {
            return
        }
        val mappingTaskName = "${transform.name}outputMappingFor${variant.name.capitalize()}"
        val myTask = project.tasks.getByName("transformClassesWith${transform.name}For${variant.name.capitalize()}")
        myTask.apply {
            doFirst {
                Timer.start(name)
            }

            doLast {
                Timer.stop(name)
            }
        }
        val outputMappingTask = project.tasks.create(mappingTaskName, OutPutMappingTask::class.java)

        outputMappingTask.apply {
            this as OutPutMappingTask
            classes.set(context.weavedClassMap)
            variantName.set(transform.name)
            outputMappingFile.set(com.android.utils.FileUtils.join(project.buildDir, AndroidProject.FD_OUTPUTS, "mapping",
                    transform.name, variant.name, "${transform.name}Mapping.txt"))

            doFirst {
                Timer.start(name)
            }

            doLast {
                Timer.stop(name)
            }
        }

        myTask.finalizedBy(outputMappingTask)
        outputMappingTask.onlyIf { myTask.didWork }
        outputMappingTask.dependsOn(myTask)
    }
}