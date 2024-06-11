package org.example.lurpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.protocol.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: 鹿又笑
 * @Create: 2024/5/30 23:06
 * @description:
 */
public class VertxTcpClient {

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @param serviceMetaInfo
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws InterruptedException, ExecutionException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        // 由于 Vert.x 提供的请求处理器是异步、反应式的，我们为了更方便地获取结果，可以使用 CompletableFuture 转异步为同步
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {
            if (!result.succeeded()) {
                System.err.println("Failed to connect to TCP server");
                return;
            }
            NetSocket socket = result.result();
            // 发送数据
            // 构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializeEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);
            // 编码请求
            try {
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息错误");
            }
            // 接收响应
            TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            socket.handler(tcpBufferHandlerWrapper);
        });
        RpcResponse rpcResponse = responseFuture.get();
        // 关闭连接
        netClient.close();
        return rpcResponse;
    }

//    public static void main(String[] args) {
//        new VertxTcpClient().start();
//    }

//    public void start() {
//        // 创建 Vert.x 实例
//        Vertx vertx = Vertx.vertx();
//
//        vertx.createNetClient().connect(8888, "localhost", result -> {
//            if (result.succeeded()) {
//                System.out.println("Connected to TCP server");
//                io.vertx.core.net.NetSocket socket = result.result();
//                for (int i = 0; i < 1000; i++) {
//                    // 发送数据
//                    Buffer buffer = Buffer.buffer();
//                    String str = "Hello, server!Hello, server!Hello, server!Hello, server!";
//                    buffer.appendInt(0);
//                    buffer.appendInt(str.getBytes().length);
//                    buffer.appendBytes(str.getBytes());
//                    socket.write(buffer);
//                }
//                // 接收响应
//                socket.handler(buffer -> {
//                    System.out.println("Received response from server: " + buffer.toString());
//                });
//            } else {
//                System.err.println("Failed to connect to TCP server");
//            }
//        });
//    }

}
