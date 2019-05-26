package com.afirez.spi.component1;

import com.afirez.spi.ServiceLoader;
import com.afirez.spi.Spi;
import com.afirez.spi.component1.api.HelloJava;
import com.afirez.spi.component2.api.HelloKotlin;

@Spi
public class HelloJavaImpl implements HelloJava {

     @Override
     public String helloJava() {
          ServiceLoader.getInstance().service(HelloKotlin.class).helloKotlin();
          return "helloJava";
     }
}
