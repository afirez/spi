package com.afirez.spi

import com.afirez.spi.weave.SpiScanClassVisitor
import com.afirez.spi.weave.SpiWeaveCodeClassVisitor
import com.android.build.gradle.TestedExtension
import com.knight.transform.KnightPlugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

/**
 * https://github.com/afirez/spi
 */
class SpiPlugin : KnightPlugin<SpiExtension, SpiContext>() {

    private val EXTENSION_NAME = "spi"

    override val isNeedPrintMapAndTaskCostTime: Boolean = true

    override fun createExtensions(): SpiExtension {
        project.extensions.create(EXTENSION_NAME, SpiExtension::class.java)
        (project.extensions.getByName(EXTENSION_NAME) as SpiExtension).apply {
            context = SpiContext(project, this)
            return this
        }
    }

    override fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return SpiScanClassVisitor(context, classWriter)
    }

    override fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor? {
        return SpiWeaveCodeClassVisitor(context, classWriter)
    }

    override fun getContext(project: Project, extension: SpiExtension, android: TestedExtension): SpiContext {
        return SpiContext(project, extension)
    }

    override fun getTransformName(): String {
        return "SpiTransform"
    }

    override fun isNeedScanClass(): Boolean {
        return true
    }

    override fun isNeedScanWeaveRClass(): Boolean {
        return false
    }
}