package com.afirez.spi


import com.didiglobal.booster.transform.asm.ClassTransformer
import com.didiglobal.booster.transform.asm.asClassNode
import com.didiglobal.booster.transform.asm.className
import com.didiglobal.booster.transform.util.AbstractSupervisor
import org.objectweb.asm.tree.AnnotationNode

/**
 * 收集 spi service
 */
class SpiSupervisor(
//    private val prefix: String,
    action: (HashMap<String, HashMap<String, String>>) -> Unit
) : AbstractSupervisor<HashMap<String, HashMap<String, String>>>(action) {
    override fun accept(name: String): Boolean {
        return name.endsWith(".class", true)
    }

    override fun collect(name: String, data: () -> ByteArray) {
        val klass = data().asClassNode()
        val className = klass.name
//        val superName = klass.superName
        val interfaces = klass.interfaces

        val annotations = mutableListOf<AnnotationNode>()
        if (klass.invisibleAnnotations != null) {
            annotations += klass.invisibleAnnotations
        }
        if (klass.visibleAnnotations != null) {
            annotations += klass.visibleAnnotations
        }
        annotations.forEach {
//            println("Annotation ${it.desc}")
            when (it.desc) {
                "L${SpiTransformer.spiAnotattion};" -> {
                    /**
                     * {api -> { path -> impl}} or {api -> { path -> impl}}
                     */
                    var extensionsMap = HashMap<String, HashMap<String, String>>()
                    var anotattionName: Any? = null
                    var anotattionValue: Any? = null
                    if (!it.values.isNullOrEmpty()) {
                        anotattionName = it.values[0]
                        anotattionValue = it.values[1]
                    }
                    println("@SPI ( $anotattionName = '$anotattionValue' ) class $className:")

                    if (interfaces?.size == 0) {

                        val type = className
                        val extension = className
                        val path = if (anotattionValue == null) {
                            className.replace("/", ".")
                        } else {
                            "$anotattionValue"
                        }

//                        var extensionMap = context.extensionsMap[type]
                        var extensionMap = extensionsMap[type]
                        if (extensionMap == null) {
                            extensionMap = HashMap()
                            extensionsMap[type] = extensionMap
//                    println("    new Map:[ $type -> $extensionMap ]")
                        }
                        extensionMap[path] = extension
                        action(extensionsMap)
                        return
                    }

                    interfaces?.forEach {
                        val type = it
                        val extension = className
                        println("   SPI: [ $type -> $extension ]")
                        val path = if (anotattionValue == null) {
                            className.replace("/", ".")
                        } else {
                            "$anotattionValue"
                        }
//                        var extensionMap = context.extensionsMap[type]
                        var extensionMap = extensionsMap[type]
                        if (extensionMap == null) {
                            extensionMap = HashMap()
//                            context.extensionsMap[type] = extensionMap!!
                            extensionsMap[type] = extensionMap!!
//                    println("Spi: new Map:[ $service -> $map ]")
                        }
                        extensionMap!![path] = extension

                        action(extensionsMap)
                    }

                    println(" --> end class $className")
                }

            }
        }


//        action(HashMap<String, HashMap<String, String>>())
    }


}