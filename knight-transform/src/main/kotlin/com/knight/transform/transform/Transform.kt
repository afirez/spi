package com.knight.transform.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformInvocation
import com.android.ide.common.internal.WaitableExecutor
import com.knight.transform.BaseContext
import com.knight.transform.asm.IWeaver
import org.apache.commons.io.FileUtils
import java.io.File


class Transform(val myContext: BaseContext<*>, val weaver: IWeaver) {
    val waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()

    fun transform(transform: TransformInvocation) {
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
                            Status.ADDED, Status.CHANGED -> {
                                transformJar(jarInput.file, dest, myContext.extension.isScanJar)
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
                        transformJar(jarInput.file, dest, myContext.extension.isScanJar)
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
                                Status.ADDED, Status.CHANGED -> {
                                    FileUtils.touch(destFile)
                                    transformSingleFile(inputFile, destFile, srcDirPath)
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
                        transformDir(directoryInput.file, dest)
                    }
                }
            }
        }
        waitableExecutor.waitForTasksWithQuickFail<Any>(true)
    }

    private fun transformJar(srcJar: File, destJar: File, isNeedScan: Boolean) {
        waitableExecutor.execute {
            if (isNeedScan) {
                weaver.weaveJar(srcJar, destJar)
            } else {
                FileUtils.copyFile(srcJar, destJar)
            }
        }
    }

    private fun transformSingleFile(inputFile: File, outputFile: File, srcDir: String) {
        waitableExecutor.execute {
            weaver.weaveFile(inputFile, outputFile, srcDir)
        }
    }

    private fun transformDir(inputFile: File, outputFile: File) {
        val inputDirPath = inputFile.absolutePath
        val outputDirPath = outputFile.absolutePath
        if (inputFile.isDirectory) {
            com.android.utils.FileUtils.getAllFiles(inputFile).forEach {
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