package com.afirez.spi.component2.impl

import android.app.Application
import android.content.Context
import android.util.Log
import com.afirez.spi.SPI
import com.afirez.applike.AppLike

/**
 * https://github.com/afirez/spi
 */
@SPI
class App : AppLike {
    override fun attachBaseContext(app: Application?, base: Context?) {
        Log.d("App", "--> attachBaseContext(app = $app, base = $base) : $this")
    }

    override fun onCreate(app: Application?) {
        Log.d("App", "--> onCreate(app = $app) : $this")
    }

    override fun onTerminate(app: Application?) {
        Log.d("App", "--> onTerminate(app = $app) : $this")
    }
}