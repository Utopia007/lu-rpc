package org.example.lurpc.protocol;

import io.vertx.core.buffer.Buffer;
import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;
import org.example.lurpc.serializer.Serializer;
import org.example.lurpc.serializer.SerializerFactory;

import java.io.IOException;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/1 16:06
 * @description: 实现消息解码器
 */
public class ProtocolMessageDecoder {

    /**
     * 解码
     * @param buffer
     * @return
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        // 分别从指定位置读出buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        // 校验魔数
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        // 解决粘包问题  =》 只读取指定长度的数据
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        // 解析消息体
        ProtocolMessageSerializeEnum serializeEnum = ProtocolMessageSerializeEnum.getEnumByKey(header.getSerializer());
        if (serializeEnum == null) {
            throw new RuntimeException("序列化消息的协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializeEnum.getValue());
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("序列化消息的类型不存在");
        }
        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest request = serializer.deSerialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deSerialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }

    }

}
