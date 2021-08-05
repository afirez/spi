# spi

[中文](README_CN.md)
> SPI for componentization.

## Download

Add **spi-gradle-plugin** to your project !

```

buildscript {
  repositories {
    jcenter()

    // add maven repository for spi-plugin at build.gradle file of root project
    maven { url "https://raw.githubusercontent.com/afirez/spi/master/repo/" }
  }

  dependencies {
    classpath 'com.afirez.spi:spi-gradle-plugin:1.0.1'
  }
}

// in module build.gradle
apply plugin: 'com.android.application'
apply plugin: 'spi' 
// or apply plugin: 'com.afirez.spi'

```

Add **spi** to module project if needed !

```
allprojects {
  repositories {
    ...

    // add maven repository for spi at build.gradle file of root project
    maven { url "https://raw.githubusercontent.com/afirez/spi/master/repo/" }

    ...
  }
}
```


```

implementation "com.afirez.spi:spi:1.0.1" 

```

## Usage

### SPI AppLike

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f4c0b6821?w=1252&h=946&f=png&s=193865)

Make every module aware of Application lifecycle by annotated with @SPI, it will be discovered an registered to ExtensionLoader. eg:

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f4c026017?w=1632&h=1272&f=png&s=299116)

Then, AppDelegate will load and dispatch AppLikes by ExtensionLoader.

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f51b66ed3?w=2018&h=1774&f=png&s=329836)

Call AppDelegate in App and don't forget to register App to AndoridManifest.xml. Enjoy it!
  
![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f55447fa2?w=1374&h=1048&f=png&s=234748)

### SPI Actiivty

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f51c034a6?w=2040&h=1192&f=png&s=333383)

Annotate Activity with @SPI with path so that it will be discovered and registered to ExtensionLoader, then it can be loaded by ExtensionLoader with path. (if it annotated without path, path will be it's class name.)

### SPI Fragment

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f7f338ee5?w=1932&h=1666&f=png&s=400667)

Annotate Fragment with @SPI with path so that it will be discovered and registered to ExtensionLoader, then it can be loaded by ExtensionLoader with path. (if it annotated without path, path will be it's class name.)

### SPI Provider

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f89aab5fc?w=1930&h=1012&f=png&s=246460)

Annotate SpiKotlinProviderImpl with @SPI with path so that it will be discovered and registered to ExtensionLoader, then it can be loaded by ExtensionLoader with path.(if it annotated without path, path will be it's class name.)

### Do more by SPI

SPI focuses on interface discovery, registration and routing table implementation. So We can collect and dispath interface like AppLike, or make a router implementation based on SPI, and etc.

## Thanks

- [ASM](https://asm.ow2.io/)
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