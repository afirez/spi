package com.afirez.applike;

import android.app.Application;
import android.content.Context;

/**
 * https://github.com/afirez/spi
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppDelegate.getInstance().attachBaseContext(this, base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.getInstance().onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDelegate.getInstance().onTerminate(this);
    }
}
