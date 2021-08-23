package com.knight.transform.weave

import com.knight.transform.BaseContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import transform.task.WeavedClass

abstract class BaseClassVisitor<C : BaseContext<*>>(val context: C, cv: ClassVisitor) : ClassVisitor(Opcodes.ASM5, cv), Opcodes {
    lateinit var className: String
    lateinit var weavedClass: WeavedClass
    override fun visit(version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        weavedClass = WeavedClass(className)
        context.weavedClassMap.add(weavedClass)
    }
}