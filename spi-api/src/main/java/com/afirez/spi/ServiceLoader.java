package com.afirez.spi;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * https://github.com/afirez/spi
 */
public class ServiceLoader {

    public static ServiceLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        static ServiceLoader INSTANCE = new ServiceLoader();
    }

    public static Class<?> androidxFragment;
    public static Class<?> v4Fragment;

    static {
        try {
            androidxFragment = Class.forName("androidx.fragment.app.Fragment");
            v4Fragment = Class.forName("android.support.v4.app.Fragment");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Map<Class<?>, Map<String, Class<?>>> serviceMap = new HashMap<>();

    private Map<Class<?>, Map<String, Object>> serviceInstanceMap = new HashMap<>();

    private ServiceLoader() {
        String path = "https://github.com/afirez/spi";
        addService(String.class, String.class, path);
    }

    public synchronized void addService(Class<?> serviceType, Class<?> serviceImplType, String path) {
        if (serviceType == null
                || serviceImplType == null
                || serviceImplType.isInterface()
                || !serviceType.isAssignableFrom(serviceImplType)) {
            return;
        }

        if (Activity.class.isAssignableFrom(serviceImplType)) {
            serviceType = Activity.class;
        } else if (androidxFragment != null && androidxFragment.isAssignableFrom(serviceImplType)) {
            serviceType = androidxFragment;
        } else if (v4Fragment != null && v4Fragment.isAssignableFrom(serviceImplType)) {
            serviceType = v4Fragment;
        }

        Map<String, Class<?>> map = serviceMap.get(serviceType);
        if (map == null) {
            map = new HashMap<>();
            serviceMap.put(serviceType, map);
        }

        if (path == null) {
            path = serviceType.getName();
        }
        map.put(path, serviceImplType);
    }

    public <T> Map<String, Class<?>> serviceTypes(Class<T> serviceType) {
        return serviceMap.get(serviceType);

    }

    public <T> Class<T> serviceType(Class<T> serviceType) {
        return serviceType(serviceType, serviceType.getName());
    }

    public <T> Class<T> serviceType(Class<T> serviceType, String path) {
        if (serviceType == null || path == null) {
            return null;
        }

        Map<String, Class<?>> serviceTypeMap = serviceMap.get(serviceType);
        if (serviceTypeMap == null) {
            return null;
        }

        return (Class<T>) serviceTypeMap.get(path);

    }

    public <T> Map<String, T> services(Class<T> serviceType) {
        Map<String, Class<?>> typeMap = serviceMap.get(serviceType);
        if (typeMap == null) {
            return new HashMap<>();
        }
        for (Map.Entry<String, Class<?>> entry : typeMap.entrySet()) {
            if (entry != null) {
                service(serviceType, entry.getKey());
            }
        }

        return (Map<String, T>) serviceInstanceMap.get(serviceType);
    }

    public <T> T service(Class<T> serviceType) {
        return service(serviceType, null);
    }


    public synchronized <T> T service(Class<T> serviceType, String path) {
        if (serviceType == null) {
            return null;
        }

        if (path == null) {
            path = serviceType.getName();
        }

        Object obj = null;
        Map<String, Object> objMap = serviceInstanceMap.get(serviceType);
        if (objMap != null && (obj = objMap.get(path)) != null) {
            return (T) obj;
        }


        Map<String, Class<?>> typeMap = serviceMap.get(serviceType);
        if (typeMap == null) {
            return null;
        }

        Class clz = typeMap.get(path);
        if (clz == null) {
            return null;
        }

        try {
            obj = clz.newInstance();
            if (objMap == null) {
                objMap = new HashMap<>();
                serviceInstanceMap.put(serviceType, objMap);
            }
            objMap.put(path, obj);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return (T) obj;

    }
}
