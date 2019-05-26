package com.afirez.spi.component2

import com.afirez.spi.Spi
import com.afirez.spi.component2.api.HelloKotlin

@Spi
class HelloKotlinImpl: HelloKotlin {
    override fun helloKotlin(): String {
        return "helloKotlin"
    }
}
