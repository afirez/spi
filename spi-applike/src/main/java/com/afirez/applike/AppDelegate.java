package com.afirez.applike;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.afirez.spi.ExtensionLoader;

import java.util.Map;
import java.util.Set;

/**
 * https://github.com/afirez/spi
 */
public class AppDelegate {


    public static AppDelegate getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        static AppDelegate INSTANCE = new AppDelegate();
    }

    private Application app;

    private Activity topActivity;

    public Application app() {
        return app;
    }

    public Activity topActivity() {
        return topActivity;
    }

    public Context topActivityOrApp() {
        return topActivity == null ? app : topActivity;
    }

    public void attachBaseContext(Application app, Context base) {
        this.app = app;
        Map<String, AppLike> callbacksMap = ExtensionLoader.getInstance().loadExtensions(AppLike.class);
        if (callbacksMap == null) {
            return;
        }
        Set<Map.Entry<String, AppLike>> entries = callbacksMap.entrySet();

        for (Map.Entry<String, AppLike> entry : entries) {
            if (entry == null) {
                continue;
            }

            AppLike callbacks = entry.getValue();
            if (callbacks == null) {
                continue;
            }

            callbacks.attachBaseContext(app, base);
        }
    }

    public void onCreate(Application app) {
        this.app = app;
        app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        Map<String, AppLike> callbacksMap = ExtensionLoader.getInstance().loadExtensions(AppLike.class);
        if (callbacksMap == null) {
            return;
        }

        Set<Map.Entry<String, AppLike>> entries = callbacksMap.entrySet();

        for (Map.Entry<String, AppLike> entry : entries) {
            if (entry == null) {
                continue;
            }

            AppLike callbacks = entry.getValue();
            if (callbacks == null) {
                continue;
            }

            callbacks.onCreate(app);
        }
    }


    public void onTerminate(Application app) {
        Map<String, AppLike> callbacksMap = ExtensionLoader.getInstance().loadExtensions(AppLike.class);
        if (callbacksMap == null) {
            return;
        }
        Set<Map.Entry<String, AppLike>> entries = callbacksMap.entrySet();

        for (Map.Entry<String, AppLike> entry : entries) {
            if (entry == null) {
                continue;
            }

            AppLike callbacks = entry.getValue();
            if (callbacks == null) {
                continue;
            }

            callbacks.onTerminate(app);
        }
    }


    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            topActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            topActivity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}
