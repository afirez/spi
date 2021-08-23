package com.knight.transform.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformInvocation
import com.android.ide.common.internal.WaitableExecutor
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class TransformContext(val invocation: TransformInvocation) {

    val targetJars = ConcurrentHashMap<String, ArrayList<String>>()
    val targetFile = ArrayList<String>()


    val waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()


    fun getOutPutFile(content: QualifiedContent): File {
        return invocation.outputProvider.getContentLocation(
                content.file.absolutePath,
                content.contentTypes,
                content.scopes,
                if (content is JarInput) Format.JAR else Format.DIRECTORY)
    }
}