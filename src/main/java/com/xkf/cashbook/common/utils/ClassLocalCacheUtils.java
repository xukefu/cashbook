package com.xkf.cashbook.common.utils;

import java.util.concurrent.ConcurrentHashMap;

public class ClassLocalCacheUtils {

    private final static ConcurrentHashMap<String, Class<?>> clazzMap = new ConcurrentHashMap();

    public static Class<?> getClazz(String clazzName) throws ClassNotFoundException {
        Class<?> clazz = clazzMap.get(clazzName);
        if (clazz == null) {
            clazz = Class.forName(clazzName);
            clazzMap.put(clazzName, clazz);
        }
        return clazz;
    }

}
