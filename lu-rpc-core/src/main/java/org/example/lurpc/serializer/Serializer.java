package org.example.lurpc.serializer;

import java.io.IOException;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -17:30
 * @Description
 */
public interface Serializer {

    /**
     * 序列化
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deSerialize(byte[] bytes, Class<T> type) throws IOException;
}
