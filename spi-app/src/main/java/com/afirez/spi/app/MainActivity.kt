package com.afirez.spi.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afirez.spi.ServiceLoader
import com.afirez.spi.Spi
import com.afirez.spi.component1.api.HelloJava
import com.afirez.spi.component2.api.HelloKotlin
import kotlinx.android.synthetic.main.activity_main.*

/**
 * https://github.com/afirez/spi
 */
@Spi(path = "/spi/activity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHelloWorld.setOnClickListener {
            val helloJava = ServiceLoader.getInstance().service(HelloJava::class.java)
            if (helloJava != null) {
                val msg = helloJava.helloJava()
                Log.w("ServiceLoader", msg)
            }

            val helloKotlin = ServiceLoader.getInstance().service(HelloKotlin::class.java)
            if (helloKotlin != null) {
                val msg = helloKotlin.helloKotlin()
                Log.w("ServiceLoader", msg)
            }
        }

        val tag = "/spi/androidxfragment"
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            val fragmentType = ServiceLoader.getInstance().serviceType(Fragment::class.java, tag)
            fragment = fragmentType.newInstance()
        }

        if (fragment == null) {
            return
        }

        var transaction = supportFragmentManager.beginTransaction()
        if (fragment!!.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.flContainer, fragment, tag)
        }
        transaction.commitAllowingStateLoss()
    }
}
