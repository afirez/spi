package com.afirez.spi.component1.impl;

import android.content.Context;
import android.content.Intent;
import com.afirez.spi.ExtensionLoader;
import com.afirez.spi.SPI;
import com.afirez.applike.AppDelegate;
import com.afirez.spi.component1.SpiJavaProvider;
import com.afirez.spi.component1.SpiRouter1Provider;

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "com.afirez.spi.component1.SpiRouter1Provider")
public class SpiRouter1ProviderImpl implements SpiRouter1Provider {


    @Override
    public void navSpiActivity() {
        Context context = AppDelegate.getInstance().topActivityOrApp();
        Intent intent = new Intent(context, SpiActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void navSpiFragmentActivity() {
        Context context = AppDelegate.getInstance().topActivityOrApp();
        Intent intent = new Intent(context, SpiFragmentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public SpiJavaProvider spiJavaProvider() {
        return ExtensionLoader.getInstance().loadExtension("/spi/provider/java");
    }
}
