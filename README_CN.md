# 你可能需要 SPI 了

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f4aab6d3f?w=1906&h=1408&f=png&s=261001)

## 为什么会开源这个 SPI 项目 ？

在项目组件化的过程中，曾通过 AndoridManifest.xml 注册的方式实现过 Application 生命周期的组件化，类似 Glide 中解析 AndoridManifest.xml 发现 GlideModule 的机制，有点繁琐。而后了解到 Google 的 AutoService，通过注解加 Java 原生 SPI，实现 Appcation 生命周期的组件化，好像刚刚好。SPI 可以没有，没有 SPI，可以用其他方案，比如说路由。首先，一般路由基本在提供路由该提供的功能，路由发现和注册机制局限在路由框架内部，不会提供 SPI，如果用路由实现，每添加一个需要 Application 生命周期的组件都需要通过在 Application 中添加代码，不是那么好维护。SPI 也可以到处都是，随着项目组件化后，发现很多地方都需要用到 SPI 机制，AutoService 加原生 SPI 机制，除了有反射还有IO，在接口非常多的时候，比较耗时。而 SPI 机制是遵循开闭原则的，这个事情也是在脑海里留下了不可描述的印象......于是慢慢的，才有了这个 SPI 项目。

## 在项目中引入 [SPI](https://github.com/afirez/spi)

添加 **spi-gradle-plugin** 插件到你的项目

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

添加 **spi** 到需要的子模块

```
allprojects {
  repositories {
    ...

    // add maven repository for spi at build.gradle file of root project
    maven { url "https://raw.githubusercontent.com/afirez/spi/master/repo/" }

    ...
  }

```

```

implementation "com.afirez.spi:spi:1.0.1"
```

## 如何使用 SPI

### 1、从 SPI AppLike 谈起

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f4c0b6821?w=1252&h=946&f=png&s=193865)

实现 AppLike 接口，并添加 @SPI 注解，该实现者 App 将会在编译时被发现和注册到 ExtensionLoader 中，如下图所示：

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f4c026017?w=1632&h=1272&f=png&s=299116)

然后, AppDelegate 可以通过 ExtensionLoader 拿到所有 AppLike 的实现者并分发 Application 的生命周期。如下图所示：

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f51b66ed3?w=2018&h=1774&f=png&s=329836)

最后，记得在项目的 App 中调用 AppDelegate 相应方法（attachBaseContext，onCreate，onTerminate 等方法），这样其他模块便有了 Application 的生命周期。
  
![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f55447fa2?w=1374&h=1048&f=png&s=234748)

### 2、SPI Actiivty

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f51c034a6?w=2040&h=1192&f=png&s=333383)

被 @SPI 注解过的 Activity ，会在编译期被发现并注册到 ExtensionLoader，然后可以通过 ExtensionLoader 获取指定路径 path 的 Activity 类。（如果 @SPI 中没有路径 path 的值，路径 path 的值将会是被注解类的包名加类名）

### 3、SPI Fragment

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f7f338ee5?w=1932&h=1666&f=png&s=400667)

被 @SPI 注解过的 Fragment，会在编译期被发现并注册到 ExtensionLoader，然后可以通过 ExtensionLoader 获取指定路径 path 的 Fragment 类。（如果 @SPI 中没有路径 path 的值，路径 path 的值将会是被注解类的包名加类名）

### 4、SPI Provider

![](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f89aab5fc?w=1930&h=1012&f=png&s=246460)

被 @SPI 注解过的 SpiKotlinProviderImpl，同样会在编译期被发现并注册到 ExtensionLoader，然后可以通过 ExtensionLoader 获取指定路径 path 的 SpiKotlinProvider 实现类。（如果 @SPI 中没有路径 path 的值，路径 path 的值将会是被注解类的包名加类名）

### 5、用 SPI 搞更多事情

SPI 是一个方便接口聚合与分发的工具，很多接口扩展的场景都需要用到，让我们的代码遵循开闭原则！例如 AppLike 实现 Application 生命周期组件化，请参考 AppLike 等用例自行扩展。

SPI 其实本身已有一个路由表，可以为接口或类（类支持 Activity 和 Fragment）的实现类指定 path 路径，方便我们拿到指定 path 的 实现类。如果你想自己实现组件化的路由，完全可以基于 spi 实现

SPI 聚焦于接口发现与注册和路由表，仅仅基于 SPI 实现 Android 组件化，模块解偶很彻底，但是缺少路由的一些功能，后续考虑开源一个路由项目，敬请期待！

去 [spi](https://github.com/afirez/spi) github 下载源码试试， 了解更多细节。

## 感谢

- [ASM](https://asm.ow2.io/)
- [hunter](https://github.com/Leaking/Hunter)
- [KnightTransform](https://github.com/kakayang2011/KnightTransform)

![afirez](https://user-gold-cdn.xitu.io/2019/6/1/16b13c2f917705f9?w=200&h=200&f=jpeg&s=20853)