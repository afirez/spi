package com.afirez.spi.component2;

import androidx.fragment.app.Fragment;

/**
 * https://github.com/afirez/spi
 */
public interface SpiRouter2Provider {

    SpiKotlinProvider spiKotlinProvider();

    Fragment spiFragment();

}
