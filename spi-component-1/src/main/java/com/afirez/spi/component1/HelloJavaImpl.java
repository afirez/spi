package com.afirez.spi.component1;

import com.afirez.spi.ExtensionLoader;
import com.afirez.spi.SPI;
import com.afirez.spi.component1.api.HelloJava;
import com.afirez.spi.component2.api.HelloKotlin;

@SPI(path = "/spi/provider/hello/java")
public class HelloJavaImpl implements HelloJava {

    @Override
    public String helloJava() {
        HelloKotlin helloKotlin = ExtensionLoader.getInstance().loadExtension("/spi/provider/hello/kotlin");
        String kotlin = helloKotlin != null ? helloKotlin.helloKotlin() : "";
        return "helloJava --> " + kotlin;
    }
}
