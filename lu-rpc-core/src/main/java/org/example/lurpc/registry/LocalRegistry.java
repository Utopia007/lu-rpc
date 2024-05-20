package org.example.lurpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -16:36
 * @Description 本地注册中心
 *
 * 使用线程安全的 ConcurrentHashMap 存储服务注册信息，key 为服务名称、value 为服务的实现类
 */
public class LocalRegistry {

    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName
     * @return
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }

}
