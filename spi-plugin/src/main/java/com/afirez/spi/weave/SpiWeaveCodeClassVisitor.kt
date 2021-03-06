package com.afirez.spi.weave

import com.afirez.spi.SpiContext
import com.afirez.spi.SpiExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.*

/**
 * https://github.com/afirez/spi
 */
class SpiWeaveCodeClassVisitor(context: SpiContext, cv: ClassVisitor) : BaseClassVisitor<SpiContext>(context, cv) {
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
        isServiceLoader = name == extension.loaderPath
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

        if (isServiceLoader && access == 2 && name == "<init>" && descriptor == "()V") { //找到目标类的私有构造方法
            return SpiWeaveMethodVisitor(context, mv)
        }

        return mv
    }

    class SpiWeaveMethodVisitor(val context: SpiContext, mv: MethodVisitor?) : MethodVisitor(Opcodes.ASM5, mv) {
        val extension = context.extension as SpiExtension

        override fun visitInsn(opcode: Int) {
            when (opcode) {
                Opcodes.IRETURN,
                Opcodes.FRETURN,
                Opcodes.ARETURN,
                Opcodes.LRETURN,
                Opcodes.DRETURN,
                Opcodes.RETURN -> {
                    context.extensionsMap.forEach { type, map ->
                        map.forEach{path, extension ->
                            inject(type, extension, path)
                        }
                    }
                }
            }
            super.visitInsn(opcode)
        }

        private fun inject(type: String, extension: String, path: String) {
            println(" ====>  Spi ${this.extension.add} [ $type --> $extension ]")
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitLdcInsn(Type.getObjectType(type))
            mv.visitLdcInsn(Type.getObjectType(extension))
            mv.visitLdcInsn(path)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                this.extension.loaderPath,
                this.extension.add,
                "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/String;)V",
                false
            )
        }
    }
}