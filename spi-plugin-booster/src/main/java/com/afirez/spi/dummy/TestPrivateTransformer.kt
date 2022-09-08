package com.afirez.spi.dummy

import com.didiglobal.booster.kotlinx.asIterable
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.ClassTransformer
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

//@AutoService(ClassTransformer::class)
class TestPrivateTransformer : ClassTransformer {

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode {
        val hookedClassName = klass.name
        var hookedMethodName = ""
        val iterator = klass.methods.iterator()
        while (iterator.hasNext()) {
            val method = iterator.next()
            method.instructions?.iterator()?.forEach {
                // 查找TelephonyManager.getNetworkType
                if (it.opcode == Opcodes.INVOKEVIRTUAL && it is MethodInsnNode) {
                    if (it.owner == "android/telephony/TelephonyManager" && it.name == "getNetworkType") {
                        hookedMethodName = method.name

                        println("====find private success ： $hookedClassName  /  $hookedMethodName")
                    }
                }
            }
            // 如果hookedMethodName不为空那么表示找到了，那么就插入代码
            if (!(hookedMethodName == null || hookedClassName.isEmpty())) {
                method?.instructions?.iterator()?.asIterable()?.filter {
                    it.opcode == Opcodes.RETURN
                }?.forEach {
                    method.instructions?.apply {
                        insertBefore(it, LdcInsnNode(hookedClassName))
                        insertBefore(it, LdcInsnNode(hookedMethodName))
                        insertBefore(it, LdcInsnNode("android/telephony/TelephonyManager"))
                        insertBefore(it, LdcInsnNode("getNetworkType"))
                        insertBefore(
                            it,
                            MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "com/afirez/spi/app",
                                "reportPrivateApi",
                                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                                false
                            )
                        )
                    }
                }
            }
        }

        return super.transform(context, klass)
    }
}