package org.example.lurpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import org.example.lurpc.protocol.ProtocolConstant;
import org.example.lurpc.server.HttpServer;

/**
 * @Author: 鹿又笑
 * @Create: 2024/5/30 17:59
 * @description:
 */
public class VertxTCPServer implements HttpServer {

    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(socket -> {
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
                        size = buffer.getInt(4);
                        // 设置parser的模式为固定长度模式，长度为消息体长度
                        parser.fixedSizeMode(size);
                        // 写入头信息到结果
                        resultBuffer.appendBuffer(buffer);
                    }else {
                        // 否则，意味着正在读取消息体，将当前数据块追加到resultBuffer，然后打印出完整消息内容
                        resultBuffer.appendBuffer(buffer);
                        System.out.println(resultBuffer.toString());
                        // 重置一轮
                        parser.fixedSizeMode(8);
                        size = -1;
                        resultBuffer = Buffer.buffer();
                    }
                }
            });
            socket.handler(parser);
        });

        // 启动 TCP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port: " + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTCPServer().doStart(8888);
    }

}
