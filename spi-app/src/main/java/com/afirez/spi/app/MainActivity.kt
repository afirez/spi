package com.afirez.spi.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.afirez.spi.ServiceLoader
import com.afirez.spi.component1.api.HelloJava
import com.afirez.spi.component2.api.HelloKotlin


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
