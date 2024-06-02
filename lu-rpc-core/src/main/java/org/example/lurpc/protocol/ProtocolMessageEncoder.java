package org.example.lurpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.example.lurpc.serializer.Serializer;
import org.example.lurpc.serializer.SerializerFactory;

import java.io.IOException;

/**
 * @Author: 鹿又笑
 * @Create: 2024/5/31 16:25
 * @description: 消息编码器
 */
public class ProtocolMessageEncoder {

    /**
     * 编码
     * @param message
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> message) throws IOException {
        if (message == null || message.getHeader() == null) {
            return Buffer.buffer();
        }
        ProtocolMessage.Header header = message.getHeader();
        // 向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
        // 获取序列化器
        ProtocolMessageSerializeEnum serializeEnum = ProtocolMessageSerializeEnum.getEnumByKey(header.getSerializer());
        System.out.println(serializeEnum);
        if (serializeEnum == null) {
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializeEnum.getValue());
        byte[] bodyBytes = serializer.serialize(message.getBody());
        // 写入body长度和数据
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }

}
