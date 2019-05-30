package com.afirez.spi.component2

import com.afirez.spi.SPI
import com.afirez.spi.component2.api.HelloKotlin

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/provider/hello/kotlin")
class HelloKotlinImpl : HelloKotlin {
    override fun helloKotlin(): String {
        return "helloKotlin"
    }
}
