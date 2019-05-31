package com.afirez.spi.component1.impl;

import com.afirez.spi.ExtensionLoader;
import com.afirez.spi.SPI;
import com.afirez.spi.component1.SpiJavaProvider;
import com.afirez.spi.component2.SpiRouter2Provider;

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/provider/java")
public class SpiJavaProviderImpl implements SpiJavaProvider {

    @Override
    public String helloJava() {
        SpiRouter2Provider spiRouter2Provider = ExtensionLoader.getInstance().loadExtension(SpiRouter2Provider.class);
        String kotlin = spiRouter2Provider != null && spiRouter2Provider.spiKotlinProvider() != null
                ? spiRouter2Provider.spiKotlinProvider().helloKotlin() : "";
        return "helloJava --> " + kotlin;
    }
}
