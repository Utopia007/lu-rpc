package org.example.lurpc.server.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;
import org.example.lurpc.protocol.*;
import org.example.lurpc.registry.LocalRegistry;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/2 15:34
 * @description: 请求处理器  接受请求，然后通过反射调用服务实现类。
 */
public class TCPServerHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket socket) {
        // 处理连接
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接收请求，解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误 in tcpServerHandler");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();

            // 处理请求
            // 构造响应结果对象
            RpcResponse response = new RpcResponse();

            // 获取要调用的服务实现类，通过反射调用
            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object invoke = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // 封装返回结果
                response.setData(invoke);
                response.setDataType(method.getReturnType());
                response.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                response.setMessage("错误调用," + e.getMessage());
                response.setException(e);
            }

            // 发送响应，编码
//            ProtocolMessage.Header header = new ProtocolMessage<>().getHeader();
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, response);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                socket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        socket.handler(bufferHandlerWrapper);
    }
}
