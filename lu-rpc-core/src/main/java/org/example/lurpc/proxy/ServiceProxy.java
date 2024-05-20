package org.example.lurpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.constant.RpcConstant;
import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.example.lurpc.serializer.Serializer;
import org.example.lurpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @auther : LuYouxiao
 * @date 2024/3/31   -18:38
 * @Description
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        final Serializer serializer = new JDKSerializer();
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServerKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址111");
            }

            // todo 从注册中心获取到的服务节点地址可能是多个。暂时先取第一个，之后再优化
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送请求
            HttpResponse httpResponse = null;
            try {
                System.out.println("selectedServiceMetaInfo.getServiceAddress() = " + selectedServiceMetaInfo.getServiceAddress());
                httpResponse = HttpRequest.post("http://127.0.0.1:8888").body(bodyBytes).execute();
                byte[] res = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deSerialize(res, RpcResponse.class);
                return rpcResponse.getData();
            } finally {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }











}










