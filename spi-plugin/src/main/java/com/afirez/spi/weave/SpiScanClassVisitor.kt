package com.afirez.spi.weave

import com.afirez.spi.SpiContext
import com.afirez.spi.SpiExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassWriter

class SpiScanClassVisitor(context: SpiContext, classWriter: ClassWriter) :
    BaseClassVisitor<SpiContext>(context, classWriter) {
    var interfaces: Array<String>? = null
    val extension = context.extension as SpiExtension

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.interfaces = interfaces
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        when (descriptor) {
            "L${extension.spiPath};" -> {
                interfaces?.forEach {
                    println("Spi: scanPath:[$it -> $className]")
                    var map = context.serviceMap[it]
                    if (map == null) {
                        map = HashMap()
                        context.serviceMap[it] = map
                        println("Spi: new Map:[$it -> $map]")
                    }
                    map[it.replace("/", ".")] = className
                }
            }
        }
        return super.visitAnnotation(descriptor, visible)
    }
}
