# spi

[中文](README_CN.md)
> SPI for componentization.

## Download

Add **spi-gradle-plugin** to your project !

```

buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.afirez.spi:spi-gradle-plugin:1.0.0'
  }
}

// in module build.gradle
apply plugin: 'com.android.application'
apply plugin: 'spi' 
// or apply plugin: 'com.afirez.spi'

```

Add **spi** to module project if needed !

```

implementation "com.afirez.spi:spi:1.0.0" 

```

## Usage

### SPI AppLike

![](https://afirez.oss-cn-hangzhou.aliyuncs.com/c16699dc9a57754511eb275be6113d55.png)

Make every module aware of Application lifecycle by annotated with @SPI, it will be discovered an registered to ExtensionLoader. eg:

![](https://afirez.oss-cn-hangzhou.aliyuncs.com/b555d9f281ad5e5c7ab57e906ade61a9.png)

Then, AppDelegate will load and dispatch AppLikes by ExtensionLoader.

![](https://afirez.oss-cn-hangzhou.aliyuncs.com/9e6bdaabcc2105fad026439f38bcae69.png)

Call AppDelegate in App and don't forget to register App to AndoridManifest.xml. Enjoy it!
  
![](https://afirez.oss-cn-hangzhou.aliyuncs.com/3ffa66bf5abc91bf9644ebb08b5008d6.png)

### SPI Actiivty

![](https://afirez.oss-cn-hangzhou.aliyuncs.com/00adc899f846523757102d5df66538d3.png)

Annotate Activity with @SPI with path so that it will be discovered and registered to ExtensionLoader, then it can be loaded by ExtensionLoader with path. (if it annotated without path, path will be it's class name.)

### SPI Fragment

![](https://afirez.oss-cn-hangzhou.aliyuncs.com/8030bb05943d521dcecb0b1915fc06f1.png)

Annotate Fragment with @SPI with path so that it will be discovered and registered to ExtensionLoader, then it can be loaded by ExtensionLoader with path. (if it annotated without path, path will be it's class name.)

### SPI Provider

![](https://afirez.oss-cn-hangzhou.aliyuncs.com/3584f9da90a8769849d69e13c667f555.png)

Annotate SpiKotlinProviderImpl with @SPI with path so that it will be discovered and registered to ExtensionLoader, then it can be loaded by ExtensionLoader with path.(if it annotated without path, path will be it's class name.)

### Do more by SPI

SPI focuses on interface discovery, registration and routing table implementation. So We can collect and dispath interface like AppLike, or make a router implementation based on SPI, and etc.

## Thanks

- [ASM](https://asm.ow2.io/): a very small and fast Java bytecode manipulation framework
- [hunter](https://github.com/Leaking/Hunter)
- [KnightTransform](https://github.com/kakayang2011/KnightTransform)

## License


    Copyright 2019 afirez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.