package org.example.lurpc.protocol;

/**
 * @Author: 鹿又笑
 * @Create: 2024/4/30 20:18
 * @description: 协议常量
 */
public class protocolConstant {

    /**
     * 消息头长度   8+8+8+8+8+64+32
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    int PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    int PROTOCOL_VERSION = 0x1;




}
