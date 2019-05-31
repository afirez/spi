package com.afirez.spi.component2.impl

import androidx.fragment.app.Fragment
import com.afirez.spi.ExtensionLoader
import com.afirez.spi.SPI
import com.afirez.spi.component2.SpiKotlinProvider
import com.afirez.spi.component2.SpiRouter2Provider

@SPI(path = "com.afirez.spi.component2.SpiRouter2Provider")
class SpiRouter2ProviderImpl : SpiRouter2Provider {

    override fun spiKotlinProvider(): SpiKotlinProvider? {
        return ExtensionLoader.getInstance().loadExtension<SpiKotlinProvider>("/spi/provider/kotlin")
    }

    override fun spiFragment(): Fragment? {
        val fragmentType = ExtensionLoader.getInstance().extension<Fragment>("/spi/fragment/spi")
        val fragment = fragmentType.newInstance() ?: null
        return fragment
    }
}
