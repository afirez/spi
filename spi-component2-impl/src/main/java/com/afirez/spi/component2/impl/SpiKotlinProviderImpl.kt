package com.afirez.spi.component2.impl

import com.afirez.spi.SPI
import com.afirez.spi.component2.SpiKotlinProvider

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/provider/kotlin")
class SpiKotlinProviderImpl : SpiKotlinProvider {
    override fun helloKotlin(): String {
        return "helloKotlin"
    }
}
