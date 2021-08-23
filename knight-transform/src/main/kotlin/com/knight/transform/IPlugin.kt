package com.knight.transform

import com.knight.transform.Interceptor.IClassVisitorInterceptor
import com.knight.transform.asm.IWeaver
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

interface IPlugin {
    fun createWeaveClassVisitor(classWriter: ClassWriter): ClassVisitor

    fun createScanClassVisitor(classWriter: ClassWriter): ClassVisitor?

    // 是否需要预先扫描class文件
    fun isNeedScanClass(): Boolean

    // 是否需要扫描R文件
    fun isNeedScanWeaveRClass(): Boolean

    fun getScanClassVisitorInterceptor(): List<IClassVisitorInterceptor>?
    fun getWeaveClassVisitorInterceptor(): List<IClassVisitorInterceptor>?

}