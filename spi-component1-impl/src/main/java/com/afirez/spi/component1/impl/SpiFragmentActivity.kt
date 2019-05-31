package com.afirez.spi.component1.impl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afirez.spi.ExtensionLoader
import com.afirez.spi.SPI
import com.afirez.spi.component2.SpiRouter2Provider

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/activity/fragment")
class SpiFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_spi_fragment)

        showSpiFragment()
    }

    private fun showSpiFragment() {
        val path = "/spi/fragment/spi"
        val tag = path
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = ExtensionLoader.getInstance().loadExtension(SpiRouter2Provider::class.java).spiFragment()
        }

        if (fragment == null) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(android.R.id.content, fragment, tag)
        }
        transaction.commitAllowingStateLoss()
    }
}
