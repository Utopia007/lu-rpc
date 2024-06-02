package org.example.provider;

import org.example.common.service.UserService;
import org.example.lurpc.RpcApplication;
import org.example.lurpc.config.RegistryConfig;
import org.example.lurpc.config.RpcConfig;
import org.example.lurpc.model.ServiceMetaInfo;
import org.example.lurpc.registry.LocalRegistry;
import org.example.lurpc.registry.Registry;
import org.example.lurpc.registry.RegistryFactory;
import org.example.lurpc.server.tcp.VertxTCPServer;

/**
 * @Author: 鹿又笑
 * @Create: 2024/6/2 17:22
 * @description:
 */
public class ProviderExampleForTCP {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.registerService(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 TCP 服务
        VertxTCPServer vertxTCPServer = new VertxTCPServer();
        vertxTCPServer.doStart(8080);
    }

}
