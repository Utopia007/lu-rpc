package org.example.lurpc.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;
import org.example.lurpc.registry.LocalRegistry;
import org.example.lurpc.serializer.Serializer;
import org.example.lurpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -17:46
 * @Description
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    /*
    业务流程如下：
        1 反序列化请求为对象，并从请求对象中获取参数。
        2 根据服务名称从本地注册器中获取到对应的服务实现类。
        3 通过反射机制调用方法，得到返回结果。
        4 对返回结果进行封装和序列化，并写入到响应中。
     */

    @Override
    public void handle(HttpServerRequest request) {

//        final Serializer serializer = new JDKSerializer();
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 日志
        System.out.println("Received request: " + request.method() + " ***** " + request.uri());

        // 异步处理http请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deSerialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为 null，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest为空 is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // 获取要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                System.out.println(rpcRequest.getMethodName()+"-------"+rpcRequest.getParameterTypes());
                Object invoke = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // 封装返回结果
                rpcResponse.setData(invoke);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("=== ok");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 响应
            doResponse(request, rpcResponse, serializer);
        });

    }

    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {

        HttpServerResponse response = request.response();
        response.putHeader("content-type", "application/json");

        try {
            // 序列化
            byte[] serialize = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(serialize));
        } catch (IOException e) {
            response.end(Buffer.buffer());
            throw new RuntimeException(e);
        }

    }


}
