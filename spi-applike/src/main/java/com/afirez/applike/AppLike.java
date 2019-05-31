package com.afirez.applike;

import android.app.Application;
import android.content.Context;

/**
 * https://github.com/afirez/spi
 */
public interface AppLike {

    void attachBaseContext(Application app,Context base);

    void onCreate(Application app);

    void onTerminate(Application app) ;
}
