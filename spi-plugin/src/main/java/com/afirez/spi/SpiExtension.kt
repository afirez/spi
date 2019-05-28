package com.afirez.spi

import com.knight.transform.BaseExtension

/**
 * https://github.com/afirez/spi
 */
open class SpiExtension(
    var spiPath: String = "com/afirez/spi/Spi",
    var serviceLoaderPath: String = "com/afirez/spi/ServiceLoader",
    var addService: String = "addService"
) : BaseExtension()