package com.afirez.spi.component1;

import com.afirez.spi.ExtensionLoader;
import com.afirez.spi.SPI;
import com.afirez.spi.component1.api.HelloJava;
import com.afirez.spi.component2.api.HelloKotlin;

@SPI(path = "/spi/provider/hello/java")
public class HelloJavaImpl implements HelloJava {

     @Override
     public String helloJava() {
          ExtensionLoader.getInstance().loadExtension(HelloKotlin.class).helloKotlin();
          return "helloJava";
     }
}
