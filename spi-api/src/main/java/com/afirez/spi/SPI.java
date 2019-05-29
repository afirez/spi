package com.afirez.spi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * https://github.com/afirez/spi
 */
@Retention(RetentionPolicy.CLASS)
public @interface SPI {
   String path() default "";
}
