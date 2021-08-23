package com.knight.transform.Interceptor


interface Chain {
    fun transform(): ByteArray
    fun request(): ClassVisitorParams;
}

interface IClassVisitorInterceptor {
    fun intercept(chain: Chain): ByteArray
}

data class ClassVisitorParams(var inputByte: ByteArray, var classloader: ClassLoader? = null)