package com.afirez.spi.weave

import com.afirez.spi.SpiContext
import com.afirez.spi.SpiExtension
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class SpiInjectCodeToConstructorVisitor(val context: SpiContext, mv: MethodVisitor?) : MethodVisitor(Opcodes.ASM5, mv) {
    val extension = context.extension as SpiExtension

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.IRETURN,
            Opcodes.FRETURN,
            Opcodes.ARETURN,
            Opcodes.LRETURN,
            Opcodes.DRETURN,
            Opcodes.RETURN -> {
                println("Spi serviceMap: ${context.serviceMap}")
                context.serviceMap.forEach { serviceTypePath, map ->
                    map.forEach{path, serviceImplTypePath ->
                        injectService(serviceTypePath, serviceImplTypePath, path)
                    }
                }
            }
        }
        super.visitInsn(opcode)
    }

    private fun injectService(serviceTypePath: String, serviceImplTypePath: String, path: String) {
        println("Spi addService [ $serviceTypePath --> $serviceImplTypePath ]")
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitLdcInsn(Type.getObjectType(serviceTypePath))
        mv.visitLdcInsn(Type.getObjectType(serviceImplTypePath))
        mv.visitLdcInsn(path)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            extension.serviceLoaderPath,
            extension.addService,
            "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/String;)V",
            false
        )
//        mv.visitInsn(Opcodes.POP)
    }
}
