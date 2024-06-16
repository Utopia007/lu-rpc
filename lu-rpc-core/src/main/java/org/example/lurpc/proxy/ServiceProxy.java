package org.example.lurpc.proxy;

import cn.hutool.core.collection.CollUtil;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.constant.RpcConstant;
import org.example.lurpc.fault.tolerant.TolerantStrategy;
import org.example.lurpc.fault.tolerant.TolerantStrategyFactory;
import org.example.lurpc.loadbalancer.LoadBalancer;
import org.example.lurpc.loadbalancer.LoadBalancerFactory;
import org.example.lurpc.model.RpcRequest;
import org.example.lurpc.model.RpcResponse;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.example.lurpc.fault.retry.RetryStrategy;
import org.example.lurpc.fault.retry.RetryStrategyFactory;
import org.example.lurpc.serializer.Serializer;
import org.example.lurpc.serializer.SerializerFactory;
import org.example.lurpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
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
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化
        // byte[] bodyBytes = serializer.serialize(rpcRequest);

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServerKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 从注册中心获取到的服务节点地址可能是多个。暂时先取第一个，之后再优化
//            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路径）作为负载均衡参数
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        // TCP请求
        // 使用重试机制
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
//            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();

            // 发送 http 请求
//            HttpResponse httpResponse = null;
//            try {
//                System.out.println("selectedServiceMetaInfo.getServiceAddress() = " + selectedServiceMetaInfo.getServiceAddress());
//                httpResponse = HttpRequest.post("http://127.0.0.1:8888").body(bodyBytes).execute();
//                byte[] res = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deSerialize(res, RpcResponse.class);
//                return rpcResponse.getData();
//            } finally {
//                if (httpResponse != null) {
//                    httpResponse.close();
//                }
//            }
        } catch (Exception e) {
            // 容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();
    }

}
