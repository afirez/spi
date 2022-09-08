package com.afirez.spi.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.afirez.applike.AppDelegate;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
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
