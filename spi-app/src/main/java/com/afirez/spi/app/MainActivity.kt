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

    }
}
