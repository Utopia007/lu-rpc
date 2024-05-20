package org.example.lurpc.registry;

import org.example.lurpc.spi.SpiLoader;

/**
 * @auther : LuYouxiao
 * @date 2024/4/16   -21:49
 * @Description
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    public static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class, key);
    }

}
