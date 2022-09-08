package com.afirez.spi

import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.didiglobal.booster.transform.asm.defaultInit
import com.didiglobal.booster.transform.util.NameCollector
import com.google.auto.service.AutoService
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

@AutoService(ClassTransformer::class)
class SpiTransformer : ClassTransformer {

    val services = HashMap<String, HashMap<String, String>>()

    override fun onPreTransform(context: TransformContext) {
        context.registerCollector(SpiSupervisor {
            it.forEach { api, newImplMap ->
                val implMap = services.getOrDefault(api, HashMap<String, String>())
                implMap += newImplMap
                services.put(api, implMap)
            }
        })

        // 一旦有 JAR/DIR 中包含 ${SERVICE_REGISTRY} 这个文件
        // 则强制走 transform 的流程，无论是全量还是增量
        context.registerCollector(NameCollector(SERVICE_REGISTRY))
    }

    override fun transform(context: TransformContext, klass: ClassNode) = klass.apply {
        val name = klass.name
        when (name) {
            SERVICE_REGISTRY -> {
//                println("services ${services.size}")
                val init = methods.find {
                    it.name == "<init>" && it.desc == "()V"
                } ?: defaultInit.apply {
                    methods.add(this)
                }

                val pre = InsnList()
                val post = InsnList()
                var beforeReturn = true
                val oldInstructions = init.instructions
                val size = oldInstructions.size()
                for (i in 0 until size) {
                    val inst = oldInstructions.get(i)
                    if (beforeReturn) {
//                        if (inst is InsnNode) {
                            when (inst.opcode) {
                                Opcodes.IRETURN,
                                Opcodes.FRETURN,
                                Opcodes.ARETURN,
                                Opcodes.LRETURN,
                                Opcodes.DRETURN,
                                Opcodes.RETURN -> {
                                    beforeReturn = false
                                }
                                else -> {}
                            }
//                        }
                    }
                    if (beforeReturn) {
                        pre.add(inst)
                    } else {
                        post.add(inst)
                    }
                }

                val newInsnList = InsnList().apply {
                    add(pre)

                    services.forEach { api, implMap ->
                        implMap.forEach { path, impl ->
                            add(VarInsnNode(Opcodes.ALOAD, 0))
                            // class of api
                            add(LdcInsnNode(Type.getObjectType(api)))
                            // class of implementation
                            add(LdcInsnNode(Type.getObjectType(impl)))
                            // path to class of api
                            add(LdcInsnNode(path))
                            // ServiceRegistry.register(interface, path, implementation)
                            add(
                                MethodInsnNode(
                                    Opcodes.INVOKEVIRTUAL,
                                    SERVICE_REGISTRY,
                                    addService,
                                    "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/String;)V",
                                )
                            )
                        }
                    }

                    add(post)
//                    add(InsnNode(Opcodes.RETURN))
                }
                init.instructions = newInsnList
            }
        }
    }

    companion object {
        val spiAnotattion: String = "com/afirez/spi/SPI"
        //    private const val SERVICE_REGISTRY = "com/your/package/ServiceRegistry.class"
        //    private const

        val SERVICE_REGISTRY = "com/afirez/spi/ExtensionLoader"
        val addService: String = "addExtension"
    }
}
