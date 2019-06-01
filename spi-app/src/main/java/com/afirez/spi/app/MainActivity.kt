package com.afirez.spi.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afirez.spi.ExtensionLoader
import com.afirez.spi.SPI
import com.afirez.spi.component1.SpiRouter1Provider
import com.afirez.spi.component2.SpiRouter2Provider
import kotlinx.android.synthetic.main.activity_main.*

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/activity/main")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv01AppLike.setOnClickListener {
            /**
             * @see com.afirez.applike.AppLike
             */
            Toast.makeText(this@MainActivity, "@see com.afirez.applike.AppLike", Toast.LENGTH_SHORT).show()
        }

        tv02Activity.setOnClickListener {
            /**
             * @see com.afirez.spi.component1.impl.SpiActivity
             */
            ExtensionLoader.getInstance().loadExtension(SpiRouter1Provider::class.java).navSpiActivity()
        }

        tv03Fragment.setOnClickListener {
            /**
             * @see com.afirez.spi.component2.impl.SpiFragment
             */
            ExtensionLoader.getInstance().loadExtension(SpiRouter1Provider::class.java).navSpiFragmentActivity()
        }

        tv04Provider.setOnClickListener {
            /**
             * @see com.afirez.spi.component1.SpiJavaProvider
             */
            val spiJavaProvider =
                ExtensionLoader.getInstance().loadExtension(SpiRouter1Provider::class.java).spiJavaProvider()
            if (spiJavaProvider != null) {
                val helloJava = spiJavaProvider.helloJava()
                Toast.makeText(this@MainActivity, "spi $helloJava", Toast.LENGTH_SHORT).show()
            }
            /**
             * @see com.afirez.spi.component2.SpiKotlinProvider
             */
            val spiKotlinProvider =
                ExtensionLoader.getInstance().loadExtension(SpiRouter2Provider::class.java).spiKotlinProvider()
            if (spiJavaProvider != null) {
                val helloKotlin = spiKotlinProvider.helloKotlin()
                tv04Provider.postDelayed({
                    Toast.makeText(this@MainActivity, "spi $helloKotlin", Toast.LENGTH_SHORT).show()
                }, 1000)
            }
        }

        tv05RouterProvider.setOnClickListener {
            /**
             * @see com.afirez.spi.component1.SpiRouter1Provider
             */
            Toast.makeText(
                this@MainActivity,
                "@see com.afirez.spi.component1.SpiRouter1Provider",
                Toast.LENGTH_SHORT
            ).show()


            /**
             * @see com.afirez.spi.component2.SpiRouter2Provider
             */
            tv05RouterProvider.postDelayed({
                Toast.makeText(
                    this@MainActivity,
                    "@see com.afirez.spi.component2.SpiRouter2Provider",
                    Toast.LENGTH_SHORT
                ).show()
            }, 1000)
        }

        tv05More.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "1 、 SPI 是一个方便接口聚合与分发的工具，很多接口扩展的场景都需要用到， 让我们的代码遵循开闭原则 ！例如 AppLike 实现 Application 生命周期分发， 请参考 AppLike 和 Provider 等用例自行扩展。\n"
                        + "2、 SPI 本身已有一个路由表，可以为接口或类（类支持 Activity 和 Fragment ）的实现类指定 path 路径 ， 方便我们拿到指定 path 的 实现类 。 如果你想自己实现组件化的路由， 完全可以基于 spi 实现 \n"
                        + "3、 SPI 聚焦于接口发现与注册和路由表实现， 仅仅基于 SPI 实现 Android 组件化， 模块解偶很彻底， 但是缺少路由的一些功能，后续考虑开源一个路由项目，敬请期待！",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
