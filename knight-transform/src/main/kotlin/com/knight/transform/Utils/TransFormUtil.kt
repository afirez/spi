package com.knight.transform.Utils

import com.android.build.api.transform.Format
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformInvocation
import com.android.ide.common.internal.WaitableExecutor
import com.knight.transform.BaseContext
import com.knight.transform.asm.IWeaver
import org.apache.commons.io.FileUtils
import java.io.File

object TransFormUtil {
    fun transform(context: BaseContext<*>, weaver: IWeaver, transform: TransformInvocation) {
        val waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()

        transform.apply {
            if (!isIncremental) outputProvider.deleteAll()
            inputs.forEach {
                it.jarInputs.forEach { jarInput ->
                    val status = jarInput.status
                    val dest = outputProvider.getContentLocation(
                            jarInput.file.absolutePath,
                            jarInput.contentTypes,
                            jarInput.scopes,
                            Format.JAR)
                    if (isIncremental) {
                        when (status) {
                            Status.ADDED, Status.CHANGED -> kotlin.run {
                                waitableExecutor.execute {
                                    if (context.extension.isScanJar) {
                                        weaver.weaveJar(jarInput.file, dest)
                                    } else {
                                        FileUtils.copyFile(jarInput.file, dest)
                                    }
                                }
                            }
                            Status.REMOVED -> {
                                if (dest.exists()) {
                                    FileUtils.forceDelete(dest)
                                }
                            }
                            else -> {
                            }
                        }
                    } else {
                        kotlin.run {
                            waitableExecutor.execute {
                                if (context.extension.isScanJar) {
                                    weaver.weaveJar(jarInput.file, dest)
                                } else {
                                    FileUtils.copyFile(jarInput.file, dest)
                                }
                            }
                        }
                    }
                }

                it.directoryInputs.forEach { directoryInput ->
                    val dest = outputProvider.getContentLocation(
                            directoryInput.name,
                            directoryInput.contentTypes,
                            directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtils.forceMkdir(dest)
                    if (isIncremental) {
                        val srcDirPath = directoryInput.file.absolutePath
                        val destDirPath = dest.absolutePath
                        directoryInput.changedFiles.forEach { inputFile, status ->
                            val destFilePath = inputFile.absolutePath.replace(srcDirPath, destDirPath)
                            val destFile = File(destFilePath)
                            when (status) {
                                Status.ADDED, Status.CHANGED -> kotlin.run {
                                    FileUtils.touch(destFile)
                                    waitableExecutor.execute {
                                        weaver.weaveFile(inputFile, destFile, srcDirPath)
                                    }
                                }
                                Status.REMOVED -> {
                                    if (destFile.exists()) {
                                        destFile.delete()
                                    }
                                }
                                else -> {
                                }
                            }
                        }
                    } else {
                        kotlin.run {
                            val inputDirPath = directoryInput.file.absolutePath
                            val outputDirPath = dest.absolutePath
                            if (directoryInput.file.isDirectory) {
                                com.android.utils.FileUtils.getAllFiles(directoryInput.file).forEach {
                                    waitableExecutor.execute {
                                        try {
                                            val filePath = it.absolutePath
                                            val outputFile = File(filePath.replace(inputDirPath, outputDirPath))
                                            weaver.weaveFile(it, outputFile, inputDirPath)
                                        } catch (e: Exception) {
                                            println("error: ${e}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            waitableExecutor.waitForTasksWithQuickFail<Any>(true)
        }

    }
}