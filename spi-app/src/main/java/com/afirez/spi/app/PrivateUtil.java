package com.afirez.spi.app;

import android.util.Log;

public class PrivateUtil {

    public static void reportPrivateApi(String hookedClass, String hookedMethod, String invokedClass, String invokedMethod) {
        Log.w("alphazz", "======hookedClass = " + hookedClass);
        Log.w("alphazz", "======hookedMethod = " + hookedMethod);
        Log.w("alphazz", "======invokedClass = " + invokedClass);
        Log.w("alphazz", "======invokedMethod = " + invokedMethod);
    }
}

