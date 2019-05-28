package com.afirez.spi.weave

import com.afirez.spi.SpiContext
import com.afirez.spi.SpiExtension
import com.knight.transform.weave.BaseClassVisitor
import org.objectweb.asm.*

/**
 * @link https://github.com/afirez/spi
 */
class SpiScanClassVisitor(context: SpiContext, classWriter: ClassWriter) :
    BaseClassVisitor<SpiContext>(context, classWriter) {
    var superName: String? = null
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
        this.superName = superName
        this.interfaces = interfaces
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        var av = super.visitAnnotation(descriptor, visible)

        when (descriptor) {
            "L${extension.spiPath};" -> {
                av = SpiScanAnnotationVisitor(av)
            }
        }

        return av
    }


    inner class SpiScanAnnotationVisitor(av: AnnotationVisitor) : AnnotationVisitor(Opcodes.ASM5, av) {
        var name: String? = null
        var value: Any? = null


        override fun visit(name: String?, value: Any?) {
            super.visit(name, value)
            this.name = name
            this.value = value
        }

        override fun visitEnd() {
            super.visitEnd()
            println("@Spi ( $name = '$value' ) class $className:")

            if (interfaces?.size == 0) {

                val service = className
                val serviceImpl = className
                val path = if (value == null) {
                    className.replace("/", ".")
                } else {
                    "$value"
                }
                var map = context.serviceMap[service]
                if (map == null) {
                    map = HashMap()
                    context.serviceMap[service] = map
//                    println("    new Map:[ $service -> $map ]")
                }
                map[path] = serviceImpl
                return
            }

            interfaces?.forEach {
                val service = it
                val serviceImpl = className
                println("   Spi: [ $service -> $serviceImpl ]")
                val path = if (value == null) {
                    it.replace("/", ".")
                } else {
                    "$value"
                }
                var map = context.serviceMap[service]
                if (map == null) {
                    map = HashMap()
                    context.serviceMap[service] = map!!
//                    println("Spi: new Map:[ $service -> $map ]")
                }
                map!![path] = serviceImpl
            }

            println(" --> end class $className")
        }
    }


//    private fun typeOf(className: String?, targetclassName: String): Boolean {
//        className ?: return false
//
//        if (className == "Ljava/lang/Object") {
//            return false
//        }
//        if (className == targetclassName) {
//            return true
//        }
//
//        return typeOf(getSuperClass(className), targetclassName)
//
//    }

    //    List<String> getSuperClasses(className){
//        superClass=getSuperClass(className)
//        return superClass+getSuperClasses(superClass)
//    }
//
//    String getSuperClass(className) {
//        cw=new ClassWriter()
//        v=new SuperClassReadingClassVisitor(cw)
//        new ClassReader(className).accept(v)
//        return v.superClass
//    }

//    fun getSuperClass(className: String?): String? {
//        className ?: return null
//
//        try {
//            val classReader = ClassReader(className)
//            val cv = object : ClassVisitor(Opcodes.ASM5, cv) {
//                var superName: String? = ""
//                override fun visit(
//                    version: Int,
//                    access: Int,
//                    name: String?,
//                    signature: String?,
//                    superName: String?,
//                    interfaces: Array<out String>?
//                ) {
//                    this.superName = superName
//                    super.visit(version, access, name, signature, superName, interfaces)
//                }
//            }
//            classReader.accept(cv, ClassReader.SKIP_FRAMES)
//            return cv.superName
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            return null
//        }
//    }

}
