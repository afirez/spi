package com.afirez.spi.component2

import com.afirez.spi.Spi
import com.afirez.spi.component2.api.HelloKotlin

/**
 * https://github.com/afirez/spi
 */
@Spi
class HelloKotlinImpl: HelloKotlin {
    override fun helloKotlin(): String {
        return "helloKotlin"
    }
}
