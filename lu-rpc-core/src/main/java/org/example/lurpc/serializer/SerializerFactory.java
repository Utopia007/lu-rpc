package org.example.lurpc.serializer;

import org.example.lurpc.spi.SpiLoader;

/**
 * @auther : LuYouxiao
 * @date 2024/4/12   -16:36
 * @Description
 */
public class SerializerFactory {

    /**
     * 序列化映射
     */
//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
//        put(SerializerKeys.JDK, new JDKSerializer());
//        put(SerializerKeys.JSON, new JsonSerializer());
//        put(SerializerKeys.HESSIAN, new HessianSerializer());
//        put(SerializerKeys.KRYO, new HessianSerializer());
//    }};
    static {
        SpiLoader.load(Serializer.class);
    }



    /**
     * 默认实例
     */
//    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");
    private static final Serializer DEFAULT_SERIALIZER = new JDKSerializer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){

        // 懒加载 serialize
//        SpiLoader.load(Serializer.class);

//        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
