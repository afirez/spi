package com.afirez.spi;

import android.app.Activity;
import android.app.Fragment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://github.com/afirez/spi
 */
public class ExtensionLoader {

    public static ExtensionLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        static ExtensionLoader INSTANCE = new ExtensionLoader();
    }

    private static Class<?> androidxFragment;
    private static Class<?> v4Fragment;

    public static Class<?> fragmentType() {
        return v4Fragment != null ? v4Fragment : androidxFragment;
    }

    static {
        try {
            androidxFragment = Class.forName("androidx.fragment.app.Fragment");
            v4Fragment = Class.forName("android.support.v4.app.Fragment");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final Map<String, Class<?>> extensionMapByPath = new ConcurrentHashMap<>();

    private final Map<Class<?>, Map<String, Class<?>>> extensionsMap = new ConcurrentHashMap<>();

    private final Map<Class<?>, Map<String, Object>> extensionsInstanceMap = new ConcurrentHashMap<>();

    private ExtensionLoader() {
        String path = "https://github.com/afirez/spi";
        addExtension(String.class, String.class, path);
    }

    public void addExtension(Class<?> type, Class<?> extension, String path) {
        if (type == null
                || extension == null
                || extension.isInterface()
                || !type.isAssignableFrom(extension)) {
            return;
        }

        if (Activity.class.isAssignableFrom(extension)) {
            type = Activity.class;
        } else if (Fragment.class.isAssignableFrom(extension)) {
            type = Fragment.class;
        } else if (androidxFragment != null && androidxFragment.isAssignableFrom(extension)) {
            type = androidxFragment;
        } else if (v4Fragment != null && v4Fragment.isAssignableFrom(extension)) {
            type = v4Fragment;
        }

        Map<String, Class<?>> extensionMap = extensionsMap.get(type);
        if (extensionMap == null) {
            synchronized (extensionsMap) {
                extensionMap = extensionsMap.get(type);
                if (extensionMap == null) {
                    extensionMap = new ConcurrentHashMap<>();
                    extensionsMap.put(type, extensionMap);
                }
            }
        }

        if (path == null) {
            path = type.getName();
        }
        extensionMap.put(path, extension);

        extensionMapByPath.put(path, type);
    }

    public <T> Map<String, Class<?>> extensions(Class<T> type) {
        return extensionsMap.get(type);

    }

    public <T> Class<T> extension(String path) {
        Class<?> type = extensionMapByPath.get(path);
        return (Class<T>) extension(type, path);
    }


    public <T> Class<T> extension(Class<T> type) {
        return extension(type, type.getName());
    }

    public <T> Class<T> extension(Class<T> type, String path) {
        if (type == null || path == null) {
            return null;
        }

        Map<String, Class<?>> extensionMap = extensionsMap.get(type);
        if (extensionMap == null) {
            return null;
        }

        return (Class<T>) extensionMap.get(path);

    }

    public <T> Map<String, T> loadExtensions(Class<T> type) {
        Map<String, Class<?>> extensionMap = extensionsMap.get(type);
        if (extensionMap == null) {
            return new ConcurrentHashMap<>();
        }

        for (Map.Entry<String, Class<?>> entry : extensionMap.entrySet()) {
            if (entry != null) {
                loadExtension(type, entry.getKey());
            }
        }

        return (Map<String, T>) extensionsInstanceMap.get(type);
    }


    public <T> T loadExtension(String path) {
        Class<?> type = extensionMapByPath.get(path);

        return (T) loadExtension(type, path);
    }

    public <T> T loadExtension(Class<T> type) {
        return loadExtension(type, null);
    }

    public <T> T loadExtension(Class<T> type, String path) {
        if (type == null
                || Activity.class.isAssignableFrom(type)
                || Fragment.class.isAssignableFrom(type)
                || (androidxFragment != null && androidxFragment.isAssignableFrom(type))
                || (v4Fragment != null && v4Fragment.isAssignableFrom(type))) {
            return null;
        }

        if (path == null) {
            path = type.getName();
        }

        Object obj = null;
        Map<String, Object> objMap = extensionsInstanceMap.get(type);
        if (objMap != null && (obj = objMap.get(path)) != null) {
            return (T) obj;
        }


        Map<String, Class<?>> extensionMap = extensionsMap.get(type);
        if (extensionMap == null) {
            return null;
        }

        Class clz = extensionMap.get(path);
        if (clz == null) {
            return null;
        }

        if (objMap == null) {
            synchronized (extensionsInstanceMap) {
                objMap = extensionsInstanceMap.get(type);
                if (objMap == null) {
                    objMap = new ConcurrentHashMap<>();
                    extensionsInstanceMap.put(type, objMap);
                }
            }
        }

        synchronized (extensionsInstanceMap) {
            obj = objMap.get(path);
            if ((obj == null)) {
                try {
                    obj = clz.newInstance();
                    objMap.put(path, obj);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

        }

        return (T) obj;

    }
}
