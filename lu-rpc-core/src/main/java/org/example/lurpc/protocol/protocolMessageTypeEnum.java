package org.example.lurpc.protocol;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: 鹿又笑
 * @Create: 2024/4/30 20:33
 * @description: 协议消息的类型枚举
 */
@Getter
public enum protocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    protocolMessageTypeEnum(int key) {
        this.key = key;
    }

    public static protocolMessageTypeEnum getEnumByKey(int key) {
        for (protocolMessageTypeEnum anEnum : protocolMessageTypeEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }


}
