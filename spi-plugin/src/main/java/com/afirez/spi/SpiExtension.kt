package com.afirez.spi

import com.knight.transform.BaseExtension

/**
 * https://github.com/afirez/spi
 */
open class SpiExtension(
    var spiPath: String = "com/afirez/spi/SPI",
    var loaderPath: String = "com/afirez/spi/ExtensionLoader",
    var add: String = "addExtension"
) : BaseExtension()