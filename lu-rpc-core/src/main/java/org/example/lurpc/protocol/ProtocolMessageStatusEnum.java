package org.example.lurpc.protocol;

import lombok.Getter;

import java.util.PrimitiveIterator;

/**
 * @Author: 鹿又笑
 * @Create: 2024/4/30 20:24
 * @description: 协议消息的状态枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok", 20),
    BAD_REQUEST("badRequest", 40),
    BAD_RESPONSE("badResponse", 50);

    private final String key;

    private final int value;

    ProtocolMessageStatusEnum(String key, int value){
        this.key = key;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

}
