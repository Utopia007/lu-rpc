package org.example.lurpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import org.example.lurpc.protocol.ProtocolConstant;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/2 19:54
 * @description: 使用 recordParser 对原有的 buffer 处理能力进行增强  (装饰者模式)
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // 创建一个RecordParser实例，使用newFixed(8)指定每次处理8字节的数据块。RecordParser用于处理数据流，帮助解决粘包问题
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);
        // 设置解析器的输出处理器，处理解析后的数据,Handler<Buffer>，用于处理解析后的数据块
        parser.setOutput(new Handler<Buffer>() {
            // 初始化
            int size = -1;
            // 一次完整的读取(头+体)
            Buffer resultBuffer = Buffer.buffer();

            // handle方法会在每次有新的数据块（Buffer）可处理时被调用
            @Override
            public void handle(Buffer buffer) {
                // 如果size == -1，说明当前正在读取消息头，从第四个字节开始获取消息体长度
                if (size == -1) {
                    // 读取消息体长度
                    size = buffer.getInt(13);
                    // 设置parser的模式为固定长度模式，长度为消息体长度
                    parser.fixedSizeMode(size);
                    // 写入头信息到结果
                    resultBuffer.appendBuffer(buffer);
                } else {
                    // 否则，意味着正在读取消息体，将当前数据块追加到resultBuffer，然后打印出完整消息内容
                    resultBuffer.appendBuffer(buffer);
//                    System.out.println(resultBuffer.toString());
                    // 已拼接为完整Buffer，执行处理
                    bufferHandler.handle(resultBuffer);
                    // 重置一轮
                    parser.fixedSizeMode(8);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });
        return parser;
    }
}
