package org.example.lurpc.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 鹿又笑
 * @Create: 2024/4/30 20:39
 * @description: 协议消息的序列化枚举
 */
@Getter
public enum protocolMessageSerializeEnum {

    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian");

    private final int key;

    private final String value;

    protocolMessageSerializeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public static protocolMessageSerializeEnum getEnumByKey(int key) {
        for (protocolMessageSerializeEnum anEnum : protocolMessageSerializeEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据value获取值
     * @param value
     * @return
     */
    public static protocolMessageSerializeEnum getEnumByValue(String value) {
        for (protocolMessageSerializeEnum anEnum : protocolMessageSerializeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
