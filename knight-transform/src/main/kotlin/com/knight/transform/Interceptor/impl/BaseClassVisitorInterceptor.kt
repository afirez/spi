package com.knight.transform.Interceptor.impl

import com.knight.transform.BaseContext
import com.knight.transform.Interceptor.Chain
import com.knight.transform.Interceptor.IClassVisitorInterceptor
import com.knight.transform.weave.BaseClassVisitor
import com.knight.transform.weave.ExtendClassWriter
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class BaseClassVisitorInterceptor(private val wrapClassWriter: (ClassWriter) -> ClassVisitor?) : IClassVisitorInterceptor {
    override fun intercept(chain: Chain): ByteArray {
        chain.request().let {
            val classReader = ClassReader(it.inputByte)
            val classWriter = ExtendClassWriter(it.classloader, classReader,
                    ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
            val classVisitor = wrapClassWriter.invoke(classWriter)
            try {
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            } catch (e: Exception) {
                println("Exception occurred when visit code \n " + e.printStackTrace())

            }
            it.inputByte = classWriter.toByteArray()
            return chain.transform()
        }
    }


}