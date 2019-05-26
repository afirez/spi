package com.afirez.spi.weave

import com.afirez.spi.SpiContext
import com.afirez.spi.SpiExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor


class SpiWeaveCodeClassVisitor(context: SpiContext, cv: ClassVisitor):BaseClassVisitor<SpiContext>(context, cv) {
    val extension = context.extension as SpiExtension

    var isServiceLoader = false


    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        isServiceLoader = name == extension.serviceLoaderPath
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {

        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (isServiceLoader && access == 2 && name == "<init>" && descriptor == "()V") {//找到目标类的私有构造方法
            println("Spi addService...: [service -> $className]")
            return SpiInjectCodeToConstructorVisitor(context, mv)
        }

        return mv
    }

}