package com.knight.transform.Interceptor

import org.gradle.api.GradleException

class ClassVisitorChain(val index: Int, val interceptors: List<IClassVisitorInterceptor>, val params: ClassVisitorParams) : Chain {


    override fun transform(): ByteArray {
        if (index >= interceptors.size) {
            return params.inputByte
        }

        val next = ClassVisitorChain(index + 1, interceptors, params)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }

    override fun request(): ClassVisitorParams {
        return params
    }


}